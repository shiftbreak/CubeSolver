import java.util.Random;

public class Cube implements LogicalCube{
 /*
  * This class is supposed to represent the cube.
  * The class stores the state of the cube and contains
  * methods to allow the cube to be manipulated whilst
  * still remaining in a feasible state. 
  * 
  * according to convention  l r u d f b are used to represent
  * each side.
  *  
  * Regarding the orientation of each side matrix:
  *     
  *                     Top
  *                   00 01 02
  *                   10 11 12  
  *                   20 21 22
  *           Left     Front     Right      Back
  *         00 01 02  00 01 02  00 01 02  00 01 02
  *         10 11 12  10 11 12  10 11 12  10 11 12
  *         20 21 22  20 21 22  20 21 22  20 21 22
  *                    Bottom
  *                   22 21 20
  *                   12 11 10
  *                   02 01 00
  *                   
  *    E.g. the top layer of all the sides      = 0x etc
  *    the back layer of the top and the bottom = 0x etc 
  *    
  *    There are four different available whole cube moves ( a combination of these can be used
  *    to position the cube byteo any required position
  *    
  *    There will be two operations avaialble for each face (clockwise and anticlockwise)
  *    Therefore 12 different manipulations are required:
  *    
  *    These are specified as follows:
  *    l, ll, l2   == clockwise / anticlockwise rotation of L 
  *    r, rr, rr   == clockwise / anticlockwise rotation of R
  *    u, uu, u2   == clockwise / anticlockwise rotation of U
  *    d, dd, d2   == clockwise / anticlockwise rotation of D
  *    f, ff, f2   == clockwise / anticlockwise rotation of F
  *    b, bb, b2   == clockwise / anticlockwise rotation of B
  */
	private byte[][] L = new byte[3][3];
	private byte[][] R = new byte[3][3];
	private byte[][] U = new byte[3][3];
	private byte[][] D = new byte[3][3];
	private byte[][] F = new byte[3][3];
	private byte[][] B = new byte[3][3];
	
	private static byte[] randommoves;
	
	public Cube(){
		new Cube(CubeConstants.solvedCube);
	}

	public static Cube getRandomCube(Random r){
		Cube c = new Cube();
		c.randomise(r);
		return c;

	}

	
	public Cube(Cube cube){
		for(byte i=0;i<3;i++){
			System.arraycopy(cube.getBack()[i],0,B[i],0,3);
			System.arraycopy(cube.getFront()[i],0,F[i],0,3);
			System.arraycopy(cube.getLeft()[i],0,L[i],0,3);
			System.arraycopy(cube.getRight()[i],0,R[i],0,3);
			System.arraycopy(cube.getUp()[i],0,U[i],0,3);
			System.arraycopy(cube.getDown()[i],0,D[i],0,3);
		}
	}
	
	
	public Cube(byte[][] L,byte[][] R,byte[][] U,byte[][] D,byte[][] F,byte[][] B){
		this.L = L;
		this.R = R;
		this.U = U;
		this.D = D;
		this.F = F;
		this.B = B;
	}
	
