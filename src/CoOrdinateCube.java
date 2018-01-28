import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Random;

public class CoOrdinateCube implements TwoPhaseLogicalCube{
	
	/*
	 * Fast cube is a class that creates a cube in the form of co-ordinates
	 * the following coordinates are to be used:
	 * 
	 * Corner orientation coordinate:
	 * 0-2186
	 * 
	 * Edge orientation co-ordinate:
	 * 0-2047
	 * 
	 * Corner position coordinate:
	 * 0-40319
	 * 
	 * Edge postion coordinate:
	 * 0-479001599
	 * 
	 * Any different cube can be represented as a distinct combination of these
	 * values.
	 * 
	 * the convention used here for order of edge pieces is:
	 * 
	 * UR,UF,UL,UB,DR,DF,DL,DB,FR,FL,BL,BR
	 * 
	 */

	private int[][] EOMovTable;
	private int[][] COMovTable;
	private int CO = 0;
	private int EO = 0;
	private int UD = 0;
	private int CP = 0;
	private int P2EP = 0;
	private int P2UD = 0;
	private int[][] CPMovTable;
	private int[][] UDSliceMovTable;
	private int[][] UDSlicePhaseTwoMovTable;
	private int[][] EPPhaseTwoMovTable;
	private byte[] setupmoves;
	private PruningTable P1PrunTable;
	private PruningTable P2PrunTable;



	public CoOrdinateCube(){
		COMovTable = new int[18][2187];
		EOMovTable = new int[18][2048];
		UDSliceMovTable = new int[18][495];
		UDSlicePhaseTwoMovTable = new int[10][11880];
		CPMovTable = new int[10][40320];
		EPPhaseTwoMovTable = new int[10][40320];
		loadMovTables();
	}
	public CoOrdinateCube(int CO,int EO,int UD){
		this.CO = CO;
		this.EO = EO;
		this.UD = UD;
		COMovTable = new int[18][2187];
		EOMovTable = new int[18][2048];
		UDSliceMovTable = new int[18][495];
		UDSlicePhaseTwoMovTable = new int[10][11880];
		CPMovTable = new int[10][40320];
		EPPhaseTwoMovTable = new int[10][40320];
	}
	public CoOrdinateCube(int CO,int EO,int UD, int CP, int P2EP, int P2UD){
		this.CO = CO;
		this.EO = EO;
		this.UD = UD;
		this.CP = CP;
		this.P2EP = P2EP;
		this.P2UD = P2UD;
		COMovTable = new int[18][2187];
		EOMovTable = new int[18][2048];
		UDSliceMovTable = new int[18][495];
		UDSlicePhaseTwoMovTable = new int[10][11880];
		CPMovTable = new int[10][40320];
		EPPhaseTwoMovTable = new int[10][40320];
	}
	public CoOrdinateCube(int CO,int EO,int UD,int P2EP, int P2UD, int CP, int[][] COMovTable, int[][]EOMovTable,int[][]UDSliceMovTable,int[][]UDSlicePhaseTwoMovTable,int[][]CPMovTable,int[][]EPPhaseTwoMovTable){
		this.COMovTable = COMovTable;
		this.EOMovTable = EOMovTable;
		this.UDSliceMovTable = UDSliceMovTable;
		this.UDSlicePhaseTwoMovTable = UDSlicePhaseTwoMovTable;
		this.CPMovTable = CPMovTable;
		this.EPPhaseTwoMovTable = EPPhaseTwoMovTable;
		loadMovTables();
		this.CO = CO;
		this.EO = EO;
		this.UD = UD;
		this.P2EP = P2EP;
		this.P2UD = P2UD;
		this.CP = CP;
	}
	public CoOrdinateCube(subCubedCube c){
		
		COMovTable = new int[18][2187];
		EOMovTable = new int[18][2048];
		UDSliceMovTable = new int[18][495];
		UDSlicePhaseTwoMovTable = new int[10][11880];
		CPMovTable = new int[10][40320];
		EPPhaseTwoMovTable = new int[10][40320];
		loadMovTables();
		
		CO = c.getCO();
		EO = c.getEO();
		UD = c.getUD();
		CP = c.getCP();
		P2UD = c.getP2UD();
		P2EP = c.getP2EP();
		
		
	
		
	}
	
	
	
