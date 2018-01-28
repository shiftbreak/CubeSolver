
public  class CubeConstants  {
	public final static byte P2U = 0;
	public final static byte P2UU = 1;
	public final static byte P2D = 2;
	public final static byte P2DD = 3;
	public final static byte P2L2 = 4;
	public final static byte P2R2 = 5;
	public final static byte P2U2 = 6;
	public final static byte P2D2 = 7;
	public final static byte P2F2 = 8;
	public final static byte P2B2 = 9;

	
	public final static byte CUBE_DOWN = 1;
	public final static byte CUBE_LEFT = 2;
	public final static byte CUBE_RIGHT = 3;
	public final static byte CUBE_UP = 4;
	public final static byte CUBE_VERT_FLIP = 5;
	public final static byte CUBE_HORIZ_FLIP = 6;
	public final static byte CUBE_HORIZ_REFLECT = 7;
	public final static byte L = 7;
	public final static byte LL = 8;
	public final static byte R = 9;
	public final static byte RR = 10;
	public final static byte U = 11;
	public final static byte UU = 12;
	public final static byte D = 13;
	public final static byte DD = 14;
	public final static byte F = 15;
	public final static byte FF = 16;
	public final static byte B = 17;
	public final static byte BB = 18;
	public final static byte L2 = 19;
	public final static byte R2 = 20;
	public final static byte U2 = 21;
	public final static byte F2 = 22;
	public final static byte D2 = 23;
	public final static byte B2 = 24;
	public final static byte S_F2 = 25;
	public final static byte S_U4 = 26;
	public final static byte S_UU4 = 27;
	public final static byte S_U42 = 28;
	public final static byte S_LR = 29;
	public static String getName(int n){
		if(n == 7){return "L";}
		if(n == 8){return "LL";}
		if(n == 9){return "R";}
		if(n == 10){return "RR";}
		if(n == 11){return "U";}
		if(n == 12){return "UU";}
		if(n == 13){return "D";}
		if(n == 14){return "DD";}
		if(n == 15){return "F";}
		if(n == 16){return "FF";}
		if(n == 17){return "B";}
		if(n == 18){return "BB";}
		if(n == 19){return "L2";}
		if(n == 20){return "R2";}
		if(n == 21){return "U2";}
		if(n == 22){return "F2";}
		if(n == 23){return "D2";}
		if(n == 24){return "B2";}
		else{return "INVALID MOVE NUMBER";}
	}



	
	public final static byte WHITE = 25;
	public final static byte BLUE = 26;
	public final static byte RED = 27;
	public final static byte ORANGE = 28;
	public final static byte YELLOW = 29;
	public final static byte GREEN = 30;
	public final static byte FIRST = 31;
	public final static byte SECOND = 32;
	public final static byte THIRD = 33;
	
	public final static Cube solvedCube = new Cube(
			new byte[][]{{WHITE,WHITE,WHITE},{WHITE,WHITE,WHITE},{WHITE,WHITE,WHITE}},
			new byte[][]{{YELLOW,YELLOW,YELLOW},{YELLOW,YELLOW,YELLOW},{YELLOW,YELLOW,YELLOW}},
	        new byte[][]{{BLUE,BLUE,BLUE},{BLUE,BLUE,BLUE},{BLUE,BLUE,BLUE}},
	        new byte[][]{{GREEN,GREEN,GREEN},{GREEN,GREEN,GREEN},{GREEN,GREEN,GREEN}},
	        new byte[][]{{RED,RED,RED},{RED,RED,RED},{RED,RED,RED}},
	        new byte[][]{{ORANGE,ORANGE,ORANGE},{ORANGE,ORANGE,ORANGE},{ORANGE,ORANGE,ORANGE}}
	        
	        
			
	);
	public final static byte CROSS = 34;
	public static final byte TLC = 35;
	public static final byte SLE =36;
	public final static byte BLC = 37;
	public final static byte BLCP = 38;
	public final static byte BLCO = 39;
	public final static byte BLEP = 40;
	
	public final static byte URF = 41;
	public final static byte ULF = 42;
	public final static byte ULB = 43;
	public final static byte URB = 44;
	public final static byte DRF = 45;
	public final static byte DLF = 46;
	public final static byte DLB = 47;
	public final static byte DRB = 48;
	
	public final static byte UR = 49;
	public final static byte UF = 50;
	public final static byte UL = 51;
	public final static byte UB = 52;
	public final static byte DR = 53;
	public final static byte DF = 54;
	public final static byte DL = 55;
	public final static byte DB = 56;
	public final static byte FR = 57;
	public final static byte FL = 58;
	public final static byte BL = 59;
	public final static byte BR = 60;
	
	
	
}