	public boolean isSolvable(){
		byte CountRed=0;
		byte CountBlue=0;
		byte CountYellow=0;
		byte CountWhite=0;
		byte CountOrange=0;
		byte CountGreen=0;
		boolean solvable=true;
		
		for(byte a=0;a<3;a++){
			for(byte bb=0;bb<3;bb++){
				if(F[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(F[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(F[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(F[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(F[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(F[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
				if(B[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(B[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(B[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(B[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(B[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(B[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
				if(L[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(L[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(L[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(L[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(L[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(L[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
				if(R[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(R[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(R[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(R[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(R[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(R[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
				if(U[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(U[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(U[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(U[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(U[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(U[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
				if(D[a][bb]==CubeConstants.YELLOW){
					CountYellow++;
				}
				if(D[a][bb]==CubeConstants.BLUE){
					CountBlue++;
				}
				if(D[a][bb]==CubeConstants.RED){
					CountRed++;
				}
				if(D[a][bb]==CubeConstants.WHITE){
					CountWhite++;
				}
				if(D[a][bb]==CubeConstants.GREEN){
					CountGreen++;
				}
				if(D[a][bb]==CubeConstants.ORANGE){
					CountOrange++;
				}
			}
		}
		
		if(CountYellow>9){
			System.out.println("INCORRECT NUMBER OF YELLOW: " + CountYellow);
			solvable = false;
		}
		if(CountBlue>9){
			System.out.println("INCORRECT NUMBER OF BLUE: " + CountBlue);
			solvable = false;
		}
		if(CountRed>9){
			System.out.println("INCORRECT NUMBER OF RED: " + CountRed);
			solvable = false;
		}
        if(CountGreen>9){
        	System.out.println("INCORRECT NUMBER OF GREEN: " + CountGreen);
			solvable = false;
		}
        if(CountOrange>9){
        	System.out.println("INCORRECT NUMBER OF ORANGE: " + CountOrange);
			solvable = false;
		}
        if(CountWhite>9){
        	System.out.println("INCORRECT NUMBER OF WHITE: " + CountWhite);
			solvable=false;
		}
		return solvable;
	}
	
	public byte[] getSetupMoves(){
		return randommoves;
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
		L = output.L;
		R = output.R;
		F = output.F;
		B = output.B;
		U = output.U;
		D = output.D;
		
		
	
	}
	
	public boolean isSolved(){
		boolean solvedF = true;
		byte centreF = F[1][1];
		boolean solvedB = true;
		byte centreB = B[1][1];
		boolean solvedU = true;
		byte centreU = U[1][1];
		boolean solvedD = true;
		byte centreD = D[1][1];
		boolean solvedL = true;
		byte centreL = L[1][1];
		boolean solvedR = true;
		byte centreR = R[1][1];
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				if(F[i][j] != centreF){
					solvedF = false;
				}
				if(B[i][j] != centreB){
					solvedB = false;
				}
				if(U[i][j] != centreU){
					solvedU = false;
				}
				if(D[i][j] != centreD){
					solvedD = false;
				}
				if(L[i][j] != centreL){
					solvedL = false;
				}
				if(R[i][j] != centreR){
					solvedR = false;
				}
				
			}
		}
		if(solvedU&&solvedD&&solvedF&&solvedB&&solvedL&&solvedR){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public void doMove(byte[] movements){
		if(movements.length!=0){
			for(int i=0;i < movements.length; i++){
				doMove(movements[i]);
			}
		}
	}
	public void doMove(byte movement){
		if(movement == CubeConstants.L){
			L = rotClock(L);
			byte[]fValL = new byte[]{F[2][0],F[1][0],F[0][0]};
			byte[]bValL = new byte[]{B[2][2],B[1][2],B[0][2]};
			for(byte i=0;i<3;i++){
				F[i][0] = U[i][0];
				U[i][0] = bValL[i];
				B[i][2] = D[i][2];
				D[i][2] = fValL[i];
				
			}
		}
		if(movement == CubeConstants.LL){
			L = rotAntiClock(L);
			byte[]dValL = new byte[]{D[2][2],D[1][2],D[0][2]};
			byte[]uValL = new byte[]{U[2][0],U[1][0],U[0][0]};
			for(byte i=0;i<3;i++){
				U[i][0] = F[i][0];
				F[i][0] = dValL[i];
				D[i][2] = B[i][2];
				B[i][2] = uValL[i];
			}
		}
		if(movement == CubeConstants.F){
			F = rotClock(F);
			byte[] lValF = new byte[]{L[2][2],L[1][2],L[0][2]};
			byte[] dValF = new byte[]{D[2][2],D[2][1],D[2][0]};
			for(byte i=0;i<3;i++){
				D[2][i] = R[i][0];
				R[i][0] = U[2][i];
				U[2][i] = lValF[i];
				L[i][2] = dValF[i];
				
				
			}
			
			
			
		}
		if(movement == CubeConstants.FF){
			F = rotAntiClock(F);
			byte[] uValF = new byte[] {U[2][2],U[2][1],U[2][0]};
			byte[] lValF = new byte[] {L[2][2],L[1][2],L[0][2]};
			for (byte i=0;i<3;i++){
				U[2][i] = R[i][0];
				R[i][0] = D[2][i];
				L[i][2] = uValF[i];
				D[2][i] = lValF[i];
			}
		}
		
		if(movement == CubeConstants.R){
			R = rotClock(R);
			byte[]dValR = new byte[]{D[2][0],D[1][0],D[0][0]};
			byte[]uValR = new byte[]{U[2][2],U[1][2],U[0][2]};
			
			for(byte i=0;i<3;i++){
				D[i][0] = B[i][0];
				B[i][0] = uValR[i];
				U[i][2] = F[i][2];
				F[i][2] = dValR[i];
			}
		}
		if(movement == CubeConstants.RR){
			R = rotAntiClock(R);
			byte[]fValR = new byte[]{F[2][2],F[1][2],F[0][2]};
			byte[]bValR = new byte[]{B[2][0],B[1][0],B[0][0]};
			for(byte i=0;i<3;i++){
				F[i][2] = U[i][2];
				U[i][2] = bValR[i];
				B[i][0] = D[i][0];
				D[i][0] = fValR[i];
			}
		}
		if(movement == CubeConstants.U){
			U = rotClock(U);
			for(byte i=0;i<3;i++){
				byte a = L[0][i];
				L[0][i] = F[0][i];
				F[0][i] = R[0][i];
				R[0][i] = B[0][i];
				B[0][i] = a;
			}
		}
		if(movement == CubeConstants.UU){
			U = rotAntiClock(U);
			for(byte i=0;i<3;i++){
				byte a = F[0][i];
				F[0][i] = L[0][i];
				L[0][i] = B[0][i];
				B[0][i] = R[0][i];
				R[0][i] = a;
			}
		}
		if(movement == CubeConstants.DD){
			D = rotAntiClock(D);
			for(byte i=0;i<3;i++){
				byte a = L[2][i];
				L[2][i] = F[2][i];
				F[2][i] = R[2][i];
				R[2][i] = B[2][i];
				B[2][i] = a;
			}
		}
		if(movement == CubeConstants.D){
			D = rotClock(D);
			for(byte i=0;i<3;i++){
				byte a = F[2][i];
				F[2][i] = L[2][i];
				L[2][i] = B[2][i];
				B[2][i] = R[2][i];
				R[2][i] = a;
			}
		}
		
		if(movement == CubeConstants.B){
			B = rotClock(B);
			byte[] uValB = new byte[]{U[0][2],U[0][1],U[0][0]};
			byte[] lValB = new byte[]{L[2][0],L[1][0],L[0][0]};
			for(byte i=0;i<3;i++){
				
				U[0][i] = R[i][2];
				R[i][2] = D[0][i];
				D[0][i] = lValB[i];
				L[i][0] = uValB[i];
			}
		}
		if(movement == CubeConstants.BB){
			B = rotAntiClock(B);
			byte[] dValB = new byte[]{D[0][2],D[0][1],D[0][0]};
			byte[] lValB = new byte[]{L[2][0],L[1][0],L[0][0]};
			for(byte i=0;i<3;i++){
				D[0][i] = R[i][2];
				R[i][2] = U[0][i];
				U[0][i] = lValB[i];
				L[i][0] = dValB[i];
			}
		}
		if(movement == CubeConstants.L2){
			L = rotClock(L);
			L = rotClock(L);
			byte[] dvalL = new byte[]{D[2][2],D[1][2],D[0][2]};
			byte[] uvalL = new byte[]{U[2][0],U[1][0],U[0][0]};
			byte[] bValL = new byte[]{B[2][2],B[1][2],B[0][2]};
			byte[] fValL = new byte[]{F[2][0],F[1][0],F[0][0]};
			for(byte i=0;i<3;i++){
				U[i][0] = dvalL[i];
				D[i][2] = uvalL[i];
				F[i][0] = bValL[i];
				B[i][2]=  fValL[i];
			}
		}
		if(movement == CubeConstants.R2){
			R = rotTwice(R);
			byte[] dValR = new byte[]{D[2][0],D[1][0],D[0][0]};
			byte[] uValR = new byte[]{U[2][2],U[1][2],U[0][2]};
			byte[] bValR = new byte[]{B[2][0],B[1][0],B[0][0]};
			byte[] fValR = new byte[]{F[2][2],F[1][2],F[0][2]};
			for(byte i=0;i<3;i++){
				B[i][0] = fValR[i];
				D[i][0] = uValR[i];
				F[i][2] = bValR[i];
				U[i][2] = dValR[i];
			}
			
		}
		if(movement == CubeConstants.U2){
			U = rotTwice(U);
			for(byte i=0;i<3;i++){
				byte t = L[0][i];
				L[0][i] = R[0][i];
				R[0][i] = t;
				byte tt = F[0][i];
				F[0][i] = B[0][i];
				B[0][i] = tt;
			}
		}
		if(movement == CubeConstants.D2){
			D = rotTwice(D);
			for(byte i=0;i<3;i++){
				byte t = L[2][i];
				L[2][i] = R[2][i];
				R[2][i] = t;
				byte tt = F[2][i];
				F[2][i] = B[2][i];
				B[2][i] = tt;
			}
		}
		if(movement == CubeConstants.F2){
			F = rotTwice(F);
			byte[] rValF = new byte[]{R[2][0],R[1][0],R[0][0]};
			byte[] lValF = new byte[]{L[2][2],L[1][2],L[0][2]};
			
			for(byte i=0;i<3;i++){
				L[i][2] = rValF[i];
				R[i][0] = lValF[i];
				byte t = D[2][i];
				D[2][i] = U[2][i];
				U[2][i] = t;
			}
		}
		if(movement == CubeConstants.B2){
			B = rotTwice(B);
			byte[] rValB = new byte[]{R[2][2],R[1][2],R[0][2]};
			byte[] lValB = new byte[]{L[2][0],L[1][0],L[0][0]};
			
			for(byte i=0;i<3;i++){
				L[i][0] = rValB[i];
				R[i][2] = lValB[i];
				byte t = D[0][i];
				D[0][i] = U[0][i];
				U[0][i] = t;
			}
		}
	}
	
	public byte[][]getLeft(){return L;}
	public byte[][]getRight(){return R;}
	public byte[][]getUp(){return U;}
	public byte[][]getDown(){return D;}
	public byte[][]getFront(){return F;}
	public byte[][]getBack(){return B;}
	
	public void setLeft(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				L[i][j] = values[i][j];
			}
		}
		
	}
	public void setRight(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				R[i][j] = values[i][j];
			}
		}
	}
	public void setUp(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				U[i][j] = values[i][j];
			}
		}
	}
	public void setDown(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				D[i][j] = values[i][j];
			}
		}
	}
	public void setFront(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				F[i][j] = values[i][j];
			}
		}
	}
	public void setBack(byte[][]values){
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				B[i][j] = values[i][j];
			}
		}
	}
	
	private static byte[][] getFlipped(byte[][] input){
		byte[][] output = new byte[3][3];
		for(byte i=0;i<3;i++){
			output[0][i] = input[2][i];
		}
		for(byte i=0;i<3;i++){
			output[1][i] = input[1][i];
		}
		for(byte i=0;i<3;i++){
			output[2][i] = input[0][i];
		}
		return output;
		
	}
	private static byte[][] getHorizFlipped(byte[][] input){
		byte[][] output = new byte[3][3];
		for(byte i=0;i<3;i++){
			output[i][0] = input[i][2];
		}
		for(byte i=0;i<3;i++){
			output[i][1] = input[i][1];
		}
		for(byte i=0;i<3;i++){
			output[i][2] = input[i][0];
		}
		return output;
	}
	private static byte[][] rotClock(byte [][] input){
		byte[][] output = new byte[3][3];
		for(byte i=0;i<=2;i++){
			for(byte j=0;j<=2;j++){
				output[i][j] = input[2-j][i];
			}
		}
		return output;
		
	}
	private static byte[][] rotAntiClock(byte[][] input){
		byte[][]output  = new byte[3][3];
		for(byte i=0;i<=2;i++){
			for(byte j=0;j<=2;j++){
				output[i][j] = input[j][2-i];
			}
		}
		return output;
	}
	private static byte[][] rotTwice(byte [][] input){
		byte[][]output  = new byte[3][3];
		for(byte i=0;i<=2;i++){
			for(byte j=0;j<=2;j++){
				output[i][j] = input[2-i][2-j];
			}
		}
		return output;
	}
	
	public void rotateCube(byte[] rotations){
		if(rotations.length!=0){
			for(byte i=0;i<rotations.length; i++){
				rotateCube(rotations[i]);
			}
		}
	}
	public void rotateCube(byte movement){
		byte[][]tempU = deepCopy(U);
		byte[][]tempD = deepCopy(D);
		byte[][]tempL = deepCopy(L);
		byte[][]tempR = deepCopy(R);
		byte[][]tempF = deepCopy(F);
		byte[][]tempB = deepCopy(B);
		// clockwise rotation using horizontal perpendicular axis
		if(movement==CubeConstants.CUBE_DOWN){
			F = tempU;
			D = getHorizFlipped(getFlipped(tempF));
			B = tempD;
			U = getHorizFlipped(getFlipped(tempB));
			L = rotClock(tempL);
			R = rotAntiClock(tempR);
			
		}
		// anticlockwise rotation using same axis as above
		else if(movement== CubeConstants.CUBE_UP){
			U = tempF;
			F = getHorizFlipped(getFlipped(tempD));
			D = tempB;
			B = getHorizFlipped(getFlipped(tempU));
			L = rotAntiClock(tempL);
			R = rotClock(tempR);
		}
		// 2*clockwise rotation as above
		else if(movement== CubeConstants.CUBE_VERT_FLIP){
			rotateCube(CubeConstants.CUBE_DOWN);
			rotateCube(CubeConstants.CUBE_DOWN);
			rotateCube(CubeConstants.CUBE_HORIZ_FLIP);
		}
		// vertical axis clockwise rotation
		else if(movement== CubeConstants.CUBE_LEFT){
			U = rotClock(tempU);
			D = rotAntiClock(tempD);
			L = tempF;
			F = tempR;
			R = tempB;
			B = tempL;
		}
		// vertical axis anticlockwise rotation
		else if(movement== CubeConstants.CUBE_RIGHT){
			U = rotAntiClock(tempU);
			D = rotClock(tempD);
			F = tempL;
			L = tempB;
			B = tempR;
			R = tempF;
		}
		// 2* rotation as above
		else if(movement == CubeConstants.CUBE_HORIZ_FLIP){
			rotateCube(CubeConstants.CUBE_LEFT);
			rotateCube(CubeConstants.CUBE_LEFT);
		}
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
		printFace(U);
		System.out.println("LEFT:");
		printFace(L);
		System.out.println("FRONT:");
		printFace(F);
		System.out.println("RIGHT:");
		printFace(R);
		System.out.println("BACK:");
		printFace(B);
		System.out.println("DOWN:");
		printFace(D);
	}
	
	private static byte[][]deepCopy(byte[][] input){
		byte[][] output = new byte[3][3];
		for(byte i=0;i<3;i++){
			for(byte j=0;j<3;j++){
				output[i][j] = input[i][j];
			}
		}
		return output;
	}
	
	public boolean SevenStep(byte step){
		if(step == CubeConstants.CROSS){
			byte uC = U[1][1];
			byte lC = L[1][1];
			byte rC = R[1][1];
			byte fC = F[1][1];
			byte bC = B[1][1];
			if(U[0][1] == uC && U[1][0] == uC && U[1][2] == uC && U[2][1] == uC){
				if(L[0][1]==lC && R[0][1] == rC && F[0][1] == fC && B[0][1] == bC){
					return true;
				}
			}
		}
		else if(step == CubeConstants.TLC){
			byte uC = U[1][1];
			byte lC = L[1][1];
			byte rC = R[1][1];
			byte fC = F[1][1];
			byte bC = B[1][1];
			if(U[0][0] == uC && U[0][2] == uC && U[2][0] == uC && U[2][2] == uC){
				if(B[0][2] == bC && B[0][0] == bC && L[0][0] == lC && L[0][2] == lC){
					if(R[0][0] == rC && R[0][2] == rC && F[0][0] == fC && F[0][2] == fC){
						if(SevenStep(CubeConstants.CROSS)){
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	public LogicalCube deepCopy() {
		Cube out = new Cube();
		out.setBack(getBack());
		out.setLeft(getLeft());
		out.setFront(getFront());
		out.setRight(getRight());
		out.setUp(getUp());
		out.setDown(getDown());
		return new Cube(out);
	
	}


	
}
