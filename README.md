# CubeSolver

A Rubik's cube solver built from first principles as a final year dissertation at the University of Leeds in 2006/2007 — before solver libraries existed and before Stack Overflow was a resource.

**Ranulf Green — BSc Computer Science, University of Leeds (2006/2007)**

---

## What this actually is

This isn't a tutorial project. It's a full implementation of a bidirectional heuristic search algorithm developed independently, with the hard constraint of 500MB RAM on a 1600MHz Pentium M — and no existing libraries to lean on.

During development I corresponded directly with Herbert Kociemba — the mathematician whose two-phase algorithm is the gold standard for Rubik's cube solving. Where I couldn't fully implement his approach (symmetry reduction was too complex to derive independently), I engineered around the gaps, designing a custom bit-packed pruning table that fit the available memory.

The system takes a scrambled cube as input, generates a near-optimal move sequence using IDA* search, and then physically executes those moves via a 3-jaw Lego Mindstorms robot — entirely without human intervention after the initial state is entered.

## Results

- 100% reliability across 157 consecutive randomly generated test cubes
- Average **24 moves** to solution (vs. 104 for the naive 7-stage approach)
- Average **2.4 minutes** to generate solution on test hardware
- Worst case **11.76 minutes**

| | Basic System | Improved System |
|---|---|---|
| Algorithm | 7-stage (layer-by-layer) | Two-phase (Kociemba-style IDA*) |
| Avg. moves to solution | 104 | 24 |
| Avg. time to generate | ~10ms | ~2.4 min (incl. table loading) |
| Robot solve time (avg.) | ~117 min | ~27 min |

---

## The Robot

Built from Lego Mindstorms RCX hardware. Controlled by two RCX units (each a Hitachi 8-bit 16MHz microcontroller with only **32KB RAM**):

- **RCX 1 (Rotating)** — controls the three jaws for face rotation: Left, Right, and Centre jaws, each rotating 90° in both directions. Touch sensors (mounted 90° apart) detect rotation completion.
- **RCX 2 (Gripping)** — controls jaw open/close operations using rotational sensors.

The 3-jaw design (two opposite + one at right angle) was chosen because it only requires 2 RCX units while still being able to access all 6 faces by re-orienting the cube. Jaws were lubricated and gear ratios tuned to balance grip force against rotation accuracy. Rubber pads were glued to jaw paddles to increase friction on the cube surface.

A custom IR communications protocol was built on top of LeJOS LNP (Lego Network Protocol) to reliably pass commands from the host PC to each RCX independently, with acknowledgement, duplicate suppression, and timeout-based retransmit to handle dropped packets.

---

## Software Architecture

The system is split into 7 interoperable Java components:

1. **LogicalCube** — in-memory representation of the cube state; applies face rotations
2. **Solver** — generates a move list that transforms the scrambled state to solved
3. **Interface (CubeUI)** — Java Swing GUI for inputting the cube's initial colours
4. **RCX 1 program** — LeJOS firmware program for rotation movements
5. **RCX 2 program** — LeJOS firmware program for open/close movements
6. **TranslationService** — converts high-level moves (U, B2, R…) into low-level robot manoeuvres
7. **RCXCom** — sends commands over IR to the correct RCX unit

---

## The Algorithms

### Why brute force is impossible

The Rubik's cube has ~4.3×10¹⁹ possible permutations. Exhaustively searching move sequences of the length required to solve a random cube was estimated to take 1.4 million years. The solution space must be partitioned into independent sub-problems.

### Basic Solver — 7-Stage Algorithm

The first iteration mirrors the classic human layer-by-layer method, solving the cube in 7 stages (top cross → top corners → middle edges → bottom cross → bottom corner placement → bottom corner orientation → bottom edge positions). Each stage selects the shortest applicable move sequence from a hand-coded lookup table. Averaged **104 moves** to solution across 10,000 random test cubes in ~10ms.

### Improved Solver — Two-Phase IDA* with Coordinate Representation

The second iteration implements a variant of Kociemba's two-phase algorithm — a significantly harder undertaking that required building an entirely new logical model of the cube from scratch.

**Phase 1** reduces the cube to a subgroup where only {U, UU, D, DD, L2, R2, F2, B2} are needed — solving corner orientations, edge orientations, and placing the 4 UD-slice (middle layer) edges into the middle layer in any order.

**Phase 2** solves the remaining position permutations using only those 8 moves.

Each phase uses **IDA\* iterative deepening depth-first search** guided by pruning tables.

#### Coordinate representation: the cube as 6 integers

Rather than tracking 20 individual sub-cube positions and orientations, the entire cube state is encoded into 6 compact integer coordinates:

| Phase 1 | Range | Phase 2 | Range |
|---|---|---|---|
| Corner Orientation (CO) | 0–2186 | Corner Position (CP) | 0–40319 |
| Edge Orientation (EO) | 0–2047 | Edge Position (P2EP) | 0–40319 |
| UD-Slice position (UD) | 0–494 | UD-Slice ordered (P2UD) | 0–23 |

A face rotation becomes three array lookups — O(1) per move regardless of cube complexity:

```java
// CoOrdinateCube.java — applying a phase 1 move
public void doMoveP1(byte movement) {
    CO = COMovTable[movement-7][CO];
    EO = EOMovTable[movement-7][EO];
    UD = UDSliceMovTable[movement-7][UD];
}
```

Move tables were pre-generated by iterating all 2048–40320 coordinate values, decoding each to a sub-cube permutation, applying the move, and re-encoding the result. They are serialised to disk on first run and loaded on subsequent runs.

#### The IDA* search with coordinate rollback

