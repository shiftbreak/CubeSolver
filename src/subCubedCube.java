import java.util.ArrayList;
import java.util.Random;


public class subCubedCube implements LogicalCube, TwoPhaseLogicalCube{
	private subCube[] edges = new subCube[12];
	private subCube[] corners  = new subCube[8];
	private byte Uc;
	private byte Lc;
	private byte Rc;
	private byte Dc;
	private byte Fc;
	private byte Bc;
	private byte[] randommoves;
	
	/**
	 * Create a subCubedCube in the solved state
	 * Creates a cube in a solved state
	 */
	public subCubedCube(){
		for(int i=0;i<8;i++){
			corners[i] = new subCube((byte) (i+41));
		}
		for(int i=0;i<12;i++){
			edges[i] = new subCube((byte)(i+49));
		}
		setColours(CubeConstants.solvedCube);
	}
	public subCubedCube(subCubedCube cube){
		for(byte i=0;i<3;i++){
			System.arraycopy(cube.edges,0,this.edges,0,12);
			System.arraycopy(cube.corners,0,this.corners,0,8);
		}
	}
	public subCubedCube(byte[][] L,byte[][] R,byte[][] U,byte[][] D,byte[][] F,byte[][] B){
		setColours(new Cube(L,R,U,D,F,B));
	}
	public subCubedCube(Cube cube){
		for(int i=0;i<8;i++){
			corners[i] = new subCube((byte) (i+41));
		}
		for(int i=0;i<12;i++){
			edges[i] = new subCube((byte)(i+49));
		}
		setColours(CubeConstants.solvedCube);
		CubeSolver c = new CubeSolver(cube);
		c.solve();
		byte[] setup = CubeSolver.getInverseMoves(c.getMoves());
		doMove(setup);
	}
	
	
	public Cube getFaceCube(){
		return new Cube(getLeft(),getRight(),getUp(),getDown(),getFront(),getBack());
	}
	
	private void setColours(Cube c){
		byte[][] U = c.getUp();
		byte[][] D = c.getDown();
		byte[][] L =c.getLeft();
		byte[][] R = c.getRight();
		byte[][] F = c.getFront();
		byte[][] B = c.getBack();
		
		corners[0].posColour = U[2][2];
		corners[0].clockColour = R[0][0];
		corners[0].anticlockColour = F[0][2];
		corners[1].posColour = U[2][0];
		corners[1].clockColour = F[0][0];
		corners[1].anticlockColour = L[0][2];
		corners[2].posColour = U[0][0];
		corners[2].clockColour = L[0][0];
		corners[2].anticlockColour = B[0][2];
		corners[3].posColour = U[0][2];
		corners[3].clockColour = B[0][0];
		corners[3].anticlockColour = R[0][2];
		corners[4].posColour = D[2][0];
		corners[4].clockColour = F[2][2];
		corners[4].anticlockColour = R[2][0];
		corners[5].posColour = D[2][2];
		corners[5].clockColour = L[2][2];
		corners[5].anticlockColour = F[2][0];
		
		corners[6].posColour = D[0][2];
		corners[6].clockColour = B[2][2];
		corners[6].anticlockColour = L[2][0];
		corners[7].posColour = D[0][0];
		corners[7].clockColour = R[2][2];
		corners[7].anticlockColour = B[2][0];
		edges[0].posColour = U[1][2];
		edges[0].flipColour = R[0][1];
		edges[1].posColour = U[2][1];
		edges[1].flipColour = F[0][1];
		edges[2].posColour = U[1][0];
		edges[2].flipColour = L[0][1];
		edges[3].posColour = U[0][1];
		edges[3].flipColour = B[0][1];
		edges[4].posColour = D[1][0];
		edges[4].flipColour = R[2][1];
		edges[5].posColour = D[2][1];
		edges[5].flipColour = F[2][1];
		edges[6].posColour = D[1][2];
		edges[6].flipColour = L[2][1];
		edges[7].posColour = D[0][1];
		edges[7].flipColour = B[2][1];
		edges[8].posColour = F[1][2];
		edges[8].flipColour = R[1][0];
		edges[9].posColour = F[1][0];
		edges[9].flipColour = L[1][2];
		edges[10].posColour = B[1][2];
		edges[10].flipColour = L[1][0];
		edges[11].posColour = B[1][0];
		edges[11].flipColour = R[1][2];
		Uc = U[1][1];
		Lc = L[1][1];
		Rc = R[1][1];
		Dc = D[1][1];
		Fc = F[1][1];
		Bc = B[1][1];
	}
	
	
	