	public void generateCOMovTable(){
		for(byte i=7;i<25;i++){
			for(int j=0;j<2187;j++){
				subCubedCube c = new subCubedCube();
				c.setCO(j);
				c.doMove(i);
				COMovTable[i-7][j] = c.getCO();
			}
		}
	}
	
	public void generateEOMovTable(){
		for(byte i=7;i<25;i++){
			for(int j=0;j<2048;j++){
				subCubedCube c = new subCubedCube();
				c.setEO(j);
				c.doMove(i);
				EOMovTable[i-7][j] = c.getEO();
			}
		}
	}
	
	
	public void generateUDSliceMovTable(){
		int width = 495;
		for(byte i=7;i<25;i++){
			for(int j=0;j<width;j++){
				subCubedCube c = new subCubedCube();
				c.setUD(j);
				c.doMove(i);
				UDSliceMovTable[i-7][j] = c.getUD();
			}
			
		}
	}
	
 	public void generateUDSlicePhaseTwoMovTable(){
		int width = 11880;
		for(byte i=0;i<10;i++){
			for(int j=0;j<width;j++){
				subCubedCube c = new subCubedCube();
				c.setP2UD(j);
				c.doMoveP2(i);
				UDSlicePhaseTwoMovTable[i][j] = c.getP2UD();
			}
		}
	}
 	
	public void generateCPMovTable(){
		for(byte i=0;i<10;i++){
			for(int j=0;j<40320;j++){
				subCubedCube c = new subCubedCube();
				c.setCP(j);
				c.doMoveP2(i);
				CPMovTable[i][j] = c.getCP();
			}
		}
	}
	
	public void generateEPPhaseTwoMovTable(){
		// cannot generate a full table so will use a smaller one:
		// do not consider the permutations of the middle layer edges.
		int width = 40320;
		for(byte i=0;i<10;i++){
			for(int j=0;j<width;j++){
				subCubedCube c = new subCubedCube();
				c.setP2EP(j);
				c.doMoveP2(i);
				EPPhaseTwoMovTable[i][j] = c.getP2EP();
			}
		}
	}
	
	private void updateP1Prun(CoOrdinateCube c, byte[] i){
		c.doMoveP1(i);
		int EO = c.getEO();
		int CO = c.getCO();
		int UD = c.getUD();
		P1PrunTable.setReached(EO,CO,UD);
		
	}
	public  void generatePhase1Prun(){
		int width = 2048;  // EO
		int height = 2187;  // CO
		int depth = 495;  // UD
	
		P1PrunTable = new PruningTable(width,height,depth);
		System.out.println("0%");

		CoOrdinateCube c = new CoOrdinateCube(0,0,0,0,0,0,COMovTable,EOMovTable,UDSliceMovTable, UDSlicePhaseTwoMovTable, CPMovTable, EPPhaseTwoMovTable);
		for(byte i=7;i<25;i++){
			for(byte j=7;j<25;j++){
				for(byte k=7;k<25;k++){
					for(byte l=7;l<25;l++){
						for(byte m=7;m<25;m++){
							for(byte n=7;n<25;n++){
								CO = c.getCO();
								EO = c.getEO();
								UD = c.getUD();
								updateP1Prun(c,new byte[]{i,j,k,l,m,n});
								c.setCO(CO);
								c.setEO(EO);
								c.setUD(UD);
							}
						}
					}
				}
			}
			
			if(i-7==4){
				System.out.println("25%");
			}
			if(i-7==9){
				System.out.println("50%");
			}
			if(i-7==13){
				System.out.println("75%");
			}
			if(i-7==17){
				System.out.println("100%");
			}
		}
	}
	public void dumpP1PruningTable(){
		P1PrunTable = null;
	}
	public void dumpP2PruningTable(){
		P2PrunTable = null;
	}
	private void updateP2Prun(CoOrdinateCube c, byte[] i){
		c.doMoveP2(i);
		int P2EP = c.getP2EP();
		int CP = c.getCP();
		P2PrunTable.setReached(CP,P2EP);
		
	}
		
