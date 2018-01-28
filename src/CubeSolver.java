/*
 * This class solves the cube using an extension of the human
 * 7 stage algorithm:
 * 
 * 1: Solve the top layer cross
 * 2: Place the top layer Corners
 * 3: Place the Middle layer edges
 * 4: Solve the bottom layer cross
 * 5: Place the Bottom layer corners
 * 6: Orientate the bottom layer corners
 * 7: Place the bottom layer edges 
 * 
 * The algorithm generates a set of operations that caried out on the
 * sample cube would generate a solved cube.
 * 
 * From testing the average solve time is ~84ms and the average move-count
 * is ~104 moves. (This was carried out over a sample of 1000 random cubes.
 * 
 */

/**
 * CubeSolver: Generates a solution to a Rubik's cube (fast but non optimal)
 * Requires a Cube object to be passed to it in the constructor.
 * 
 * @version 1.0
 * @author Ranulf Green
 * 
 * 
 */
public class CubeSolver implements Solver{
	
	private LogicalCube c;
	private int numMoves = 0;
	private static byte[] algorithm = new byte[0];
	/**
	 * Constructor
	 * @param c Cube to be passed to constructor
	 */
	
	public CubeSolver(LogicalCube c){
		this.c = c;
	}
	
	
	
	/**
	 * Call this method to generate a move list to solve the Cube
	 * @return true if a solution was found (most likely false if the input Cube was
	 * invalid)
	 */
	public boolean solve(){
		algorithm = new byte[0];
		if(!Cross()){
			return false;
		}if(!TopLayerCorners()){
			return false;
		}if(!SecondLayerEdges()){
			return false;
		}if(!BottomLayerCross()){
			return false;
		}if(!ThirdLayerCornerPosition()){
			return false;
		}if(!BottomLayerCornerOrientation()){
			return false;
		}if(!ThirdLayerEdgePosition()){
			return false;
		}
		return true;
	}
	

	
	private static byte[] searchCross(LogicalCube i){
		Cube[] temp = new Cube[24];
		for(byte ii=0;ii<24;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		
		
		byte[][] movelist = new byte[24][];
		movelist[0] = new byte[]{};// position back orientation correct
		movelist[1] = new byte[]{CubeConstants.B,CubeConstants.LL,CubeConstants.DD,CubeConstants.L,CubeConstants.B2}; // position back orientation reverse;
		movelist[2] = new byte[]{CubeConstants.L2,CubeConstants.DD,CubeConstants.B2,CubeConstants.L2}; // position left orientation correct
		movelist[3] = new byte[]{CubeConstants.LL,CubeConstants.BB,CubeConstants.L}; //  position left orientation reverse
		movelist[4] = new byte[]{CubeConstants.F2,CubeConstants.D2,CubeConstants.B2,CubeConstants.F2}; // position front orientation correct
		movelist[5] = new byte[]{CubeConstants.F2,CubeConstants.DD,CubeConstants.F2,CubeConstants.L,CubeConstants.BB,CubeConstants.LL}; // position front orientation reverse
		movelist[6] = new byte[]{CubeConstants.R2,CubeConstants.D,CubeConstants.B2,CubeConstants.R2}; // position right orientation ok
		movelist[7] = new byte[]{CubeConstants.R,CubeConstants.B,CubeConstants.RR}; // position right orientation reverse
		movelist[8] = new byte[]{CubeConstants.BB}; // position back left orientation top left
		movelist[9] = new byte[]{CubeConstants.LL,CubeConstants.DD,CubeConstants.L,CubeConstants.B2}; // position back left orientation top back
		movelist[10] = new byte[]{CubeConstants.L,CubeConstants.DD,CubeConstants.B2,CubeConstants.LL}; // position front left orientation top front
		movelist[11] = new byte[]{CubeConstants.L2,CubeConstants.BB,CubeConstants.L2}; // position front left orientation top left
		movelist[12] = new byte[]{CubeConstants.RR,CubeConstants.D,CubeConstants.B2,CubeConstants.R};// position front right orientation top front
		movelist[13] = new byte[]{CubeConstants.R2,CubeConstants.B,CubeConstants.R2};// position front right orientation top right
		movelist[14] = new byte[]{CubeConstants.B};// position back right orientation top right
		movelist[15] = new byte[]{CubeConstants.R,CubeConstants.D,CubeConstants.RR,CubeConstants.B2};// position back right orientation top back
		movelist[16] = new byte[]{CubeConstants.B2}; // position back orientation top down
		movelist[17] = new byte[]{CubeConstants.D,CubeConstants.L,CubeConstants.BB,CubeConstants.LL};// position back orientation top back
		movelist[18] = new byte[]{CubeConstants.DD,CubeConstants.B2};// position left orientation top down
		movelist[19] = new byte[]{CubeConstants.L,CubeConstants.BB,CubeConstants.LL};// position left orientation top left
		movelist[20] = new byte[]{CubeConstants.D2,CubeConstants.B2};// position front orientation top down
		movelist[21] = new byte[]{CubeConstants.DD,CubeConstants.L,CubeConstants.BB,CubeConstants.LL};// position front orientation top front
		movelist[22] = new byte[]{CubeConstants.D,CubeConstants.B2};// position right orientation top down
		movelist[23] = new byte[]{CubeConstants.RR,CubeConstants.B,CubeConstants.R};// position right orientation top right
		
		
		byte[] best = new byte[999];
		for(byte ii=0;ii<24;ii++){
			temp[ii].doMove(movelist[ii]);
		}
		for(byte ii = 0;ii<24;ii++){
			if(temp[ii].getUp()[1][1] == temp[ii].getUp()[0][1] && temp[ii].getBack()[1][1] == temp[ii].getBack()[0][1]){
				if(best.length!=999){
					if(best.length>movelist[ii].length){
						best = new byte[movelist[ii].length];
						best = movelist[ii];
					}
				}else if(best.length==999){
					best = new byte[movelist[ii].length];
					best = movelist[ii];
				}
			}
		}
		
		return best;
		
		
	}
	
	public boolean Cross(){
		byte[] movesBack = searchCross(c);
		c.doMove(movesBack);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesLeft = searchCross(c);
		c.doMove(movesLeft);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesFront = searchCross(c);
		c.doMove(movesFront);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesRight = searchCross(c);
		c.doMove(movesRight);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		if(movesBack.length==999||movesLeft.length==999||movesFront.length==999||movesRight.length==999){
			return false;
		}else{	
			byte[] AmovesBack = movesBack;
			byte[] AmovesLeft = transposeMoves(CubeConstants.CUBE_RIGHT,movesLeft);
			byte[] AmovesFront = transposeMoves(CubeConstants.CUBE_HORIZ_FLIP,movesFront);
			byte[] AmovesRight = transposeMoves(CubeConstants.CUBE_LEFT,movesRight);
			int len = (movesBack.length+movesLeft.length+movesFront.length+movesRight.length);
			numMoves+=len;
			byte[] temp = new byte[len];
			temp = addArray(addArray(addArray(AmovesBack,AmovesLeft),AmovesFront),AmovesRight);
			updateMoves(temp);
			return true;
		}
		
	}
		
	private static byte[] tlcSearch(LogicalCube i){
		Cube[] temp = new Cube[24];
		byte[][] movelist = new byte[24][];
		byte[] best = new byte[999];
		for(byte ii=0;ii<24;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		
		movelist[0] = new byte[]{};
		// Lower layer algorithms (all checked to be correct)
		movelist[12] = new byte[]{CubeConstants.LL,CubeConstants.DD,CubeConstants.L};// LL TOP LEFT     BACK LEFT
		movelist[13] = new byte[]{CubeConstants.B,CubeConstants.D,CubeConstants.BB}; // LL TOP left     TOP BACK
		movelist[14] = new byte[]{CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.D2,CubeConstants.B,CubeConstants.D,CubeConstants.BB};//ll top left  TOP DOWN
		movelist[15] = new byte[]{CubeConstants.DD,CubeConstants.LL,CubeConstants.DD,CubeConstants.L};// front left   top front
		movelist[16] = new byte[]{CubeConstants.DD,CubeConstants.B,CubeConstants.D,CubeConstants.BB};// front left    top left
		movelist[17] = addArray(new byte[]{CubeConstants.DD},movelist[14]);//  front left top down
		movelist[18] = new byte[]{CubeConstants.D2,CubeConstants.LL,CubeConstants.DD,CubeConstants.L};// front right top right
		movelist[19] = new byte[]{CubeConstants.D2,CubeConstants.B,CubeConstants.D,CubeConstants.BB};// front right top front
		movelist[20] = addArray(new byte[]{CubeConstants.D2},movelist[14]);// front right top down
		movelist[21] = new byte[]{CubeConstants.D,CubeConstants.LL,CubeConstants.DD,CubeConstants.L};// back right top back
		movelist[22] = new byte[]{CubeConstants.D,CubeConstants.B,CubeConstants.D,CubeConstants.BB};  // back right top right
		movelist[23] = addArray(new byte[]{CubeConstants.D},movelist[14]);// back right top down
		// Upper layer algorithms (have reported a mistake very rarely)
		movelist[1] = new byte[]{CubeConstants.LL,CubeConstants.DD,CubeConstants.L,CubeConstants.D,CubeConstants.LL,CubeConstants.DD,CubeConstants.L}; // back left , top left
		movelist[2] = new byte[]{CubeConstants.B,CubeConstants.D,CubeConstants.BB,CubeConstants.DD,CubeConstants.B,CubeConstants.D,CubeConstants.BB};// back left top back
		movelist[3] = new byte[]{CubeConstants.FF,CubeConstants.DD,CubeConstants.F,CubeConstants.B,CubeConstants.D,CubeConstants.BB}; // front left top top
		movelist[4] = new byte[]{CubeConstants.FF,CubeConstants.DD,CubeConstants.F,CubeConstants.LL,CubeConstants.DD,CubeConstants.L}; // front left top front
		movelist[5] = new byte[]{CubeConstants.L,CubeConstants.D,CubeConstants.LL,CubeConstants.D2,CubeConstants.B,CubeConstants.D,CubeConstants.BB}; // front left top left
		movelist[6] = new byte[]{CubeConstants.F,CubeConstants.D2,CubeConstants.FF,CubeConstants.LL,CubeConstants.DD,CubeConstants.L}; // front right top top
		movelist[7] = new byte[]{CubeConstants.RR,CubeConstants.D2,CubeConstants.R,CubeConstants.LL,CubeConstants.DD,CubeConstants.L};  // front right top right
		movelist[8] = new byte[]{CubeConstants.F,CubeConstants.D2,CubeConstants.FF,CubeConstants.B,CubeConstants.D,CubeConstants.BB};  // front right top front
		movelist[9] = new byte[]{CubeConstants.R,CubeConstants.D,CubeConstants.RR,CubeConstants.LL,CubeConstants.DD,CubeConstants.L}; // back right top top
		movelist[10] = new byte[]{CubeConstants.BB,CubeConstants.DD,CubeConstants.B,CubeConstants.D2,CubeConstants.LL,CubeConstants.DD,CubeConstants.L}; // back right top back
		movelist[11] = new byte[]{CubeConstants.R,CubeConstants.D,CubeConstants.RR,CubeConstants.B,CubeConstants.D,CubeConstants.BB}; //back right top right
	
		
		
		for(byte ii=0;ii<24;ii++){
			temp[ii].doMove(movelist[ii]);
		}
				
		for(byte ii = 0;ii<24;ii++){
			
			if(temp[ii].getUp()[0][0] == temp[ii].getUp()[1][1] && temp[ii].getBack()[0][2] == temp[ii].getBack()[1][1]){
				if(best.length!=999){
					if(best.length>movelist[ii].length){
						best = new byte[movelist[ii].length];
						best = movelist[ii];
					}
				}else if(best.length==999){
					best = new byte[movelist[ii].length];
					best = movelist[ii];
				}
			}
		}
		
		return best;
	}
	public boolean TopLayerCorners(){
		byte[] movesBR = tlcSearch(c);
		c.doMove(movesBR);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesFR = tlcSearch(c);
		c.doMove(movesFR);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesFL = tlcSearch(c);
		c.doMove(movesFL);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesBL = tlcSearch(c);
		c.doMove(movesBL);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		if(movesBR.length==999||movesFR.length==999||movesFL.length==999||movesBL.length==999){
			return false;
		}else{
			byte[] AmovesBR = movesBR;
			byte[] AmovesFR = transposeMoves(CubeConstants.CUBE_RIGHT,movesFR);
			byte[] AmovesFL = transposeMoves(CubeConstants.CUBE_HORIZ_FLIP,movesFL);
			byte[] AmovesBL = transposeMoves(CubeConstants.CUBE_LEFT,movesBL);
			int len =(movesBR.length+movesFR.length+movesFL.length+movesBL.length);
			numMoves +=len;
			byte[] temp = new byte[len];
			temp = addArray(addArray(addArray(AmovesBR,AmovesFR),AmovesFL),AmovesBL);
			updateMoves(temp);
			return true;
		}
	}
	
	private  static byte[] sleSearch(LogicalCube i){
		Cube[] temp = new Cube[16];
		for(byte ii=0;ii<16;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		
		byte[][] movelist = new byte[16][];
		
		// in place do nothing
		movelist[0] = new byte[]{};
		// in place needs swapping
		movelist[1] = new byte[]{CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.DD,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Middle layer front left (left facing left)
		movelist[2] = new byte[]{CubeConstants.L,CubeConstants.DD,CubeConstants.LL,CubeConstants.DD,CubeConstants.FF,CubeConstants.D,CubeConstants.F,CubeConstants.D2,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Middle layer front left (left facing front)
		movelist[3] = new byte[]{CubeConstants.L,CubeConstants.DD,CubeConstants.LL,CubeConstants.DD,CubeConstants.FF,CubeConstants.D,CubeConstants.F,CubeConstants.DD,
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Middle layer front right (left facing right)
		movelist[4] = new byte[]{CubeConstants.RR,CubeConstants.D,CubeConstants.R,CubeConstants.D,CubeConstants.F,CubeConstants.DD,CubeConstants.FF,CubeConstants.D2,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Middle layer front right (left facing front)
		movelist[5] = new byte[]{CubeConstants.RR,CubeConstants.D,CubeConstants.R,CubeConstants.D,CubeConstants.F,CubeConstants.DD,CubeConstants.FF,CubeConstants.DD,
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Middle layer back right (left facing right)
		movelist[6] = new byte[]{CubeConstants.R,CubeConstants.DD,CubeConstants.RR,CubeConstants.DD,CubeConstants.BB,CubeConstants.D,CubeConstants.B,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Middle layer back right (left facing back)
		movelist[7] = new byte[]{CubeConstants.R,CubeConstants.DD,CubeConstants.RR,CubeConstants.DD,CubeConstants.BB,CubeConstants.D,CubeConstants.B,CubeConstants.D,
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		
		// Lower layer back (left facing down)
		movelist[8] = new byte[]{CubeConstants.DD, 
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Lower layer back (left facing back)		
		movelist[9] = new byte[]{CubeConstants.D2,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Lower layer left (left facing down)
		movelist[10] = new byte[]{CubeConstants.D2,
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Lower layer left (left facing left)
		movelist[11] = new byte[]{CubeConstants.D,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Lower layer front(left facing down)
		movelist[12] = new byte[]{CubeConstants.D,
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Lower layer front (left facing front)
		movelist[13] = new byte[]{
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		// Lower layer right (left facing down)
		movelist[14] = new byte[]{
				CubeConstants.LL,CubeConstants.D,CubeConstants.L,CubeConstants.D,CubeConstants.B,CubeConstants.DD,CubeConstants.BB};
		// Lower layer right left facing right)
		movelist[15] = new byte[]{CubeConstants.DD,
				CubeConstants.B,CubeConstants.DD,CubeConstants.BB,CubeConstants.DD,CubeConstants.LL,CubeConstants.D,CubeConstants.L};
		byte[] best = new byte[999];
		for(byte ii=0;ii<16;ii++){
			temp[ii].doMove(movelist[ii]);
		}
		for(byte ii = 0;ii<16;ii++){
			if(temp[ii].getBack()[1][2] == temp[ii].getBack()[1][1] && temp[ii].getLeft()[1][0] == temp[ii].getLeft()[1][1]){
				if(best.length!=999){
					if(best.length>movelist[ii].length){
						best = new byte[movelist[ii].length];
						best = movelist[ii];
					}
				}else if(best.length==999){
					best = new byte[movelist[ii].length];
					best = movelist[ii];
				}
			}
		}
		return best;
		
	}
	public boolean SecondLayerEdges(){
		byte[] movesBR = sleSearch(c);
		c.doMove(movesBR);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesFR = sleSearch(c);
		c.doMove(movesFR);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesFL = sleSearch(c);
		c.doMove(movesFL);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		byte[] movesBL = sleSearch(c);
		c.doMove(movesBL);
		c.rotateCube(CubeConstants.CUBE_LEFT);
		if(movesBR.length==999||movesFR.length==999||movesFL.length==999||movesBL.length==999){
			return false;
		}else{
			int len=(movesBR.length+movesFR.length+movesFL.length+movesBL.length);
			numMoves+=len;
			byte[] temp = new byte[len];
			byte[] AmovesBR = movesBR;
			byte[] AmovesFR = transposeMoves(CubeConstants.CUBE_RIGHT,movesFR);
			byte[] AmovesFL = transposeMoves(CubeConstants.CUBE_HORIZ_FLIP,movesFL);
			byte[] AmovesBL = transposeMoves(CubeConstants.CUBE_LEFT,movesBL);
			temp = addArray(addArray(addArray(AmovesBR,AmovesFR),AmovesFL),AmovesBL);
			updateMoves(temp);
			return true;
		}
	}
	
	private static byte[]blcSearch(LogicalCube i){
	
		Cube[] temp = new Cube[10];
		for(byte ii=0;ii<10;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		byte[][] movelist = new byte[10][];
		movelist[0] = new byte[]{};
		movelist[1] = new byte[]{CubeConstants.F,CubeConstants.U,CubeConstants.R,CubeConstants.UU,CubeConstants.RR,CubeConstants.FF};
		movelist[2] = new byte[]{CubeConstants.F,CubeConstants.R,CubeConstants.U,CubeConstants.RR,CubeConstants.UU,CubeConstants.FF};
		movelist[3] = new byte[]{CubeConstants.L,CubeConstants.U,CubeConstants.F,CubeConstants.UU,CubeConstants.FF,CubeConstants.LL};
		movelist[4] = new byte[]{CubeConstants.L,CubeConstants.F,CubeConstants.U,CubeConstants.FF,CubeConstants.UU,CubeConstants.LL};
		movelist[5] = new byte[]{CubeConstants.R,CubeConstants.U,CubeConstants.B,CubeConstants.UU,CubeConstants.BB,CubeConstants.RR};
		movelist[6] = new byte[]{CubeConstants.R,CubeConstants.B,CubeConstants.U,CubeConstants.BB,CubeConstants.UU,CubeConstants.RR};
		movelist[7] = new byte[]{CubeConstants.B,CubeConstants.U,CubeConstants.L,CubeConstants.UU,CubeConstants.LL,CubeConstants.BB};
		movelist[8] = new byte[]{CubeConstants.B,CubeConstants.L,CubeConstants.U,CubeConstants.LL,CubeConstants.UU,CubeConstants.BB};
		movelist[9] = addArray(movelist[1],movelist[4]);
		
				
		byte[] best = new byte[999];
		for(byte ii=0;ii<10;ii++){
			temp[ii].doMove(movelist[ii]);
		}
		
		for(byte ii = 0;ii<10;ii++){
			byte[][] t = temp[ii].getUp();
			byte j = t[1][1];
			if(j == t[0][1]&& j==t[1][0]&&j==t[2][1]&&j==t[1][2]){
				if(best.length!=999){
					if(best.length>movelist[ii].length){
						best = new byte[movelist[ii].length];
						best = movelist[ii];
					}
				}else if(best.length==999){
					best = new byte[movelist[ii].length];
					best = movelist[ii];
				}
			}
		}
		return best;
	}
	public boolean BottomLayerCross(){
		c.rotateCube(CubeConstants.CUBE_VERT_FLIP);
		byte[]movesBlc = blcSearch(c);
		c.doMove(movesBlc);
		if(movesBlc.length==999){
			return false;
		}else{
			int len =movesBlc.length;
			numMoves += len;
			byte[] temp = new byte[len];
			temp = transposeMoves(CubeConstants.CUBE_VERT_FLIP,movesBlc);
			updateMoves(temp);
			return true;
		}
	}
	
	private static byte[] tlcpSearch(LogicalCube i){
		Cube[] temp = new Cube[28];
		for(byte ii=0;ii<28;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		byte[][]movelist = new byte[28][];
		movelist[0] = new byte[]{};
		movelist[1] = new byte[]{CubeConstants.L,CubeConstants.UU,CubeConstants.RR,CubeConstants.U,CubeConstants.LL,CubeConstants.UU,CubeConstants.R,CubeConstants.U2};		
		movelist[2] = new byte[]{CubeConstants.R,CubeConstants.UU,CubeConstants.LL,CubeConstants.U,CubeConstants.RR,CubeConstants.UU,CubeConstants.L,CubeConstants.U2};		
		movelist[3] = new byte[]{CubeConstants.F,CubeConstants.UU,CubeConstants.BB,CubeConstants.U,CubeConstants.FF,CubeConstants.UU,CubeConstants.B,CubeConstants.U2};
		movelist[4] = new byte[]{CubeConstants.B,CubeConstants.UU,CubeConstants.FF,CubeConstants.U,CubeConstants.BB,CubeConstants.UU,CubeConstants.F,CubeConstants.U2};		
		movelist[5] = addArray(addArray(movelist[1],movelist[3]),movelist[1]);
		movelist[6] = addArray(addArray(movelist[2],movelist[3]),movelist[2]);
		
		movelist[7] = addArray(new byte[]{CubeConstants.U},movelist[0]);
		movelist[8] = addArray(new byte[]{CubeConstants.U},movelist[1]);
		movelist[9] = addArray(new byte[]{CubeConstants.U},movelist[2]);
		movelist[10] = addArray(new byte[]{CubeConstants.U},movelist[3]);
		movelist[11] = addArray(new byte[]{CubeConstants.U},movelist[4]);
		movelist[12] = addArray(new byte[]{CubeConstants.U},movelist[5]);
		movelist[13] = addArray(new byte[]{CubeConstants.U},movelist[6]);
		
		movelist[14] = addArray(new byte[]{CubeConstants.UU},movelist[0]);
		movelist[15] = addArray(new byte[]{CubeConstants.UU},movelist[1]);
		movelist[16] = addArray(new byte[]{CubeConstants.UU},movelist[2]);
		movelist[17] = addArray(new byte[]{CubeConstants.UU},movelist[3]);
		movelist[18] = addArray(new byte[]{CubeConstants.UU},movelist[4]);
		movelist[19] = addArray(new byte[]{CubeConstants.UU},movelist[5]);
		movelist[20] = addArray(new byte[]{CubeConstants.UU},movelist[6]);
		
		movelist[21] = addArray(new byte[]{CubeConstants.U2},movelist[0]);
		movelist[22] = addArray(new byte[]{CubeConstants.U2},movelist[1]);
		movelist[23] = addArray(new byte[]{CubeConstants.U2},movelist[2]);
		movelist[24] = addArray(new byte[]{CubeConstants.U2},movelist[3]);
		movelist[25] = addArray(new byte[]{CubeConstants.U2},movelist[4]);
		movelist[26] = addArray(new byte[]{CubeConstants.U2},movelist[5]);
		movelist[27] = addArray(new byte[]{CubeConstants.U2},movelist[6]);
		
		
		byte[] best = new byte[999];
		for(byte ii=0;ii<28;ii++){
			temp[ii].doMove(movelist[ii]);
		}
		
		for(byte ii = 0;ii<28;ii++){
			byte BLL = temp[ii].getLeft()[0][0];
			byte BLB = temp[ii].getBack()[0][2];
			byte BLU = temp[ii].getUp()[0][0];
			byte[] sizeBL = new byte[]{BLL,BLB,BLU};
			
			byte FLL = temp[ii].getLeft()[0][2];
			byte FLF = temp[ii].getFront()[0][0];
			byte FLU = temp[ii].getUp()[2][0];
			byte[] sizeFL = new byte[]{FLL,FLF,FLU};
			
			byte FRR = temp[ii].getRight()[0][0];
			byte FRF = temp[ii].getFront()[0][2];
			byte FRU = temp[ii].getUp()[2][2];
			byte[] sizeFR = new byte[]{FRR,FRF,FRU};
			
			byte BRR = temp[ii].getRight()[0][2];
			byte BRF = temp[ii].getBack()[0][0];
			byte BRU = temp[ii].getUp()[0][2];
			byte[] sizeBR = new byte[]{BRR,BRF,BRU};
			
			byte[] actBL = {temp[ii].getLeft()[1][1], temp[ii].getBack()[1][1],temp[ii].getUp()[1][1]};
			byte[] actFL = {temp[ii].getFront()[1][1], temp[ii].getLeft()[1][1],temp[ii].getUp()[1][1]};
			
			byte[] actFR = {temp[ii].getFront()[1][1], temp[ii].getRight()[1][1],temp[ii].getUp()[1][1]};
			byte[] actBR = {temp[ii].getBack()[1][1], temp[ii].getRight()[1][1],temp[ii].getUp()[1][1]};
			
			byte BL = 0;
			byte FL = 0;
			byte FR = 0;
			byte BR = 0;
			for(byte u=0;u<3;u++){
				for(byte r=0;r<3;r++){
					if(sizeBL[u] == actBL[r]){
						BL++;
					}
					if(sizeFL[u] == actFL[r]){
						FL++;
					}
					if(sizeFR[u] == actFR[r]){
						FR++;
					}
					if(sizeBR[u] == actBR[r]){
						BR++;
					}
				}
			}
			
			if(BL == 3&& FL == 3 && FR == 3 && BR == 3){
				if(best.length!=999){
					if(best.length>movelist[ii].length){
						best = new byte[movelist[ii].length];
						best = movelist[ii];
					}
				}else if(best.length==999){
					best = new byte[movelist[ii].length];
					best = movelist[ii];
				}
			}
		}
		return best;
		
	}
	public boolean ThirdLayerCornerPosition(){
		byte[]movestlcp = tlcpSearch(c);
		c.doMove(movestlcp);
		if(movestlcp.length==999){
			return false;
		}else{
			int len=movestlcp.length;
			numMoves +=len;
			byte[] temp = new byte[len];
			temp = transposeMoves(CubeConstants.CUBE_VERT_FLIP,movestlcp);
			updateMoves(temp);
			return true;
		}
		
	}
	
	private static byte[] blcoSearch(LogicalCube i){
		
		Cube[] temp = new Cube[73];
		for(byte ii=0;ii<73;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		byte[][]moves = new byte[73][];
		moves[0] = new byte[]{CubeConstants.RR,CubeConstants.UU,CubeConstants.R,CubeConstants.UU,CubeConstants.RR,CubeConstants.U2,CubeConstants.R,CubeConstants.U2};
		moves[1] = new byte[]{CubeConstants.U,CubeConstants.RR,CubeConstants.UU,CubeConstants.R,CubeConstants.UU,CubeConstants.RR,CubeConstants.U2,CubeConstants.R,CubeConstants.U2,CubeConstants.UU};
		moves[2] = new byte[]{CubeConstants.UU,CubeConstants.RR,CubeConstants.UU,CubeConstants.R,CubeConstants.UU,CubeConstants.RR,CubeConstants.U2,CubeConstants.R,CubeConstants.U2,CubeConstants.U};
		moves[3] = new byte[]{CubeConstants.U2,CubeConstants.RR,CubeConstants.UU,CubeConstants.R,CubeConstants.UU,CubeConstants.RR,CubeConstants.U2,CubeConstants.R,CubeConstants.U2,CubeConstants.U2};
		moves[4] = new byte[]{CubeConstants.R,CubeConstants.U,CubeConstants.RR,CubeConstants.U,CubeConstants.R,CubeConstants.U2,CubeConstants.RR,CubeConstants.U2};
		moves[5] = new byte[]{CubeConstants.U,CubeConstants.R,CubeConstants.U,CubeConstants.RR,CubeConstants.U,CubeConstants.R,CubeConstants.U2,CubeConstants.RR,CubeConstants.U2,CubeConstants.UU};
		moves[6] = new byte[]{CubeConstants.UU,CubeConstants.R,CubeConstants.U,CubeConstants.RR,CubeConstants.U,CubeConstants.R,CubeConstants.U2,CubeConstants.RR,CubeConstants.U2,CubeConstants.U};
		moves[7] = new byte[]{CubeConstants.U2,CubeConstants.R,CubeConstants.U,CubeConstants.RR,CubeConstants.U,CubeConstants.R,CubeConstants.U2,CubeConstants.RR,CubeConstants.U2,CubeConstants.U2};
		byte num = 8;
		for(byte ii=0;ii<8;ii++){
			for(byte jj=0;jj<8;jj++){
				moves[num] = addArray(moves[ii],moves[jj]);
				num++;
			}
		}
		moves[72]= new byte[]{};
		for(byte ii=0;ii<73;ii++){
			temp[ii].doMove(moves[ii]);
		}
		byte[] best = new byte[999];
		for(byte ii = 0;ii<73;ii++){
			byte a = temp[ii].getUp()[1][1];
			byte b = temp[ii].getUp()[0][0];
			byte cc = temp[ii].getUp()[0][1];
			byte d = temp[ii].getUp()[0][2];
			byte e = temp[ii].getUp()[1][0];
			byte f = temp[ii].getUp()[2][0];
			byte g = temp[ii].getUp()[2][1];
			byte h = temp[ii].getUp()[2][2];
			byte iii = temp[ii].getUp()[1][2];
			byte j = temp[ii].getFront()[0][0];
			byte k = temp[ii].getFront()[1][1];
			byte l = temp[ii].getFront()[0][2];
			byte m = temp[ii].getLeft()[0][0];
			byte n = temp[ii].getLeft()[1][1];
			byte o = temp[ii].getLeft()[0][2];
			byte p = temp[ii].getRight()[0][0];
			byte q = temp[ii].getRight()[1][1];
			byte r = temp[ii].getRight()[0][2];
			byte s = temp[ii].getBack()[0][0];
			byte t = temp[ii].getBack()[1][1];
			byte u = temp[ii].getBack()[0][2];
			
			
			
			if(a == b&&a==cc&&a==d&&a==e&a==f&&a==g&&a==h&&a==iii&&j==k&&l==k&&m==n&&o==n&&p==q&&r==q&&s==t&&u==t){
				if(best.length!=999){
					if(best.length>moves[ii].length){
						best = new byte[moves[ii].length];
						best = moves[ii];
					}
				}else if(best.length==999){
					best = new byte[moves[ii].length];
					best = moves[ii];
				}
			}
		}
		return best;
		
	}
	public boolean BottomLayerCornerOrientation(){
		byte[]movesblco = blcoSearch(c);
		c.doMove(movesblco);
		if(movesblco.length==999){
			return false;
		}else{
			int len=movesblco.length;
			numMoves+=len;
			byte[] temp = new byte[len];
			temp = transposeMoves(CubeConstants.CUBE_VERT_FLIP,movesblco);
			updateMoves(temp);
			return true;
		}
	}
	
	private static byte[] tlepSearch(LogicalCube i){
		Cube[] temp = new Cube[73];
		for(byte ii=0;ii<73;ii++){
			Cube t  = new Cube();
			t.setBack(i.getBack());
			t.setFront(i.getFront());
			t.setLeft(i.getLeft());
			t.setRight(i.getRight());
			t.setUp(i.getUp());
			t.setDown(i.getDown());
			temp[ii] = t;
		}
		
		byte[][]moves = new byte[73][];
		// clockwise
		moves[0] = new byte[]{CubeConstants.R2,CubeConstants.U,CubeConstants.F,CubeConstants.BB,CubeConstants.R2,CubeConstants.FF,CubeConstants.B,CubeConstants.U,CubeConstants.R2};
		moves[1] = new byte[]{CubeConstants.B2,CubeConstants.U,CubeConstants.R,CubeConstants.LL,CubeConstants.B2,CubeConstants.RR,CubeConstants.L,CubeConstants.U,CubeConstants.B2};
		moves[2] = new byte[]{CubeConstants.F2,CubeConstants.U,CubeConstants.L,CubeConstants.RR,CubeConstants.F2,CubeConstants.LL,CubeConstants.R,CubeConstants.U,CubeConstants.F2};
		moves[3] = new byte[]{CubeConstants.L2,CubeConstants.U,CubeConstants.B,CubeConstants.FF,CubeConstants.L2,CubeConstants.BB,CubeConstants.F,CubeConstants.U,CubeConstants.L2};
		// anticlockwise
		moves[4] = new byte[]{CubeConstants.R2,CubeConstants.UU,CubeConstants.F,CubeConstants.BB,CubeConstants.R2,CubeConstants.FF,CubeConstants.B,CubeConstants.UU,CubeConstants.R2};
		moves[5] = new byte[]{CubeConstants.B2,CubeConstants.UU,CubeConstants.R,CubeConstants.LL,CubeConstants.B2,CubeConstants.RR,CubeConstants.L,CubeConstants.UU,CubeConstants.B2};
		moves[6] = new byte[]{CubeConstants.F2,CubeConstants.UU,CubeConstants.L,CubeConstants.RR,CubeConstants.F2,CubeConstants.LL,CubeConstants.R,CubeConstants.UU,CubeConstants.F2};
		moves[7] = new byte[]{CubeConstants.L2,CubeConstants.UU,CubeConstants.B,CubeConstants.FF,CubeConstants.L2,CubeConstants.BB,CubeConstants.F,CubeConstants.UU,CubeConstants.L2};
		byte num = 8;
		for(byte ii=0;ii<8;ii++){
			for(byte jj=0;jj<8;jj++){
				moves[num] = addArray(moves[ii],moves[jj]);
				num++;
			}
		}
		moves[72]= new byte[]{};
		for(byte ii=0;ii<73;ii++){
			temp[ii].doMove(moves[ii]);
		}
		byte[] best = new byte[999];
		for(byte ii = 0;ii<73;ii++){
			byte a = temp[ii].getFront()[1][1];
			byte aa = temp[ii].getFront()[0][1];
			byte b = temp[ii].getLeft()[1][1];
			byte bb = temp[ii].getLeft()[0][1];
			byte cc = temp[ii].getRight()[1][1];
			byte ccc = temp[ii].getRight()[0][1];
			byte d = temp[ii].getBack()[1][1];
			byte dd = temp[ii].getBack()[0][1];
			if(a==aa&&b==bb&&cc==ccc&&d==dd){
				if(best.length!=999){
					if(best.length>moves[ii].length){
						best = new byte[moves[ii].length];
						best = moves[ii];
					}
				}else if(best.length==999){
					best = new byte[moves[ii].length];
					best = moves[ii];
				}
			}
		}
		return best;
		
	}
	public boolean ThirdLayerEdgePosition(){
		byte[]movestlep = tlepSearch(c);
		c.doMove(movestlep);
		if(movestlep.length==999){
			return false;
		}else{
			int len =movestlep.length;
			numMoves +=len;
			byte[] temp = new byte[len];
			temp = transposeMoves(CubeConstants.CUBE_VERT_FLIP,movestlep);
			updateMoves(temp);
			return true;
		}
	}

	/**
	 * Adds the ongoing moves to the moves field  
	 */
	public static void updateMoves() {
		updateMoves();
	}

	/**
	 * Adds the ongoing moves to the moves field
	 * @param update. The new moves to add
	 */
	public static void updateMoves(byte[] update){
		byte[] temp = new byte[algorithm.length];
		temp = addArray(algorithm,update);
		algorithm = new byte[temp.length];
		algorithm = temp;
	}
	
	/**
	 * The size of the moves list for the solution
	 * @return numMoves
	 */
	public int getNumMoves(){
		return numMoves;
	}
	/**
	 * The list of integers that represent moves
	 * (You will find the moves that they mean in CubeVar
	 * 
	 * @return algorithm to solve the input Cube
	 */
	public byte[]getMoves(){
		return algorithm;
	}
    /**
     * Utility method to add to arrays together
     * 
     * @param a the preceeding array
     * @param b the trailing array
     * @return the new array that has been added
     */
	private static byte[] addArray(byte[]a, byte[]b){
		byte[] out = new byte[a.length+b.length];
		for(int i =0;i<out.length;i++){
			if(i<a.length){
				out[i] = a[i];
			}else if(i>=a.length){
				out[i] = b[i-a.length];
			}
		}
		return out;
	}
	/**
	 * Utlilty method to moves an element from an array and update the 
	 * algorithm so that it does not include that element
	 * 
	 * @param index the position of the element to remove
	 */
	public static void remove(byte index){
		byte[] temp = algorithm;
		byte[] start = new byte[index];
		for(byte i=0;i<index;i++){
			start[i]= temp[i];
		}
		byte[] end = new byte[temp.length-(index+1)];
		for (byte i=0;i<end.length;i++){
			end[i]= temp[i+index];
		}
		algorithm = addArray(start,end);
	}
	public static byte[] getInverseMoves(byte[] input){
		int i=0;
		byte[]output = new byte[input.length];
		for(int j=input.length-1;j>=0;j--){
			if(input[j]>=19){
				output[i]=input[j];
				i++;
			}else{
				if(input[j]%2==0){
					output[i] = (byte) (input[j]-1);
					i++;
				}else{
					output[i] = (byte) (input[j]+1);
					i++;
				}
			}
		}
		return output;
	}
	
	public static byte[] transposeMoves(byte rotation, byte[] moves){
		if(moves.length==0){
			return moves;
		}
		byte[] outMoves = new byte[moves.length];
		for(byte i=0;i<moves.length;i++){
			byte t = moves[i];
			if(rotation==CubeConstants.CUBE_VERT_FLIP){
				if(t==CubeConstants.U){
					outMoves[i]=CubeConstants.D;
				}else if(t==CubeConstants.UU){
					outMoves[i]=CubeConstants.DD;
				}else if(t==CubeConstants.U2){
					outMoves[i]=CubeConstants.D2;
				}else if(t==CubeConstants.L){
					outMoves[i]=CubeConstants.R;
				}else if(t==CubeConstants.LL){
					outMoves[i]=CubeConstants.RR;
				}else if(t==CubeConstants.L2){
					outMoves[i]=CubeConstants.R2;
				}else if(t==CubeConstants.R){
					outMoves[i]=CubeConstants.L;
				}else if(t==CubeConstants.RR){
					outMoves[i]=CubeConstants.LL;
				}else if(t==CubeConstants.R2){
					outMoves[i]=CubeConstants.L2;
				}else if(t==CubeConstants.D){
					outMoves[i]=CubeConstants.U;
				}else if(t==CubeConstants.DD){
					outMoves[i]=CubeConstants.UU;
				}else if(t==CubeConstants.D2){
					outMoves[i]=CubeConstants.U2;
				}else{
					outMoves[i]=t;
				}
			}else if(rotation==CubeConstants.CUBE_LEFT){
				if(t==CubeConstants.F){
					outMoves[i]=CubeConstants.L;
				}else if(t==CubeConstants.FF){
					outMoves[i]=CubeConstants.LL;
				}else if(t==CubeConstants.F2){
					outMoves[i]=CubeConstants.L2;
				}else if(t==CubeConstants.L){
					outMoves[i]=CubeConstants.B;
				}else if(t==CubeConstants.LL){
					outMoves[i]=CubeConstants.BB;
				}else if(t==CubeConstants.L2){
					outMoves[i]=CubeConstants.B2;
				}else if(t==CubeConstants.B){
					outMoves[i]=CubeConstants.R;
				}else if(t==CubeConstants.BB){
					outMoves[i]=CubeConstants.RR;
				}else if(t==CubeConstants.B2){
					outMoves[i]=CubeConstants.R2;
				}else if(t==CubeConstants.R){
					outMoves[i]=CubeConstants.F;
				}else if(t==CubeConstants.RR){
					outMoves[i]=CubeConstants.FF;
				}else if(t==CubeConstants.R2){
					outMoves[i]=CubeConstants.F2;
				}else{
					outMoves[i]=t;
				}
				
			}else if(rotation==CubeConstants.CUBE_RIGHT){
				if(t==CubeConstants.F){
					outMoves[i]=CubeConstants.R;
				}else if(t==CubeConstants.FF){
					outMoves[i]=CubeConstants.RR;
				}else if(t==CubeConstants.F2){
					outMoves[i]=CubeConstants.R2;
				}else if(t==CubeConstants.L){
					outMoves[i]=CubeConstants.F;
				}else if(t==CubeConstants.LL){
					outMoves[i]=CubeConstants.FF;
				}else if(t==CubeConstants.L2){
					outMoves[i]=CubeConstants.F2;
				}else if(t==CubeConstants.B){
					outMoves[i]=CubeConstants.L;
				}else if(t==CubeConstants.BB){
					outMoves[i]=CubeConstants.LL;
				}else if(t==CubeConstants.B2){
					outMoves[i]=CubeConstants.L2;
				}else if(t==CubeConstants.R){
					outMoves[i]=CubeConstants.B;
				}else if(t==CubeConstants.RR){
					outMoves[i]=CubeConstants.BB;
				}else if(t==CubeConstants.R2){
					outMoves[i]=CubeConstants.B2;
				}else{
					outMoves[i]=t;
				}
				
			}else if(rotation==CubeConstants.CUBE_HORIZ_FLIP){
				if(t==CubeConstants.F){
					outMoves[i]=CubeConstants.B;
				}else if(t==CubeConstants.FF){
					outMoves[i]=CubeConstants.BB;
				}else if(t==CubeConstants.F2){
					outMoves[i]=CubeConstants.B2;
				}else if(t==CubeConstants.L){
					outMoves[i]=CubeConstants.R;
				}else if(t==CubeConstants.LL){
					outMoves[i]=CubeConstants.RR;
				}else if(t==CubeConstants.L2){
					outMoves[i]=CubeConstants.R2;
				}else if(t==CubeConstants.B){
					outMoves[i]=CubeConstants.F;
				}else if(t==CubeConstants.BB){
					outMoves[i]=CubeConstants.FF;
				}else if(t==CubeConstants.B2){
					outMoves[i]=CubeConstants.F2;
				}else if(t==CubeConstants.R){
					outMoves[i]=CubeConstants.L;
				}else if(t==CubeConstants.RR){
					outMoves[i]=CubeConstants.LL;
				}else if(t==CubeConstants.R2){
					outMoves[i]=CubeConstants.L2;
				}else{
					outMoves[i]=t;
				}
				
			}else{
				System.out.println("INVALID ROTATATION (MAYBE NOT IMPLEMENTED YET)");
				System.exit(0);
			}
		}
		return outMoves;
		
	}
	
	

}