	public void randomise(Random r){
		Cube output =  new Cube(CubeConstants.solvedCube);
		byte[]movelist = new byte[100];
		for(byte i=0;i<100;i++){
			byte nextMove = (byte) (7+((byte)(r.nextDouble()*17)));
			movelist[i] = nextMove;
		}
		output.doMove(movelist);
		if(!output.isSolvable()){
			randomise(r);
			
		}
		randommoves = movelist;
		Cube c = new Cube(output.getLeft(),output.getRight(),output.getUp(),output.getDown(),output.getFront(),output.getBack());
		doMove(movelist);
		setColours(c);
	}
	
	
	public void doMove(byte[] movements) {
		for(int i=0;i<movements.length;i++){
			doMove(movements[i]);
		}
		
	}
	public void doMove(byte movement) {
		if(movement == CubeConstants.U){
			subCube temp = corners[3];
			corners[3] = corners[2];
			corners[2] = corners[1];
			corners[1] = corners[0];
			corners[0] = temp;
			
			subCube tempB = edges[3];
			edges[3] = edges[2];
			edges[2] = edges[1];
			edges[1] = edges[0];
			edges[0] = tempB;
					
		}
		else if(movement == CubeConstants.UU){
			subCube temp = corners[1];
			corners[1] = corners[2];
			corners[2] = corners[3];
			corners[3] = corners[0];
			corners[0] = temp;
			
			subCube tempB = edges[1];
			edges[1] = edges[2];
			edges[2] = edges[3];
			edges[3] = edges[0];
			edges[0] = tempB;
		}
		if(movement == CubeConstants.F){
			subCube temp = corners[1];
			corners[1] = corners[5];
			corners[5] = corners[4];
			corners[4] = corners[0];
			corners[0] = temp;
			
			subCube tempB = edges[9];
			edges[9] = edges[5];
			edges[5] = edges[8];
			edges[8] = edges[1];
			edges[1] = tempB;
			corners[0].orientation  = (byte) ((corners[0].orientation+1)%3);
			corners[1].orientation  = (byte) ((corners[1].orientation+2)%3);
			corners[4].orientation  = (byte) ((corners[4].orientation+2)%3);
			corners[5].orientation  = (byte) ((corners[5].orientation+1)%3);
			
			edges[1].orientation = (byte) ((edges[1].orientation+1)%2);
			edges[5].orientation = (byte) ((edges[5].orientation+1)%2);
			edges[8].orientation = (byte) ((edges[8].orientation+1)%2);
			edges[9].orientation = (byte) ((edges[9].orientation+1)%2);
			
		}else if(movement == CubeConstants.FF){
			subCube temp = corners[0];
			corners[0] = corners[4];
			corners[4] = corners[5];
			corners[5] = corners[1];
			corners[1] = temp;
			
			subCube tempB = edges[8];
			edges[8] = edges[5];
			edges[5] = edges[9];
			edges[9] = edges[1];
			edges[1] = tempB;
			
			corners[0].orientation  = (byte) ((corners[0].orientation+1)%3);
			corners[1].orientation  = (byte) ((corners[1].orientation+2)%3);
			corners[4].orientation  = (byte) ((corners[4].orientation+2)%3);
			corners[5].orientation  = (byte) ((corners[5].orientation+1)%3);
			
			edges[1].orientation = (byte) ((edges[1].orientation+1)%2);
			edges[5].orientation = (byte) ((edges[5].orientation+1)%2);
			edges[8].orientation = (byte) ((edges[8].orientation+1)%2);
			edges[9].orientation = (byte) ((edges[9].orientation+1)%2);
		}else if(movement == CubeConstants.R){
			subCube temp = corners[0];
			corners[0] = corners[4];
			corners[4] = corners[7];
			corners[7] = corners[3];
			corners[3] = temp;
			
			subCube tempB = edges[8];
			edges[8] = edges[4];
			edges[4] = edges[11];
			edges[11] = edges[0];
			edges[0] = tempB;
			
			corners[3].orientation  = (byte) ((corners[3].orientation+1)%3);
			corners[0].orientation  = (byte) ((corners[0].orientation+2)%3);
			corners[7].orientation  = (byte) ((corners[7].orientation+2)%3);
			corners[4].orientation  = (byte) ((corners[4].orientation+1)%3);
			
		}else if(movement == CubeConstants.RR){
			subCube temp = corners[3];
			corners[3] = corners[7];
			corners[7] = corners[4];
			corners[4] = corners[0];
			corners[0] = temp;
			
			subCube tempB = edges[11];
			edges[11] = edges[4];
			edges[4] = edges[8];
			edges[8] = edges[0];
			edges[0] = tempB;
			
			corners[3].orientation  = (byte) ((corners[3].orientation+1)%3);
			corners[0].orientation  = (byte) ((corners[0].orientation+2)%3);
			corners[7].orientation  = (byte) ((corners[7].orientation+2)%3);
			corners[4].orientation  = (byte) ((corners[4].orientation+1)%3);
			
		}else if(movement == CubeConstants.L){
			subCube temp = corners[2];
			corners[2] = corners[6];
			corners[6] = corners[5];
			corners[5] = corners[1];
			corners[1] = temp;
			
			subCube tempB = edges[10];
			edges[10] = edges[6];
			edges[6] = edges[9];
			edges[9] = edges[2];
			edges[2] = tempB;
			
			corners[1].orientation  = (byte) ((corners[1].orientation+1)%3);
			corners[2].orientation  = (byte) ((corners[2].orientation+2)%3);
			corners[5].orientation  = (byte) ((corners[5].orientation+2)%3);
			corners[6].orientation  = (byte) ((corners[6].orientation+1)%3);
			
		}else if(movement == CubeConstants.LL){
			subCube temp = corners[5];
			corners[5] = corners[6];
			corners[6] = corners[2];
			corners[2] = corners[1];
			corners[1] = temp;
			
			subCube tempB = edges[9];
			edges[9] = edges[6];
			edges[6] = edges[10];
			edges[10] = edges[2];
			edges[2] = tempB;
			
			corners[1].orientation  = (byte) ((corners[1].orientation+1)%3);
			corners[2].orientation  = (byte) ((corners[2].orientation+2)%3);
			corners[5].orientation  = (byte) ((corners[5].orientation+2)%3);
			corners[6].orientation  = (byte) ((corners[6].orientation+1)%3);
			
		}else if(movement == CubeConstants.B){
			subCube temp = corners[3];
			corners[3] = corners[7];
			corners[7] = corners[6];
			corners[6] = corners[2];
			corners[2] = temp;
			
			subCube tempB = edges[3];
			edges[3] = edges[11];
			edges[11] = edges[7];
			edges[7] = edges[10];
			edges[10] = tempB;
			
			corners[2].orientation  = (byte) ((corners[2].orientation+1)%3);
			corners[3].orientation  = (byte) ((corners[3].orientation+2)%3);
			corners[6].orientation  = (byte) ((corners[6].orientation+2)%3);
			corners[7].orientation  = (byte) ((corners[7].orientation+1)%3);
			
			edges[3].orientation = (byte) ((edges[3].orientation+1)%2);
			edges[7].orientation = (byte) ((edges[7].orientation+1)%2);
			edges[10].orientation = (byte) ((edges[10].orientation+1)%2);
			edges[11].orientation = (byte) ((edges[11].orientation+1)%2);
		}else if(movement == CubeConstants.BB){
			subCube temp = corners[6];
			corners[6] = corners[7];
			corners[7] = corners[3];
			corners[3] = corners[2];
			corners[2] = temp;
			
			subCube tempB = edges[10];
			edges[10] = edges[7];
			edges[7] = edges[11];
			edges[11] = edges[3];
			edges[3] = tempB;
			
			corners[2].orientation  = (byte) ((corners[2].orientation+1)%3);
			corners[3].orientation  = (byte) ((corners[3].orientation+2)%3);
			corners[6].orientation  = (byte) ((corners[6].orientation+2)%3);
			corners[7].orientation  = (byte) ((corners[7].orientation+1)%3);
			
			edges[3].orientation = (byte) ((edges[3].orientation+1)%2);
			edges[7].orientation = (byte) ((edges[7].orientation+1)%2);
			edges[10].orientation = (byte) ((edges[10].orientation+1)%2);
			edges[11].orientation = (byte) ((edges[11].orientation+1)%2);
		}else if(movement == CubeConstants.D){
			subCube temp = corners[5];
			corners[5] = corners[6];
			corners[6] = corners[7];
			corners[7] = corners[4];
			corners[4] = temp;
			
			subCube tempB = edges[6];
			edges[6] = edges[7];
			edges[7] = edges[4];
			edges[4] = edges[5];
			edges[5] = tempB;
			
		}else if(movement == CubeConstants.DD){
			subCube temp = corners[7];
			corners[7] = corners[6];
			corners[6] = corners[5];
			corners[5] = corners[4];
			corners[4] = temp;
			
			subCube tempB = edges[4];
			edges[4] = edges[7];
			edges[7] = edges[6];
			edges[6] = edges[5];
			edges[5] = tempB;
			
		}else if(movement == CubeConstants.L2){
			doMove(CubeConstants.L);
			doMove(CubeConstants.L);
		}else if(movement == CubeConstants.R2){
			doMove(CubeConstants.R);
			doMove(CubeConstants.R);
		}else if(movement == CubeConstants.U2){
			doMove(CubeConstants.U);
			doMove(CubeConstants.U);
		}else if(movement == CubeConstants.D2){
			doMove(CubeConstants.D);
			doMove(CubeConstants.D);
		}else if(movement == CubeConstants.F2){
			doMove(CubeConstants.F);
			doMove(CubeConstants.F);
		}else if(movement == CubeConstants.B2){
			doMove(CubeConstants.B);
			doMove(CubeConstants.B);
		}
	}
	
