import java.util.Random;

public interface TwoPhaseLogicalCube {
		
	public int getEO();
	public int getCO();
	public int getCP();
	public int getUD();
	public int getP2UD();
	public int getP2EP();
	public void setEO(int value);
	public void setCO(int value);
	public void setCP(int value);
	public void setUD(int value);
	public void setP2UD(int value);
	public void setP2EP(int value);
	
	public void randomise(Random r);
	public byte[] getSetupMoves();
	public boolean isSolvedP1();
	public boolean isSolvedP2();
	public void setP2CoOrdinates(byte[] moves, subCubedCube subCubed);
	public void printCube();
	
	public void doMoveP1(byte[] movements);
	public void doMoveP1(byte movement);
	public void doMoveP2(byte[] movements);
	public void doMoveP2(byte movement);
	public int[] doMoveP2(byte movement, int[] coords);
	public boolean tryMoveP1(byte movement);
	public boolean tryMoveP2(byte movement);
	public boolean tryMoveP2(byte movement, int[] coords);
	public boolean tryMatchP1(byte movement);
	public boolean tryMatchP2(byte i);
	public boolean tryMatchP2();
	public boolean tryMatchP2(byte i, int[] coords);
		
	public void loadP1PrunTable();
	public void loadP2PrunTable();
	public void generatePhase1Prun();
	public void generatePhase2Prun();
	public void dumpP1PruningTable();
	public void dumpP2PruningTable();
	
	public void loadMovTables();
}
