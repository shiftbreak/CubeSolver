import java.util.Random;


public class TwoPhaseSolver implements Solver, Runnable{


	private TwoPhaseLogicalCube start;
	private byte[] movesP1;
	private byte[] movesP2;
	private byte[] moves;
	private subCubedCube subCubed;


	
	public TwoPhaseSolver (TwoPhaseLogicalCube cube, subCubedCube subCubed){
		start = cube;
		this.subCubed = subCubed;
		movesP1 = new byte[0];
		movesP2 = new byte[0];
	}

	
	
	
	
	
	private boolean searchRecursePhase1Part1(byte[] moveList, TwoPhaseLogicalCube cube, int length, int index){
		for(byte i=7;i<25;i++){
			moveList[index] = i;
			if(index == length-1){
				if(cube.tryMatchP1(i)){
					cube.doMoveP1(i);
					updateMovesP1(moveList);
					return true;
				}
			}else{
				int CO = cube.getCO();
				int EO =cube.getEO();
				int UD = cube.getUD();
				cube.doMoveP1(i);
				if(searchRecursePhase1Part1(moveList, cube, length, (byte)index+1)){
					return true;
				}else{
					cube.setCO(CO);
					cube.setEO(EO);
					cube.setUD(UD);
				}
			}
		}
		return false;
	}
	private boolean searchRecursePhase1Part2(byte[] moveList, TwoPhaseLogicalCube cube, int length, int index){
		
		for(byte i=7;i<25;i++){
			moveList[index] = i;
			
			if(index == length-1){
				
				if(cube.tryMoveP1(i)){
					cube.doMoveP1(i);
					updateMovesP1(moveList);
					return true;
				}
			}else{
				int CO = cube.getCO();
				int EO =cube.getEO();
				int UD = cube.getUD();
				cube.doMoveP1(i);
				if(searchRecursePhase1Part2(moveList, cube, length, (byte)index+1)){
					return true;
				}else{
					cube.setCO(CO);
					cube.setEO(EO);
					cube.setUD(UD);
				}
			}
		}
		
		return false;
		
	}
	public int CP = 0;
	public int P2EP = 0;
	public int P2UD = 0;
	private boolean localPrune(byte prevMove, byte nextMove){
		if(prevMove == CubeConstants.P2U|| prevMove == CubeConstants.P2UU||prevMove == CubeConstants.P2U2){
			if(nextMove == CubeConstants.P2U|| nextMove == CubeConstants.P2UU||nextMove == CubeConstants.P2U2){
				return false;
			}
		}else if(prevMove == CubeConstants.P2D|| prevMove == CubeConstants.P2DD||prevMove == CubeConstants.P2D2){
			if(nextMove == CubeConstants.P2D|| nextMove == CubeConstants.P2DD||nextMove == CubeConstants.P2D2){
				return false;
			}
		}else if(prevMove == CubeConstants.P2L2){
			if(nextMove == CubeConstants.P2L2){
				return false;
			}
		}else if(prevMove == CubeConstants.P2R2){
			if(nextMove == CubeConstants.P2R2){
				return false;
			}
		}else if(prevMove == CubeConstants.P2F2){
			if(nextMove == CubeConstants.P2F2){
				return false;
			}
		}else if(prevMove == CubeConstants.P2B2){
			if(nextMove == CubeConstants.P2B2){
				return false;
			}
		}return true;
	}
	private static void printArray(byte[] a){
		System.out.println("");
		for(int i=0;i<a.length;i++){
			System.out.print(" " + a[i]);
		}
		System.out.println("");
	}
	
	private boolean searchRecursePhase2Part1(byte[] moveList, TwoPhaseLogicalCube cube, int length, int index){
		for(byte i=0;i<10;i++){
				moveList[index] = i;
				if(index == length-1){
					if(cube.tryMatchP2(i)){
						cube.doMoveP2(i);
						updateMovesP2(moveList);
						return true;
					}
				}else{
					int CP = cube.getCP();
					int P2EP =cube.getP2EP();
					int P2UD = cube.getP2UD();
					cube.doMoveP2(i);
					if(searchRecursePhase2Part1(moveList, cube, length, index+1)){
						return true;
					}else{
						cube.setCP(CP);
						cube.setP2EP(P2EP);
						cube.setP2UD(P2UD);
					}
				}
			}
		return false;
	}
	