	public void doMoveP2(byte[] movements) {
		for(int i=0;i<movements.length;i++){
			doMoveP2(movements[i]);
		}
		
	}
	public void doMoveP2(byte movement) {
		if(movement == CubeConstants.P2U){
			doMove(CubeConstants.U);			
		}
		else if(movement == CubeConstants.P2UU){
			doMove(CubeConstants.UU);
		}
		
		else if(movement == CubeConstants.P2D){
			doMove(CubeConstants.D);
		}else if(movement == CubeConstants.P2DD){
			doMove(CubeConstants.DD);
		}else if(movement == CubeConstants.P2L2){
			doMove(CubeConstants.L);
			doMove(CubeConstants.L);
		}else if(movement == CubeConstants.P2R2){
			doMove(CubeConstants.R);
			doMove(CubeConstants.R);
		}else if(movement == CubeConstants.P2U2){
			doMove(CubeConstants.U);
			doMove(CubeConstants.U);
		}else if(movement == CubeConstants.P2D2){
			doMove(CubeConstants.D);
			doMove(CubeConstants.D);
		}else if(movement == CubeConstants.P2F2){
			doMove(CubeConstants.F);
			doMove(CubeConstants.F);
		}else if(movement == CubeConstants.P2B2){
			doMove(CubeConstants.B);
			doMove(CubeConstants.B);
		}
		
	}
	
	
	
	public void rotateCube(byte[] rotations) {
		for(int i=0;i<rotations.length;i++){
			rotateCube(rotations[i]);
		}
	}
	public void rotateCube(byte movement) {
		Cube c = new Cube();
		c.setBack(getBack());
		c.setFront(getFront());
		c.setLeft(getLeft());
		c.setRight(getRight());
		c.setUp(getUp());
		c.setDown(getDown());
		c.rotateCube(movement);
		setFront(c.getFront());
		setBack(c.getBack());
		setLeft(c.getLeft());
		setRight(c.getRight());
		setUp(c.getUp());
		setDown(c.getDown());
	}
	
	
	public boolean isSolved() {
		boolean isSolved = true;
		for(int i=0;i<corners.length;i++){
			if(corners[i].orientation != 0 && corners[i].cornerName() == i){
				isSolved = false;
			}
		}for(int i=0;i<edges.length;i++){
			if(edges[i].orientation != 0 && edges[i].edgeName() == i){
				isSolved = false;
			}
		}
		return isSolved;
	}
	
	public byte[] getSetupMoves() {
		return randommoves;
	}
	
