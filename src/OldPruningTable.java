import java.io.Serializable;

/**
 * Class that fits 8 bit values (reached or not reached)
 * into each byte in a list of bytes.
 * Minimises memory by using one long array (only 8bytes setup cost)
 * at the cost of increased complexity of implementation
 * @author Ranulf Green
 *
 */
public class OldPruningTable implements Serializable{
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
    public OldPruningTable(int width, int height){
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
    public OldPruningTable(long width, long height, long depth){
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
    public OldPruningTable(byte[] list, int width, int height, int depth){
        this.list = list;
        this.width = (int) width;
        this.height = (int) height;
        this.depth = (int) depth;

    }
    public OldPruningTable(byte[] list, int width, int height){
        this.list = list;
        this.width = (int) width;
        this.height = (int) height;


    }

    private byte[]decode(int window){
        if(window<0){
            window+=256;
        }
        int total = window;
        byte[]broken = new byte[8];
        broken[0] = (byte) (window/128);
        total -= broken[0]*128;
        broken[1] = (byte) (total/64);
        total -= broken[1]*64;
        broken[2] = (byte) (total/32);
        total -= broken[2]*32;
        broken[3] = (byte) (total/16);
        total -= broken[3]*16;
        broken[4] = (byte) (total/8);
        total -= broken[4]*8;
        broken[5] = (byte) (total/4);
        total -= broken[5]*4;
        broken[6] = (byte) (total/2);
        total -= broken[6]*2;
        broken[7] = (byte) total;
        return broken;
    }
    private int encode(byte[]broken){
        int window = 0;
        window+=broken[0]*128;
        window+=broken[1]*64;
        window+=broken[2]*32;
        window+=broken[3]*16;
        window+=broken[4]*8;
        window+=broken[5]*4;
        window+=broken[6]*2;
        window+=broken[7];
        return window;
    }

    public void setReached(int hPlace, int vPlace){
        long ww = (width*vPlace + hPlace);
        int w = (int) (ww/8);
        int window = list[w];
        byte[] broken = decode(window);
        broken[(int) (ww%8)] = 1;
        int output = encode(broken);
        list[w] = (byte)(output);
    }
    public boolean getReached(int hPlace, int vPlace){
        long ww = (width*vPlace + hPlace);
        int w = (int) (ww/8);
        int window = list[w];
        byte[] broken = decode(window);
        if(broken[(int) (ww%8)] == 1){
            return true;
        }else{
            return false;
        }

    }
    public void setReached(int hPlace, int vPlace, int dPlace){  // EO  CO  UD
        long ww = ((long)(width*height)*dPlace + width*vPlace + hPlace);
        int w = (int) (ww/8);
        int window = list[w];
        byte[] broken = decode(window);
        broken[(int) (ww%8)] = 1;
        int output = encode(broken);
        list[w] = (byte)(output);

    }
    public boolean getReached(int hPlace, int vPlace, int dPlace){   // EO  CO  UD
        long ww = ((long)(width*height)*dPlace + width*vPlace + hPlace);
        int w = (int) (ww/8);
        int window = list[w];
        byte[] broken = decode(window);
        if(broken[(int) (ww%8)] == 1){
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