	private boolean searchRecursePhase2Part1(byte[] moveList,  int[] coords, int length, int index){
		for(byte i=0;i<10;i++){
				moveList[index] = i;
				if(index == length-1){
					if(start.tryMatchP2(i, coords)){
						start.doMoveP2(moveList);
						updateMovesP2(moveList);
						return true;
					}
				}else {
					if(searchRecursePhase2Part1(moveList,start.doMoveP2(i, coords), length, index+1)){
						return true;
					}
				
					
				}
			}
		return false;
	}
	
	
	
	
	private boolean searchRecursePhase2Part2(byte[] moveList, TwoPhaseLogicalCube cube, int length, int index){
		for(byte i=0;i<10;i++){
				moveList[index] = i;
				if(index == length-1){
					if(cube.tryMoveP2(i)){
						cube.doMoveP2(i);
						updateMovesP2(moveList);
						System.out.println("FOUND IT:");
						tallyMoves();
						System.out.println("");

						return true;
					}
				}else{
					int CP = cube.getCP();
					int P2EP =cube.getP2EP();
					int P2UD = cube.getP2UD();
					cube.doMoveP2(i);
					if(searchRecursePhase2Part2(moveList, cube, length, (byte)index+1)){
						return true;
					}else{
						cube.setCP(CP);
						cube.setP2EP(P2EP);
						cube.setP2UD(P2UD);
					}
				}
			}
		return false;
	}
	private void updateMovesP1(byte[] update){
		byte[] n = new byte[movesP1.length+update.length];
		for(int i=0;i<movesP1.length;i++){
			n[i] = movesP1[i];
		}
		for(int j=0;j<update.length;j++){
			n[j+movesP1.length] = update[j];
		}
		movesP1 = new byte[n.length];
		movesP1 = n;
	}
	
	private void updateMovesP2(byte[] update){
		byte[] n = new byte[movesP2.length+update.length];
		for(int i=0;i<movesP2.length;i++){
			n[i] = movesP2[i];
		}
		for(int j=0;j<update.length;j++){
			n[j+movesP2.length] = update[j];
		}
		movesP2= new byte[n.length];
		movesP2 = n;
	}
	
	public void tallyMoves(){
		moves = new byte[movesP1.length+movesP2.length];
		for(int j=0;j<movesP1.length+movesP2.length;j++){
			if(j<movesP1.length){
				moves[j] = movesP1[j];
			}else{
				moves[j] = P2MovesToP1Moves(movesP2)[j-movesP1.length];
			}
		}
	}

