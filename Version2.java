import java.util.concurrent.ForkJoinPool;

public class Version2 implements TotalPopulation {
	public static final ForkJoinPool fj = new ForkJoinPool();
	public CensusData cd; 
	public int ga; // columns
	public int gb; // rows
	float[] c; // array 
	
	public Version2(CensusData cd, int a, int b) {
		this.cd = cd;
		ga = a;
		gb = b;
		c = ParallelCorners.corners(cd);
	}
	
	public int[] gridPopulation(int w, int s, int e, int n) {
		if (!valid(w, s, e, n))
			throw new IllegalArgumentException("Invalid input");
		int[] r = new int[]{w, s, e, n};
		ParallelGridPop spg = new ParallelGridPop(cd, c, r, ga, gb, 0, cd.data_size);
		fj.invoke(spg);
		return spg.pop;
	}
	
	public boolean valid(int w, int s, int e, int n) {
		return (w > 0 && s > 0 && (e >= w && e <= ga) && (n >= s && n <= gb));
	}
}