import java.util.Random;

public class Tester {
	public static void main(String[] argv){


		int i = 0;

		long m = 7;

		System.out.println(8%8);

		i = i | PruningTable.computeMask(m);

		//i = i | 128;

		String s1 = String.format("%8s", Integer.toBinaryString(i & 0xFF)).replace(' ', '0');
		System.out.println(s1); // 10000001


		Random r = new Random(System.currentTimeMillis());

		int iters = 10;

		long time = System.currentTimeMillis();
		for(int j=0;j<iters;j++){

			Cube x = Cube.getRandomCube(r);
            subCubedCube cc = new subCubedCube((Cube) x.deepCopy());
            CoOrdinateCube cube = new CoOrdinateCube(cc);
            TwoPhaseSolver test = new TwoPhaseSolver(cube, cc);

			if(!test.solve()){
				System.out.println("ERROR");
				System.exit(0b0);
			}
			System.out.println("OUTPUT CUBE: \n");
			//x.printCube();
		}



		System.out.println("AVG TIME(millisec): " + (System.currentTimeMillis()-time)/iters);
	}

}