	public void PhaseOne(){
		
		System.out.println("\nStarting Phase 1!");
		if(start.isSolvedP1()){
			System.out.println("Already Solved!");
			System.out.println("");
		}else{
			start.loadP1PrunTable();
			byte limit = 12;
			for(byte length = 1;length<=limit;length++){
				System.out.print(length + ": ");
				double begin = System.currentTimeMillis();
				if(searchRecursePhase1Part1(new byte[length], start, length, 0)){
					length = (byte) (limit+1);
				}
				double end = System.currentTimeMillis();
				double time = end - begin;
				System.out.println(" " + time);
			}
			System.out.println("(part 1 complete)");
			if(start.isSolvedP1()){
				System.out.println("FOUND IT (int part 1)");
				tallyMoves();
				System.out.println("");

			}else{
				limit = 7;
				for(byte length = 1;length<=limit;length++){
					int l = length+movesP1.length;
					System.out.print(l + ": ");
					double begin = System.currentTimeMillis();
					if(searchRecursePhase1Part2(new byte[length], start, length, 0)){
						length = (byte) (limit+1);
					}
					double end = System.currentTimeMillis();
					double time = end - begin;
					System.out.println(" " + time);
					
				}
			}
		}
		start.dumpP1PruningTable();
	}
	
	
	public void PhaseTwo(){
		System.out.println("\nStarting Phase 2!");
		start.setP2CoOrdinates(movesP1, subCubed);
		if(start.isSolvedP2()){
			System.out.println("Phase 2 not required!");
			tallyMoves();
			System.out.println("");

		}else{
			start.loadP2PrunTable();
			if(!start.tryMatchP2()){
				byte limit = 18;
			
				for(byte length = 1;length<=limit;length++){
					
					int l = length+movesP1.length;
					System.out.print(l + ": ");
					double begin = System.currentTimeMillis();
					//int[] coords = new int[]{start.getCP(), start.getP2UD(),start.getP2EP()};
					if(searchRecursePhase2Part1(new byte[length], start, length, 0)){
						length = (byte) (limit+1);
					}
					double end = System.currentTimeMillis();
					double time = end - begin;
					System.out.println(" " + time);
				}
			}
			System.out.println("(part 1 complete)");
			if(start.isSolvedP2()){
				System.out.println("FOUND IT (in part 1)");
				tallyMoves();
				System.out.println("");

			}else{
				byte limit = 18;
				for(byte length = 1;length<=limit;length++){
					int l = length+movesP2.length+movesP1.length;;
					System.out.print(l + ": ");
					double begin = System.currentTimeMillis();
					
					if(searchRecursePhase2Part2(new byte[length], start, length, 0)){
						length = (byte) (limit+1);
					}
					double end = System.currentTimeMillis();
					double time = end - begin;
					System.out.println(" " + time);
					
				}
			}
		}
		start.dumpP2PruningTable();
	}
	public byte[] P2MovesToP1Moves(byte[] P2M){
		byte[]out = new byte[P2M.length];
		for(int i=0;i<out.length;i++){
			if(P2M[i] == CubeConstants.P2U){
				out[i] = CubeConstants.U;
			}else if(P2M[i] == CubeConstants.P2UU){
				out[i] = CubeConstants.UU;
			}else if(P2M[i] == CubeConstants.P2U2){
				out[i] = CubeConstants.U2;
			}else if(P2M[i] == CubeConstants.P2D){
				out[i] = CubeConstants.D;
			}else if(P2M[i] == CubeConstants.P2DD){
				out[i] = CubeConstants.DD;
			}else if(P2M[i] == CubeConstants.P2D2){
				out[i] = CubeConstants.D2;
			}else if(P2M[i] == CubeConstants.P2L2){
				out[i] = CubeConstants.L2;
			}else if(P2M[i] == CubeConstants.P2F2){
				out[i] = CubeConstants.F2;
			}else if(P2M[i] == CubeConstants.P2R2){
				out[i] = CubeConstants.R2;
			}else if(P2M[i] == CubeConstants.P2B2){
				out[i] = CubeConstants.B2;
			}
			
		}
		return out;
	}
	
	
	public boolean solve(){
		PhaseOne();
		PhaseTwo();
		return true;
	}
	public void sMove(byte[] moves){
		subCubed.doMove(moves);
		start.setEO(subCubed.getEO());
		start.setCO(subCubed.getCO());
		start.setUD(subCubed.getUD());
		start.setP2EP(subCubed.getP2EP());
		start.setP2UD(subCubed.getP2UD());
		start.setCP(subCubed.getCP());
	}
	
	public void sMove(byte move){
		subCubed.doMove(move);
		start.setEO(subCubed.getEO());
		start.setCO(subCubed.getCO());
		start.setUD(subCubed.getUD());
		start.setP2EP(subCubed.getP2EP());
		start.setP2UD(subCubed.getP2UD());
		start.setCP(subCubed.getCP());
		}
	
	
	public static void main(String[] argv){
		
		
		
		CoOrdinateCube cube = new CoOrdinateCube();
	//	byte [] aa = new byte []{CubeConstants.P2D2,CubeConstants.P2B2,CubeConstants.P2DD,CubeConstants.P2L2, CubeConstants.P2R2,CubeConstants.P2UU, CubeConstants.P2L2,CubeConstants.P2U, CubeConstants.P2B2,CubeConstants.P2DD, CubeConstants.P2F2};
		
		byte [] aba = new byte []{CubeConstants.F2,CubeConstants.R2,CubeConstants.U,CubeConstants.D2,CubeConstants.B2,CubeConstants.DD,CubeConstants.L2, CubeConstants.R2,CubeConstants.UU, CubeConstants.L2,CubeConstants.U, CubeConstants.B2};//,CubeConstants.UU, CubeConstants.F2};
		
		
		cube.doMoveP1(aba);
		cube.printCube();
		//	cube.printCube();
		
		//cube.randomise(new Random(System.currentTimeMillis()));
		
		/*cube.doMoveP1(abc);*/
		subCubedCube c = new subCubedCube();
		//subCubedCube d = new subCubedCube();
		c.doMove(aba);
		//d.doMove(abc);
		
		
		TwoPhaseSolver a = new TwoPhaseSolver(cube, c);
		a.solve();
		
	/*	System.out.println("BEFORE:");
		d.printCubeB();
		d.doMove(a.moves);
		for(int i=0;i<a.moves.length;i++){
			System.out.print(a.moves[i] + " ");
		}
		
		System.out.println("AFTER:");
		d.printCubeB();
		System.out.println("");
		d.printCube();*/

	}

	public byte[] getMoves() {
		return moves;
	}

	public int getNumMoves() {
		return moves.length;
	}





	public void run() {
		solve();
		
	}
	
	


}
