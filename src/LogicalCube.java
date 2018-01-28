import java.util.Random;

public interface LogicalCube {
	public void randomise(Random r);
	public void doMove(byte[] movements);
	public void doMove(byte movement);
	public byte[][]getLeft();
	public byte[][]getRight();
	public byte[][]getUp();
	public byte[][]getDown();
	public byte[][]getFront();
	public byte[][]getBack();
	public void setLeft(byte[][]values);
	public void setRight(byte[][]values);
	public void setUp(byte[][]values);
	public void setDown(byte[][]values);
	public void setFront(byte[][]values);
	public void setBack(byte[][]values);
	public void rotateCube(byte[] rotations);
	public void rotateCube(byte movement);
	public byte[] getSetupMoves();
	public LogicalCube deepCopy();
	public void printCube();
	
	
}