	public  void generatePhase2Prun(){
		int width = 40320;  // CP
		int height = 40320;  // EP
		P2PrunTable = new PruningTable(width,height);
		System.out.println("0%");
		CoOrdinateCube c = new CoOrdinateCube(0,0,0,0,0,0,COMovTable,EOMovTable,UDSliceMovTable, UDSlicePhaseTwoMovTable, CPMovTable, EPPhaseTwoMovTable);
		int progress = 1;
		for(byte i=0;i<10;i++){
			for(byte j=0;j<10;j++){
				for(byte k=0;k<10;k++){
					for(byte l=0;l<10;l++){
						for(byte m=0;m<10;m++){
							for(byte n=0;n<10;n++){
								for(byte o=0;o<10;o++){
									CP = c.getCP();
									P2EP = c.getP2EP();
									updateP2Prun(c,new byte[]{i,j,k,l,m,n,o});
									c.setCP(CP);
									c.setP2EP(P2EP);
								}
							}
						}
					}
				}
			}
			System.out.println(progress+"0%");
			progress++;
		}	
	}
	
	
	public int getP2EP(){
		return P2EP;
	}
	public int getEO(){
		return EO;
		
	}
	public int getCO(){
		return CO;
		
	}
	public int getUD(){
		return UD;
		
	}
	public int getCP(){
		return CP;
	}
	public int getP2UD(){
		return P2UD;
	}
	
	public int[][] getCOMovTable(){
		return COMovTable;
	}
	public int[][] getEOMovTable(){
		return EOMovTable;
	}
	public int[][] getUDSliceMovTable(){
		return UDSliceMovTable;
	}
	public int[][]getUDSlicePhaseTwoMovTable(){
		return UDSlicePhaseTwoMovTable;
	}
	public int[][]getEPPhaseTwoMovTable(){
		return EPPhaseTwoMovTable;
	}
	public int[][] getCPMovTable(){
		return CPMovTable;
	}
	
	public void setCP(int CP){
		this.CP = CP;
	}
	public void setP2EP(int P2EP){
		this.P2EP = P2EP;
	}
	public void setP2UD(int P2UD){
		this.P2UD = P2UD;
	}
	
	public void randomise(Random r) {
		subCubedCube c = new subCubedCube();
		c.randomise(r);
		EO = c.getEO();
		CO = c.getCO();
		UD = c.getUD();
		CP = c.getCP();
		P2UD  =c.getP2UD();
		P2EP = c.getP2EP();
		setupmoves = c.getSetupMoves();
	}
	public void doMoveP1(byte[] movements) {
		for(int i=0;i<movements.length;i++){
			doMoveP1(movements[i]);
		}
	}
	public void doMoveP1(byte movement){
		CO = COMovTable[movement-7][CO];
		EO = EOMovTable[movement-7][EO];
		UD = UDSliceMovTable[movement-7][UD];
	}
	public boolean tryMoveP1(byte movement){
		boolean a = true;
		if(COMovTable[movement-7][CO]!=0){a = false;}
		if(EOMovTable[movement-7][EO]!=0){a = false;}
		if(UDSliceMovTable[movement-7][UD]!=0){a = false;}
		return a;
	}
	public boolean tryMatchP1(byte movement){
		int COt = COMovTable[movement-7][CO];
		int EOt = EOMovTable[movement-7][EO];
		int UDt = UDSliceMovTable[movement-7][UD];
		return P1PrunTable.getReached(EOt,COt,UDt);
		
	}
	public boolean tryMatchP2(byte movement){
		int CPt = CPMovTable[movement][CP];
		int P2EPt = EPPhaseTwoMovTable[movement][P2EP];
		return P2PrunTable.getReached(CPt,P2EPt);
	}
	public boolean tryMatchP2(){
		return P2PrunTable.getReached(CP,P2EP);
	}
	
	public boolean tryMatchP2(byte movement, int[]coords){
		return P2PrunTable.getReached(CPMovTable[movement][coords[0]],
				EPPhaseTwoMovTable[movement][coords[2]]);
	}
	
	
	