	public void setEO(int j){
		int totEO = j;
		if(totEO==0){
			for(int i=0;i<edges.length;i++){
				edges[i].orientation = 0;
			}
		}else{
			edges[0].orientation = (byte) (totEO/(Math.pow(2,10)));
			totEO -= (int)(edges[0].orientation*Math.pow(2,10));
			edges[1].orientation = (byte) (totEO/(Math.pow(2,9)));
			totEO -= (int)(edges[1].orientation*Math.pow(2,9));
			edges[2].orientation = (byte) (totEO/(Math.pow(2,8)));
			totEO -= (int)(edges[2].orientation*Math.pow(2,8));
			edges[3].orientation = (byte) (totEO/(Math.pow(2,7)));
			totEO -= (int)(edges[3].orientation*Math.pow(2,7));
			edges[4].orientation = (byte) (totEO/(Math.pow(2,6)));
			totEO -= (int)(edges[4].orientation*Math.pow(2,6));
			edges[5].orientation = (byte) (totEO/(Math.pow(2,5)));
			totEO -= (int)(edges[5].orientation*Math.pow(2,5));
			edges[6].orientation = (byte) (totEO/(Math.pow(2,4)));
			totEO -= (int)(edges[6].orientation*Math.pow(2,4));
			edges[7].orientation = (byte) (totEO/(Math.pow(2,3)));
			totEO -= (int)(edges[7].orientation*Math.pow(2,3));
			edges[8].orientation = (byte) (totEO/(Math.pow(2,2)));
			totEO -= (int)(edges[8].orientation*Math.pow(2,2));
			edges[9].orientation = (byte) (totEO/2);
			totEO -= edges[9].orientation*2;
			edges[10].orientation = (byte) totEO;
			int sum = 0;
			for(int i=0;i<11;i++){
				sum +=edges[i].orientation;
			}
			edges[11].orientation = (byte) ((2-(sum%2))%2);
		}
		
	
	}
	public int getEO(){
		return (int) (edges[0].orientation*Math.pow(2,10)+edges[1].orientation*Math.pow(2,9)+
				edges[2].orientation*Math.pow(2,8)+edges[3].orientation*Math.pow(2,7)+
				edges[4].orientation*Math.pow(2,6)+edges[5].orientation*Math.pow(2,5) +
				edges[6].orientation*Math.pow(2,4)+ edges[7].orientation*Math.pow(2,3) + 
				edges[8].orientation*Math.pow(2,2)+edges[9].orientation*2+edges[10].orientation);
	
	}
	public void setCO(int j) {
		int totCO = j;
		if(totCO==0){
			for(int i=0;i<corners.length;i++){
				corners[i].orientation = 0;
			}
		}else{
			corners[0].orientation = (byte) (totCO/(Math.pow(3,6)));
			totCO -= (int)(corners[0].orientation*Math.pow(3,6));
			corners[1].orientation = (byte) (totCO/(Math.pow(3,5)));
			totCO -= (int)(corners[1].orientation*Math.pow(3,5));
			corners[2].orientation = (byte) (totCO/(Math.pow(3,4)));
			totCO -= (int)(corners[2].orientation*Math.pow(3,4));
			corners[3].orientation = (byte) (totCO/(Math.pow(3,3)));
			totCO -= (int)(corners[3].orientation*Math.pow(3,3));
			corners[4].orientation = (byte) (totCO/(Math.pow(3,2)));
			totCO -= (int)(corners[4].orientation*Math.pow(3,2));
			corners[5].orientation = (byte) (totCO/3);
			totCO -= corners[5].orientation*3;
			corners[6].orientation = (byte) totCO;
			int sum = 0;
			for(int i=0;i<7;i++){
				sum +=corners[i].orientation;
			}
			corners[7].orientation = (byte) ((3-(sum%3))%3);
		}
	}
	public int getCO(){
		int newCO =  (int) (corners[0].orientation*Math.pow(3,6)+corners[1].orientation*Math.pow(3,5)+
				corners[2].orientation*Math.pow(3,4)+corners[3].orientation*Math.pow(3,3)+
				corners[4].orientation*Math.pow(3,2)+corners[5].orientation*3+corners[6].orientation);
		return newCO;
	}
	public void setCP(int perm){
		int totCP = perm;
		int[] CPIN = new int[8];
		if(totCP !=0){
			CPIN[7] = totCP/factorial(7);
			totCP -= CPIN[7]*factorial(7);
			CPIN[6] = totCP/factorial(6);
			totCP -= CPIN[6]*factorial(6);
			CPIN[5] = totCP/factorial(5);
			totCP -= CPIN[5]*factorial(5);
			CPIN[4] = totCP/factorial(4);
			totCP -= CPIN[4]*factorial(4);
			CPIN[3] = totCP/factorial(3);
			totCP -= CPIN[3]*factorial(3);
			CPIN[2] = totCP/factorial(2);
			totCP -= CPIN[2]*factorial(2);
			CPIN[1] = totCP/factorial(1);
			totCP -= CPIN[1]*factorial(1);
			CPIN[0] = totCP;
			
			boolean [] taken = new boolean[8];
			for(int i=0;i<8;i++){
				taken[i] = false;
			}
			subCube[] temp = new subCube[8];
			for(int i=0;i<8;i++){
				temp[i] = copyByValue(corners[i]);
			}
			for(int i=7;i>=1;i--){
				ArrayList freePlace = new ArrayList();
				for(int h =7;h>=0;h--){
					if(!taken[h]){
						freePlace.add(h);
					}
				}
				Integer place = (Integer)freePlace.get(CPIN[i]);
				corners[i] = temp[place];
				taken[place] = true;
				
			}
			for(int i=0;i<8;i++){
				if(!taken[i]){
					corners[0] = temp[i];
				}
			}
		
		}
	}
	public int getCP(){
		int x=0;
		for(int i=7;i>0;i--){
			int s=0;
			for(int j=i-1;j>=0;j--){
				if(corners[j].cornerName()>corners[i].cornerName()){
					s++;
				}
			}
			x = (x+s)*i;
		}
		return x;
		
	}
	public void setEP(int perm){
		int totEP = perm;
		int[] EPIN = new int[7];
		if(totEP !=0){
			EPIN[0] = totEP/factorial(10);
			totEP -= EPIN[0]*factorial(10);
			EPIN[0] = totEP/factorial(9);
			totEP -= EPIN[0]*factorial(9);
			EPIN[0] = totEP/factorial(8);
			totEP -= EPIN[0]*factorial(8);
			EPIN[0] = totEP/factorial(7);
			totEP -= EPIN[0]*factorial(7);
			EPIN[0] = totEP/factorial(6);
			totEP -= EPIN[0]*factorial(6);
			EPIN[0] = totEP/factorial(5);
			totEP -= EPIN[0]*factorial(5);
			EPIN[0] = totEP/factorial(4);
			totEP -= EPIN[0]*factorial(4);
			EPIN[0] = totEP/factorial(3);
			totEP -= EPIN[0]*factorial(3);
			EPIN[0] = totEP/factorial(2);
			totEP -= EPIN[0]*factorial(2);
			EPIN[0] = totEP;
			
			int[]taken = new int[]{1,1,1,1,1,1,1,1,1,1,1,1};
			for(int j=0;j<EPIN.length;j++){
				int count = 0;
				int numZeros = 0;
				for(int i=0;i<=EPIN[j]+numZeros;i++){
					if(taken[i]==0){
						numZeros++;
					}
					count = i;
				}
				edges[11-count] = edges[j];
				taken[count] = 0;
			}
			for(int i=0;i<taken.length;i++){
				if(taken[i]==1){
					edges[i] = edges[0];
				}
			}
		}
	}
	public int getEP(){
		int[] EPOUT = new int[11];
		int[] taken = new int[]{1,1,1,1,1,1,1,1,1,1,1,1};
		for(int i=0;i<EPOUT.length;i++){
			int actEdge = edges[i].cornerName();
			int numToRight=0;
			for(int j=actEdge+1;j<EPOUT.length;j++){
				if(taken[j]==1){
					numToRight++;
				}
			}
			EPOUT[i] = numToRight;
			taken[11-numToRight] = 0;
		}
		
		int perm = factorial(11)*(EPOUT[0])+factorial(10)*(EPOUT[1])+factorial(9)*(EPOUT[2])+factorial(8)*(EPOUT[3])
					+factorial(7)*(EPOUT[4])+factorial(6)*(EPOUT[5])+factorial(5)*(EPOUT[6])+factorial(4)*(EPOUT[7])
					+factorial(3)*(EPOUT[8])+factorial(2)*(EPOUT[9])+factorial(1)*(EPOUT[10]);
		return perm;
		
	}
	public void setUD(int coord){
		int UDSlicePerm = coord;
//		 decode UDSlice Perm into subCubedCube UD slice edge positions
		for(int i=0;i<9;i++){
			for(int j=i+1;j<10;j++){
				for(int k=j+1;k<11;k++){
					for(int l=k+1;l<12;l++){
						subCubedCube temp = new subCubedCube();
						temp.swap(8,i);
						temp.swap(9,j);
						temp.swap(10,k);
						temp.swap(11,l);
						if(temp.getUD()==UDSlicePerm){
							swap(8,i);
							swap(9,j);
							swap(10,k);
							swap(11,l);
							i=9;j=10;k=11;l=12;
						}
					}
				}
			}
		}
	}
	public int getUD(){
		boolean[] occupied = new boolean[12];
		for(int i=0;i<12;i++){
			occupied[i] = false;
		}
		for(int i=0;i<12;i++){
			if(edges[i].edgeName()>=8){
				occupied[i] = true;
			}
		}
		int s=0;int k=3;int n=11;
		while(k>=0){
			if(occupied[n]){
				k--;
			}else{
				s += C(n,k);
			}
			n--;
		}
		return s;
	}
	public void setP2EP(int coord){
		int totEP = coord;
		int[] EPIN = new int[8];
		if(totEP !=0){
			EPIN[7] = totEP/factorial(7);
			totEP-= EPIN[7]*factorial(7);
			EPIN[6] = totEP/factorial(6);
			totEP -= EPIN[6]*factorial(6);
			EPIN[5] = totEP/factorial(5);
			totEP -= EPIN[5]*factorial(5);
			EPIN[4] = totEP/factorial(4);
			totEP -= EPIN[4]*factorial(4);
			EPIN[3] = totEP/factorial(3);
			totEP -= EPIN[3]*factorial(3);
			EPIN[2] = totEP/factorial(2);
			totEP -= EPIN[2]*factorial(2);
			EPIN[1] = totEP/factorial(1);
			totEP -= EPIN[1]*factorial(1);
			EPIN[0] = 0;
			
			
			boolean [] taken = new boolean[8];
			for(int i=0;i<8;i++){
				taken[i] = false;
			}
			subCube[] temp = new subCube[8];
			for(int i=0;i<8;i++){
				temp[i] = copyByValue(edges[i]);
			}
			for(int i=7;i>=1;i--){
				ArrayList freePlace = new ArrayList();
				for(int h =7;h>=0;h--){
					if(!taken[h]){
						freePlace.add(h);
					}
				}
				Integer place = (Integer)freePlace.get(EPIN[i]);
				edges[i] = temp[place];
				taken[place] = true;
				
			}
			for(int i=0;i<8;i++){
				if(!taken[i]){
					edges[0] = temp[i];
				}
			}
			
		}
	}
	public int getP2EP(){
		int x=0;
		for(int i=7;i>0;i--){
			int s=0;
			for(int j=i-1;j>=0;j--){
				if(edges[j].edgeName()>edges[i].edgeName()){
					s++;
				}
			}
			x = (x+s)*i;
		}
		
		return x;
	}
	public void setP2UD(int coord){
		//a*24 + b = UDSliceSortedCoordinate (0-11879) . a = UDSliceCoordinate (0-494), b = UDSlicePermutation.
		int UDSP = coord%24;
//		int UDSliceCoordinate = (coord-UDSP)/24;
		// may hava some problems here since FlipUD slice includes the edges orientations So this will
		// only work if all the edge orientations are correct (e.g. youre in phase 2) however as this coordinate
		// is only required for phase 2 then hopefully it should be ok.
		
		int totUD = UDSP;
		int[] UDIN = new int[4];
		if(totUD !=0){
			UDIN[3] = totUD/factorial(3);  // max 3
			totUD-= UDIN[3]*factorial(3);
			UDIN[2] = totUD/factorial(2); // max 2
			totUD -= UDIN[2]*factorial(2);
			UDIN[1] = totUD/factorial(1); // max 1
			totUD-= UDIN[1]*factorial(1);
			UDIN[0] = 0;
			
			
			boolean [] taken = new boolean[4];
			for(int i=0;i<4;i++){
				taken[i] = false;
			}
			subCube[] temp = new subCube[4];
			for(int i=8;i<12;i++){
				temp[i-8] = copyByValue(edges[i]);
			}
			for(int i=3;i>=1;i--){
				ArrayList freePlace = new ArrayList();
				for(int h =3;h>=0;h--){
					if(!taken[h]){
						freePlace.add(h);
					}
				}
				Integer place = (Integer)freePlace.get(UDIN[i]);
				edges[i+8] = temp[place];
				taken[place] = true;
				
			}
			for(int i=0;i<4;i++){
				if(!taken[i]){
					edges[8] = temp[i];
				}
			}
			
		}
	}
	public int getP2UD(){
		int e;
		int j=0;
		int[] arr = new int[4];
		for(int i=0;i<12;i++){
			e = edges[i].name;
			if(e == CubeConstants.FR||e == CubeConstants.FL||e == CubeConstants.BL||e == CubeConstants.BR){
				arr[j] = e;
				j++;
			}
		}
		int x=0;
		for(int p=3;p>=1;p--){
			int s = 0;
			for(int k=p-1;k>=0;k--){
				if(arr[k]>arr[p]){
					s++;
				}
			}
			x = (x+s)*p;
		}
		// remember in phase 2 the UD slice coord is going to be 0 since all the orientaation are 0 and
		// all the UD slice edges are in the UD slice. Thus this value always takes between 0 and 23.
		return getUD()*24+x;
	}
		
