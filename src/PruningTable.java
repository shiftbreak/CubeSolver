import java.io.Serializable;

/**
 * Class that fits 8 bit values (reached or not reached)
 * into each byte in a list of bytes.
 * Minimises memory by using one long array (only 8bytes setup cost)
 * at the cost of increased complexity of implementation
 * @author Ranulf Green
 *
 */
public class PruningTable implements Serializable{
	/*
	 * NOTE:
	 * never attempt to run both tables at the same time since this will lead to
	 * a huge memory requirment >400Mbytes 
	 */
	byte[] list;
	int width;
	int height;
	int length;
	int depth;
	/**
	 * design for phase 2 pruning table with only two dimentions
	 * @param width
	 * @param height
	 */
	public PruningTable(int width, int height){
		long l = (width*height)/8;
		long m = (width*height)%8;
		if( m==0){
			length = (int) l;
		}else{
			length = (int) (l+1);
		}
		list = new byte[length];
		this.width = width;
		this.height = height;
		
	}
	/**
	 * design for phase1 pruning table with three dimensions.
	 * uses 240Mbytes of memory so will require extended heap space
	 * @param width
	 * @param height
	 * @param depth
	 */
	public PruningTable(long width, long height, long depth){
		long l = (width*height*depth)/8;
		long m = (width*height*depth)%8;
		if(m==0){
			length = (int) l;
		}else{
			length = (int) (l+1);
		}
		list = new byte[length];
		this.width = (int) width;
		this.height = (int) height;
		this.depth = (int) depth;
	}
	public PruningTable(byte[] list, int width, int height, int depth){
		this.list = list;
		this.width = (int) width;
		this.height = (int) height;
		this.depth = (int) depth;
		
	}
	public PruningTable(byte[] list, int width, int height){
		this.list = list;
		this.width = (int) width;
		this.height = (int) height;
	
		
	}




	public void setReached(int hPlace, int vPlace){
		long ww = (width*vPlace + hPlace);
		int w = (int) (ww/8);
		int window = list[w];
		list[w] = (byte) (window | computeMask(ww));

	}
	public boolean getReached(int hPlace, int vPlace){
		long ww = (width*vPlace + hPlace);
		int w = (int) (ww/8);
		int window = list[w];
		int mask = computeMask(ww);
		if ((window & mask) == mask){
			return true;
		}else{
			return false;
		}

	}

	public static int computeMask(long ww){
		int bit = (int) (ww%8);
		switch (bit){
			case 0:
				return 1;
			case 1:
				return 2;
			case 2:
				return 4;
			case 3:
				return 8;
			case 4:
				return 16;
			case 5:
				return 32;
			case 6:
				return 64;
			case 7:
				return 128;


		}

		return 99999;

	}



	public void setReached(int hPlace, int vPlace, int dPlace){  // EO  CO  UD
		long ww = ((long)(width*height)*dPlace + width*vPlace + hPlace);
		int w = (int) (ww/8);
		int window = list[w];
		list[w] = (byte) (window | computeMask(ww));

	
	}
	public boolean getReached(int hPlace, int vPlace, int dPlace){   // EO  CO  UD
		long ww = ((long)(width*height)*dPlace + width*vPlace + hPlace);
		int w = (int) (ww/8);
		int window = list[w];


		int mask = computeMask(ww);
		if ((window & mask) == mask){
			return true;
		}else{
			return false;
		}

	}
	public void printTable(){
		for(int i=0;i<list.length;i++){
			if(list[i]<0){
				System.out.print((256+list[i]) + " ");
			}else{
				System.out.print((list[i]) + " ");
			}
		}
	}
	public byte get(int index){
		return list[index];
	}
	public void set(int index, byte value){
		list[index] = value;
	}
	public int getSize(){
		return list.length;
	}
	public byte[] getTable(){
		return list;
	}
	
}