	public void doMoveP2(byte[] movements) {
		for(int i=0;i<movements.length;i++){
			doMoveP2(movements[i]);
		}
	}
	public void doMoveP2(byte movement){
		CP  = CPMovTable[movement][CP];
		P2UD = UDSlicePhaseTwoMovTable[movement][P2UD];
		P2EP = EPPhaseTwoMovTable[movement][P2EP];
	}
	int[] coordsout = new int[3];
	public int[] doMoveP2(byte movement, int[] coords){
		coordsout[0]  = CPMovTable[movement][coords[0]];
		coordsout[1] = UDSlicePhaseTwoMovTable[movement][coords[1]];
		coordsout[2] = EPPhaseTwoMovTable[movement][coords[2]];
		return coordsout;
	}
	
	public boolean tryMoveP2(byte movement){
		boolean a = true;
		if(CPMovTable[movement][CP]!=0){a = false;}
		if(UDSlicePhaseTwoMovTable[movement][P2UD]!=0){a = false;}
		if(EPPhaseTwoMovTable[movement][P2EP]!=0){a = false;}
		return a;
	}
	boolean abc = true;
	public boolean tryMoveP2(byte movement, int[]coords){
		abc = true;
		if(CPMovTable[movement][coords[0]]!=0){abc = false;}
		if(UDSlicePhaseTwoMovTable[movement][coords[1]]!=0){abc = false;}
		if(EPPhaseTwoMovTable[movement][coords[2]]!=0){abc = false;}
		return abc;
	}
	
	public void printCube(){
		System.out.println("  EO: " + EO);
		System.out.println("  CO: " + CO);
		System.out.println("  UD: " + UD);
		System.out.println("  CP: " + CP);
		System.out.println("P2UD: " + P2UD);
		System.out.println("P2EP: " + P2EP);
	}
	
	
	public void generateMovTables(){
		generateCOMovTable();
		writeTableToFile("COMovTable", 2187,18,COMovTable);
		generateEOMovTable();
		writeTableToFile("EOMovTable", 2048,18,EOMovTable);
		generateUDSliceMovTable();
		writeTableToFile("UDSliceMovTable", 495,18,UDSliceMovTable);
		generateCPMovTable();
		writeTableToFile("CPMovTable", 40320,10,CPMovTable);
		generateEPPhaseTwoMovTable();
		writeTableToFile("EPPhaseTwoMovTable", 40320,10,EPPhaseTwoMovTable);
		generateUDSlicePhaseTwoMovTable();
		writeTableToFile("UDSlicePhaseTwoMovTable", 11880,10,UDSlicePhaseTwoMovTable);
		
	}
	public void generatePruningTables(){
		System.out.println("Generating Phase 1 pruning table: ");
		generatePhase1Prun();
		writePruningTableToFile("P1PrunTable", P1PrunTable);
		dumpP1PruningTable();
		System.out.println("Generating Phase 2 pruning table: ");
		generatePhase2Prun();
		writePruningTableToFile("P2PrunTable", P2PrunTable);
		dumpP2PruningTable();
	}
	
	public void loadMovTables(){
		readTableFromFile("COMovTable", 2187,18,COMovTable);
		readTableFromFile("EOMovTable", 2048,18,EOMovTable);
		readTableFromFile("UDSliceMovTable", 495,18,UDSliceMovTable);
		readTableFromFile("UDSlicePhaseTwoMovTable", 11880,10,UDSlicePhaseTwoMovTable);
		readTableFromFile("CPMovTable", 40320,10,CPMovTable);
		readTableFromFile("EPPhaseTwoMovTable", 41320,10,EPPhaseTwoMovTable);
	}
	
	

	public static void main(String[] argv){
		CoOrdinateCube c = new CoOrdinateCube(0,0,0);
		c.generateMovTables();
		c.generatePruningTables();
	}
	
	
	public void readTableFromFile(String filename, int width, int height, int[][] table){
		System.out.print("Loading " + filename + " . . .");
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			int n = 0;
			while((line = in.readLine()) != null){
				String[] values = line.split(" ");
				for(int i=0;i<values.length;i++){
					table[n][i] = Integer.parseInt(values[i]);
				}
				n++;
			}
		
		
		} catch (FileNotFoundException e) {
			System.out.println("Tables not found: Generating move tables:");
			generateMovTables();
		} catch (IOException e) {
			System.err.print("INVALID FILE TYPE");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e){
			System.err.print("INVALID TABLE FILE");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Done!");
	}
	