	public int getUDEO(){
		int rUD = getEO()*2048  + getUD();
		return rUD;
	}
	public int getReducedUDEO(int[] symUDtoRawUD){
//		 calculate all the symetries for this sub Cubed Cube
 		int[] n = new int[16];
 		subCubedCube c;
 		c = (subCubedCube) deepCopy();
 		n[0] = c.getUDEO();
 		int t = 1;
 		for(int i=25;i<30;i++){
 			c = (subCubedCube) deepCopy();
 			c.doMove((byte)i);
 			n[t] = c.getUDEO();
 			for(int j=i+1;j<30;j++){
 				t++;
 				c = (subCubedCube) deepCopy();
 				c.doMove(new byte[]{(byte) (i),(byte) (j)});
 				n[t] = c.getUDEO();
 				
 			}
 			t++;
 		}
 		int eqClass = 0;
 		int symmetry = 0;
 		for(int i=0;i<16;i++){
 			for(int j=0;j<symUDtoRawUD.length;j++){
 				if(n[i] == symUDtoRawUD[j]){
 					eqClass = j;
 					symmetry = i;
 				}
 			}
 		}
 		int symCoord = 16*eqClass + symmetry;
		return symCoord;
			
	}
	
	public int factorial(int number){
		if(number<=1){
			return 1;
		}else{
			return number*factorial(number-1);
		}
	}
	private void swap(int i, int j){
		subCube temp = copyByValue(edges[j]);
		
		edges[j] = edges[i];
		edges[i] = temp;
	
	}
	private subCube copyByValue(subCube s){
		byte name = s.name;
		byte posColour =s.posColour;
		byte flipColour = s.flipColour;
		byte orientation = s.orientation;
		byte clock = s.clockColour;
		byte anti = s.anticlockColour;
		subCube out = new subCube(name, posColour, flipColour, clock, anti, orientation);
		return out;
	}
	private int C(int n, int k){
		int BC = factorial(n)/(factorial(k)*factorial(n-k));
		return BC;
	}
	
	
	public byte[][] getLeft() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[2].getCornerColour((byte) 1);
		face[0][2] = corners[1].getCornerColour((byte) 2);
		face[2][0] = corners[6].getCornerColour((byte) 2);
		face[2][2] = corners[5].getCornerColour((byte) 1);
		face[0][1] = edges[2].getEdgeColour((byte) 1);
		face[1][0] = edges[10].getEdgeColour((byte) 1);
		face[1][2] = edges[9].getEdgeColour((byte) 1);
		face[2][1] = edges[6].getEdgeColour((byte) 1);
		face[1][1] = Lc;
		return face;
	}
	public byte[][] getFront() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[1].getCornerColour((byte) 1);
		face[0][2] = corners[0].getCornerColour((byte) 2);
		face[2][0] = corners[5].getCornerColour((byte) 2);
		face[2][2] = corners[4].getCornerColour((byte) 1);
		face[0][1] = edges[1].getEdgeColour((byte) 1);
		face[1][0] = edges[9].getEdgeColour((byte) 0);
		face[1][2] = edges[8].getEdgeColour((byte) 0);
		face[2][1] = edges[5].getEdgeColour((byte) 1);
		face[1][1] = Fc;
		return face;
	}
	public byte[][] getUp() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[2].getCornerColour((byte) 0);
		face[0][2] = corners[3].getCornerColour((byte) 0);
		face[2][0] = corners[1].getCornerColour((byte) 0);
		face[2][2] = corners[0].getCornerColour((byte) 0);
		face[0][1] = edges[3].getEdgeColour((byte) 0);
		face[1][0] = edges[2].getEdgeColour((byte) 0);
		face[1][2] = edges[0].getEdgeColour((byte) 0);
		face[2][1] = edges[1].getEdgeColour((byte) 0);
		face[1][1] = Uc;
		return face;
	}
	public byte[][] getDown() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[7].getCornerColour((byte) 0);
		face[0][2] = corners[6].getCornerColour((byte) 0);
		face[2][0] = corners[4].getCornerColour((byte) 0);
		face[2][2] = corners[5].getCornerColour((byte) 0);
		face[0][1] = edges[7].getEdgeColour((byte) 0);
		face[1][0] = edges[4].getEdgeColour((byte) 0);
		face[1][2] = edges[6].getEdgeColour((byte) 0);
		face[2][1] = edges[5].getEdgeColour((byte) 0);
		face[1][1] = Dc;
		return face;
	}
	public byte[][] getRight() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[0].getCornerColour((byte) 1);
		face[0][2] = corners[3].getCornerColour((byte) 2);
		face[2][0] = corners[4].getCornerColour((byte) 2);
		face[2][2] = corners[7].getCornerColour((byte) 1);
		face[0][1] = edges[0].getEdgeColour((byte) 1);
		face[1][0] = edges[8].getEdgeColour((byte) 1);
		face[1][2] = edges[11].getEdgeColour((byte) 1);
		face[2][1] = edges[4].getEdgeColour((byte) 1);
		face[1][1] = Rc;
		return face;
	}
	public byte[][] getBack() {
		byte[][]face = new byte[3][3];
		face[0][0] = corners[3].getCornerColour((byte) 1);
		face[0][2] = corners[2].getCornerColour((byte) 2);
		face[2][0] = corners[7].getCornerColour((byte) 2);
		face[2][2] = corners[6].getCornerColour((byte) 1);
		face[0][1] = edges[3].getEdgeColour((byte) 1);
		face[1][0] = edges[11].getEdgeColour((byte) 0);
		face[1][2] = edges[10].getEdgeColour((byte) 0);
		face[2][1] = edges[7].getEdgeColour((byte) 1);
		face[1][1] = Bc;
		return face;
	}
	public void setLeft(byte[][] values) {
		corners[2].setCornerColour((byte) 1, values[0][0]);
		corners[1].setCornerColour((byte) 2, values[0][2]);
		corners[6].setCornerColour((byte) 2, values[2][0]);
		corners[5].setCornerColour((byte) 1, values[2][2]);
		edges[2].setEdgeColour((byte) 1, values[0][1]);
		edges[10].setEdgeColour((byte) 1,values[1][0]);
		edges[9].setEdgeColour((byte) 1, values[1][2]);
		edges[6].setEdgeColour((byte) 1, values[2][1]);
		Lc = values[1][1];
	}
	public void setRight(byte[][] values) {
		corners[0].setCornerColour((byte) 1, values[0][0]);
		corners[3].setCornerColour((byte) 2, values[0][2]);
		corners[4].setCornerColour((byte) 2, values[2][0]);
		corners[7].setCornerColour((byte) 1, values[2][2]);
		edges[0].setEdgeColour((byte) 1, values[0][1]);
		edges[8].setEdgeColour((byte) 1, values[1][0]);
		edges[11].setEdgeColour((byte) 1, values[1][2]);
		edges[4].setEdgeColour((byte) 1, values[2][1]);
		Rc = values[1][1];
	}
	public void setUp(byte[][] values) {
		corners[2].setCornerColour((byte) 0, values[0][0]);
		corners[3].setCornerColour((byte) 0, values[0][2]);
		corners[1].setCornerColour((byte) 0, values[2][0]);
		corners[0].setCornerColour((byte) 0, values[2][2]);
		edges[3].setEdgeColour((byte) 0, values[0][1]);
		edges[2].setEdgeColour((byte) 0, values[1][0]);
		edges[0].setEdgeColour((byte) 0,values[1][2]);
		edges[1].setEdgeColour((byte) 0,values[2][1]);
		Uc = values[1][1];
	}
	public void setDown(byte[][] values) {
		corners[7].setCornerColour((byte) 0, values[0][0]);
		corners[6].setCornerColour((byte) 0, values[0][2]);
		corners[4].setCornerColour((byte) 0, values[2][0]);
		corners[5].setCornerColour((byte) 0, values[2][2]);
		edges[7].setEdgeColour((byte) 0, values[0][1]);
		edges[4].setEdgeColour((byte) 0, values[1][0]);
		edges[6].setEdgeColour((byte) 0, values[1][2]);
		edges[5].setEdgeColour((byte) 0, values[2][1]);
		Dc = values[1][1];
	}
	public void setFront(byte[][] values) {
		corners[1].setCornerColour((byte) 1, values[0][0]);
		corners[0].setCornerColour((byte) 2, values[0][2]);
		corners[5].setCornerColour((byte) 2, values[2][0]);
		corners[4].setCornerColour((byte) 1, values[2][2]);
		edges[1].setEdgeColour((byte) 1, values[0][1]);
		edges[9].setEdgeColour((byte) 0, values[1][0]);
		edges[8].setEdgeColour((byte) 0, values[1][2]);
		edges[5].setEdgeColour((byte) 1, values[2][1]);
		Fc = values[1][1];
	}
	public void setBack(byte[][] values) {
		corners[3].setCornerColour((byte) 1,values[0][0]);
		corners[2].setCornerColour((byte) 2,values [0][2]);
		corners[7].setCornerColour((byte) 2, values[2][0]);
		corners[6].setCornerColour((byte) 1, values[2][2]);
		edges[3].setEdgeColour((byte) 1, values[0][1]);
		edges[11].setEdgeColour((byte) 0, values[1][0]);
		edges[10].setEdgeColour((byte) 0, values[1][2]);
		edges[7].setEdgeColour((byte) 1, values[2][1]);
		Bc = values[1][1];
	}
	
	public static void printFace(byte[][] face){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				System.out.print(face[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	public void printCube(){
		System.out.println("UP:");
		printFace(getUp());
		System.out.println("LEFT:");
		printFace(getLeft());
		System.out.println("FRONT:");
		printFace(getFront());
		System.out.println("RIGHT:");
		printFace(getRight());
		System.out.println("BACK:");
		printFace(getBack());
		System.out.println("DOWN:");
		printFace(getDown());
	}
	public void printCubeB(){
		System.out.println("  EO: " + getEO());
		System.out.println("  CO: " + getCO());
		System.out.println("  UD: " + getUD());
		System.out.println("  CP: " + getCP());
		System.out.println("P2UD: " + getP2UD());
		System.out.println("P2EP: " + getP2EP());
	}
	
	
	public boolean solvedCross(){
		if(edges[0].edgeName() ==0&&edges[1].edgeName() == 1&&edges[2].edgeName()==2&&edges[3].edgeName()==3){
			if(edges[0].orientation==0&&edges[1].orientation==0&&edges[2].orientation==0&&edges[3].orientation==0){
				return true;
			}
		}
		return false;
		
	}
	
	public void doMoveP1(byte[] movements) {
		doMove(movements);
		
	}
	public void doMoveP1(byte movement) {
		doMove(movement);
	}

	class subCube{
		public byte orientation;
		public byte name;
		public byte clockColour;
		public byte anticlockColour;
		public byte posColour;
		public byte flipColour;
			
		public subCube(byte name){
			this.name = name;
			this.orientation = 0;
		}
		public subCube(byte name, byte posColour, byte flipColour){
			this.orientation = 0;
			this.name = name;
			this.posColour = posColour;
			this.flipColour = flipColour;
			
		}
		public void setOrientation(byte orientation){
			this.orientation = orientation;
		}
		
		public subCube(byte name, byte posColour, byte clockColour, byte anticlockColour){
			this.orientation = 0;
			this.name = name;
			this.posColour = posColour;
			this.clockColour = clockColour;
			this.anticlockColour = anticlockColour;
		}
		
		public subCube(byte name, byte posColour, byte flipColour,  byte clockColour, byte anticlockColour, byte orientation){
			this.orientation = orientation;
			this.name = name;
			this.posColour = posColour;
			this.clockColour = clockColour;
			this.anticlockColour = anticlockColour;
			this.flipColour = flipColour;
		}
		public byte getCornerColour(byte or){
			if(orientation==or){
				return posColour;
			}else if(orientation == or-1){
				return clockColour;
			}else if(orientation == or+2){
				return clockColour;
			}else{
				return anticlockColour;
			}
		}
		public void setCornerColour(byte or, byte colour){
			if(orientation==or){
				posColour = colour;
			}else if(orientation == or-1){
				clockColour = colour;
			}else if(orientation == or+2){
				clockColour = colour;
			}else{
				anticlockColour = colour;
			}
		}
		public byte edgeName(){
			return (byte) (name-49);
		}
		public byte cornerName(){
			return (byte) (name-41);
		}
		
		public byte getEdgeColour(byte or){
			if(orientation == or){
				return posColour;
			}else{
				return flipColour;
			}
		}
		public void setEdgeColour(byte or, byte colour){
			if(orientation == or){
				posColour = colour;
			}else{
				flipColour = colour;
			}
		}
		
		
	}
	public static void main(String[] argv){
			subCubedCube c = new subCubedCube();
			c.doMove(CubeConstants.UU);
			c.doMove(CubeConstants.F2);
			c.printCubeB();
//			System.out.println("");
//			subCubedCube d = (subCubedCube) c.deepCopy();
			
			//d.printCubeB();
	}
	public LogicalCube deepCopy() {
		subCubedCube out = new subCubedCube();
		
		out.setCO(getCO());
		out.setEO(getEO());
		out.setUD(getUD());
		out.setP2EP(getP2EP());
		out.setP2UD(getP2UD());
		out.setCP(getCP());
	
		return out;
	
	}
	public boolean tryMoveP1(byte movement) {
		subCubedCube c = (subCubedCube)this.deepCopy();
		c.doMoveP1(movement);
		if(c.isSolved()){return true;}
		else{return false;}
	}
	public boolean tryMoveP2(byte movement) {
		subCubedCube c = (subCubedCube)this.deepCopy();
		c.doMoveP2(movement);
		if(c.isSolved()){return true;}
		else{return false;}
	}
	
	private void replaceColour(subCubedCube canvas, byte in, byte rep){
		byte[][]f = getFront();
		byte[][]b = getBack();
		byte[][]l = getLeft();
		byte[][]r = getRight();
		byte[][]u = getUp();
		byte[][]d = getDown();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(f[i][j]== in){
					f[i][j] = rep;
				}
				if(b[i][j]== in){
					b[i][j] = rep;
				}
				if(l[i][j]== in){
					l[i][j] = rep;
				}
				if(r[i][j]== in){
					r[i][j] = rep;
				}
				if(u[i][j]== in){
					u[i][j] = rep;
				}
				if(d[i][j]== in){
					d[i][j] = rep;
				}
			}
		}
		canvas.setFront(f);
		canvas.setBack(b);
		canvas.setLeft(l);
		canvas.setRight(r);
		canvas.setUp(u);
		canvas.setDown(d);
	}
	private void setSides(subCubedCube c){
		setFront(c.getFront());
		setBack(c.getBack());
		setLeft(c.getLeft());
		setRight(c.getRight());
		setUp(c.getUp());
		setDown(c.getDown());
	}
	public void reflectLR(subCubedCube canvas){
		byte[][]f = getFront();
		byte[][]b = getBack();
		byte[][]l = getLeft();
		byte[][]r = getRight();
		byte[][]u = getUp();
		byte[][]d = getDown();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				l[i][j] = getRight()[i][2-i];
				r[i][j] = getLeft()[i][2-j];
				
			}
			u[i][0] = getUp()[i][2];
			u[i][2] = getUp()[i][0];
			f[i][0] = getFront()[i][2];
			f[i][2] = getFront()[i][0];
			d[i][0] = getDown()[i][2];
			d[i][2] = getDown()[i][0];
			b[i][0] = getBack()[i][2];
			b[i][2] = getBack()[i][0];
		}
		canvas.setFront(f);
		canvas.setBack(b);
		canvas.setLeft(l);
		canvas.setRight(r);
		canvas.setUp(u);
		canvas.setDown(d);
		
	}
	public boolean isSolvedP1() {
		if(getUD()==0&&getCO()==0&&getEO()==0){
			return true;
		}
		return false;
	}
	public boolean isSolvedP2() {
		if(getCP()==0&&getP2EP()==0&&getP2UD()==0){
			return true;
		}
		return false;
	}
	
	public boolean tryMatchP1(byte movement) {
		// TODO Auto-generated method stub
		return false;
	}
	public void setP2CoOrdinates(byte[] moves, subCubedCube subCubed) {
		// TODO Auto-generated method stub
		
	}
	public int[] doMoveP2(byte movement, int[] coords) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean tryMoveP2(byte movement, int[] coords) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean tryMatchP2(byte i) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean tryMatchP2() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean tryMatchP2(byte i, int[] coords) {
		// TODO Auto-generated method stub
		return false;
	}
	public void loadP1PrunTable() {
		// TODO Auto-generated method stub
		
	}
	public void loadP2PrunTable() {
		// TODO Auto-generated method stub
		
	}
	public void generatePhase1Prun() {
		// TODO Auto-generated method stub
		
	}
	public void generatePhase2Prun() {
		// TODO Auto-generated method stub
		
	}
	public void dumpP1PruningTable() {
		// TODO Auto-generated method stub
		
	}
	public void dumpP2PruningTable() {
		// TODO Auto-generated method stub
		
	}
	public void loadMovTables() {
		// TODO Auto-generated method stub
		
	}

	
}