The search avoids the cost of deep-copying cube state by saving and restoring only the 3 integer coordinates on backtrack:

```java
// TwoPhaseSolver.java — IDA* search for phase 1
private boolean searchRecursePhase1Part2(byte[] moveList, TwoPhaseLogicalCube cube,
                                          int length, int index) {
    for (byte i = 7; i < 25; i++) {
        moveList[index] = i;
        if (index == length - 1) {
            if (cube.tryMoveP1(i)) {
                cube.doMoveP1(i);
                updateMovesP1(moveList);
                return true;
            }
        } else {
            int CO = cube.getCO();   // save 3 ints — not a full object copy
            int EO = cube.getEO();
            int UD = cube.getUD();
            cube.doMoveP1(i);
            if (searchRecursePhase1Part2(moveList, cube, length, (byte) index + 1)) {
                return true;
            } else {
                cube.setCO(CO);      // restore on backtrack
                cube.setEO(EO);
                cube.setUD(UD);
            }
        }
    }
    return false;
}
```

The pruning table short-circuits the search: at each leaf, `tryMatchP1` checks whether the resulting coordinate triple has been reached in the pre-computed table before committing the move. This eliminates entire subtrees without exploring them.

#### The RAM problem — and how it was solved

The phase 1 pruning table needs to track all permutations reachable in 6 moves from the solved state:

```
2048 (EO) × 2187 (CO) × 495 (UD) = 2,217,093,120 values
```

Storing one bit per value and packing 8 bits per byte collapses this to **264MB**. The phase 2 table is **193MB**. Both cannot fit simultaneously in 500MB RAM.

The implementation in `PruningTable.java` packs 8 boolean values into each byte using bitwise operations, and backs the entire multi-dimensional table with a single flat `byte[]` — reducing Java's per-array object overhead from 16 bytes × millions of sub-arrays to 16 bytes total:

```java
// PruningTable.java — bit-packing 8 values into one byte
public void setReached(int hPlace, int vPlace, int dPlace) {
    long ww = ((long)(width * height) * dPlace + width * vPlace + hPlace);
    int w = (int)(ww / 8);
    list[w] = (byte)(list[w] | computeMask(ww));  // set one bit without disturbing the other 7
}

public boolean getReached(int hPlace, int vPlace, int dPlace) {
    long ww = ((long)(width * height) * dPlace + width * vPlace + hPlace);
    int w = (int)(ww / 8);
    int mask = computeMask(ww);
    return (list[w] & mask) == mask;
}
```

Since both tables can't coexist in memory, each is loaded from disk, used, then explicitly nulled (`dumpP1PruningTable()`) before the other is loaded. Tables are generated once and persisted via Java serialisation, regenerated automatically if missing:

```java
// CoOrdinateCube.java — lazy table generation
} catch (FileNotFoundException e) {
    System.out.println("PRUNING TABLES NOT FOUND: GENERATING!");
    generatePruningTables();
    loadP1PrunTable();
}
```

**Phase 2 approximation**: a full phase 2 pruning table across all 3 coordinates would require 4.65GB. The table was reduced to 193MB by dropping the P2UD coordinate, making phase 2 use an estimated rather than exact heuristic. The search depth is capped at 7 to keep worst-case time reasonable.

---

## Constraints and engineering decisions

**No libraries, no Stack Overflow.** This was 2006. The reference implementations that existed (Kociemba's *Cube Explorer*, Herbert Kociemba's own work) were closed-source or documented only at a high level. The coordinate encoding, move table generation, pruning table construction, and IDA* search were all derived independently from academic papers and a direct email exchange with Kociemba himself.

**32KB RCX RAM.** LeJOS does not support garbage collection — every variable created in a loop permanently consumes memory for the lifetime of the program. All RCX variables had to be pre-allocated at program start. The RCX programs were designed to initialise all state upfront and run within a fixed memory budget for the duration of a full solve.

**IR packet loss.** The Lego infrared link between PC and RCX drops packets. A custom communications protocol was built from scratch with acknowledgement, timeout-based retransmit, and duplicate suppression — guaranteeing each command is received and executed exactly once even under packet loss. The `RCXLNPAddressingPort` could only address one RCX at a time, requiring the port to be opened and closed for each message to alternate between the two units.

**Lego mechanical flex.** Lego components flex under rotational load, causing the jaw's actual turn to deviate from the motor's sensor reading. Touch sensors were remounted on sliding rods so they could be calibrated to the exact endpoint of each jaw rotation rather than relying on motor encoder readings.

**Symmetry reduction not implemented.** The full Kociemba implementation uses symmetry coordinates to reduce pruning table sizes by a factor of 16 — bringing phase 1 to ~27MB and phase 2 to ~22MB, both fitting easily in RAM. Deriving those symmetry classes was beyond the scope of a single-person final year project. The workaround — bit-packing, flat arrays, and swapping tables in and out of memory — achieved correctness at the cost of ~111 seconds of load time per solve. With both tables resident simultaneously (which would require ~457MB headroom), average solve time drops to an estimated 32.89 seconds.

---

## The robot

The solver drives a physical Lego Mindstorms robot with three jaws (two opposing, one perpendicular), two RCX units, and a custom IR communications protocol with retry logic and duplicate detection. A move translation service converts the solver's output (e.g. `U`, `B2`, `RR`) into sequences of physical jaw open/close/rotate operations. The robot re-orientates the cube between moves as needed so any face can be reached with only 3 jaws.

---

## Built with

Java · LeJOS firmware · Lego Mindstorms RCX · IDA* · Kociemba two-phase algorithm

---

## Dissertation

The full dissertation is included in this repository: [`dissertation ranulf.pdf`](dissertation%20ranulf.pdf)