	public void loadP1PrunTable(){
		System.out.print("Loading Phase 1 Pruning table . . .");
		try{
			InputStream is = new FileInputStream("P1PrunTable");
			BufferedInputStream buf = new BufferedInputStream(is);
			ObjectInputStream in = new ObjectInputStream(buf);
			Object a = in.readObject();
			if(a instanceof byte[]){
				byte[] table = (byte[])a;
				int width = 2048;  // EO
				int height = 2187;  // CO
				int depth = 495;  // UD
				P1PrunTable = new PruningTable(table,width,height,depth);
			}else{
				System.err.println(a.getClass() + " is not an instance of Pruning Table!");
				System.exit(0);
			}
			
		}catch(FileNotFoundException e){
			System.out.println("PRUNING TABLES NOT FOUND: GENERATING!");
			generatePruningTables();
			loadP1PrunTable();
		
		}catch (IOException e) {e.printStackTrace();
		}catch (ClassNotFoundException e) {e.printStackTrace();
		}
		System.out.println("Done!");
	}
	
	public void loadP2PrunTable(){
		System.out.print("Loading Phase 2 Pruning table . . .");
		try{
			InputStream is = new FileInputStream("P2PrunTable");
			BufferedInputStream buf = new BufferedInputStream(is);
			ObjectInputStream in = new ObjectInputStream(buf);
			Object a = in.readObject();
			if(a instanceof byte[]){
				byte[] table = (byte[])a;;;
				int width = 40320;  // CP
				int height = 40320;  // EP
				P2PrunTable = new PruningTable(table,width,height);
			}else{
				System.err.println(a.getClass() + " is not an instance of Pruning Table!");
				System.exit(0);
			}
			
		}catch(FileNotFoundException e){
			System.out.println("PRUNING TABLES NOT FOUND: GENERATING!");
			generatePruningTables();
			loadP2PrunTable();
		}catch (IOException e) {e.printStackTrace();
		}catch (ClassNotFoundException e) {e.printStackTrace();
		}
		System.out.println("Done!");
	}
	
	public void writePruningTableToFile(String filename, PruningTable p){
		System.out.println("Started writing " + filename + " to file");
		try {
			byte[] table = p.getTable();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			//DataOutputStream os = new DataOutputStream(new FileOutputStream(filename));		
			//os.write(table);
			out.writeObject(table);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished wriiting "+ filename +" to file");
		
	}
	
	
	
	public void writeTableToFile(String filename, int width, int height, int[][] table) {
		System.out.println("started writing " + filename + " to File");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename), 1024);
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					out.write(Integer.toString(table[i][j])+ " ");
					out.flush();
					
				}
				out.newLine();
			}
		
			out.close();
			System.out.println("finished writing " + filename + " to File");
		}catch (IOException e) {
			
		}
		
	}
	public boolean isSolvedP1(){
		int tot = CO+UD+EO;
		if(tot == 0){
			return true;
		}else{
			return false;
		}
	}
	public boolean isSolvedP2(){
		int tot = P2UD+P2EP+CP;
		if(tot == 0){
			return true;
		}else{
			return false;
		}
	}
	public void setP2CoOrdinates(byte[] moves, subCubedCube c){
		c.doMove(moves);
		P2EP = c.getP2EP();
		P2UD = c.getP2UD();
		CP = c.getCP();
	}
	
	public void setEO(int value) {
		this.EO = value;
		
	}
	public void setCO(int value) {
		this.CO = value;
		
	}
	public void setUD(int value) {
		this.UD = value;
	}
	public byte[] getSetupMoves() {
		return setupmoves;
	}
	
	public TwoPhaseLogicalCube deepCopy() {
		CoOrdinateCube c = new CoOrdinateCube();
		c.setCO(CO);
		c.setCP(CP);
		c.setEO(EO);
		c.setP2EP(P2EP);
		c.setP2UD(P2UD);
		c.setUD(UD);
		return (TwoPhaseLogicalCube) c;
	}
	
	
	
}

