import java.util.concurrent.ForkJoinPool;


public class SequentialCutOffGridBuildingTime {
	public static final ForkJoinPool fj = new ForkJoinPool();
	public static final CensusData cd = PopulationQuery.parse("CenPop2010.txt");
	public static final int ga = 100;
	public static final int gb = 500;
	
	private static void SequentialCutOffBuildGrid(Version4 sp, int c) {
		ParallelGridBuild v = new ParallelGridBuild(sp.cd.data, sp.ga, sp.gb, 
				sp.c[0], sp.c[1], sp.c[2], sp.c[3]);
		SequentialCutOffGridBuilding t = new SequentialCutOffGridBuilding(v, 0, sp.cd.data_size, c);
		fj.invoke(t);
		sp.g = t.g;
		sp.checkBuild = true;
	}
	
	public static void main(String[] args) {
		long start, end;
		
		// warm up
		for (int i = 0; i < 5; i++) {
			Version4 sp = new Version4(cd, 100, 500);
			start = System.currentTimeMillis();
			SequentialCutOffBuildGrid(sp, 50);
			end = System.currentTimeMillis();
			System.out.println("Parallel build grid with sequential cut-off = 50: " + (end - start));
		}
		
		for (int i = 2; i < 220000; i *= 2) {
			Version4 sp = new Version4(cd, 100, 500);
			start = System.currentTimeMillis();
			SequentialCutOffBuildGrid(sp, i);
			end = System.currentTimeMillis();
			System.out.println("Parallel build grid with sequential cut-off = " + i + ": " + (end - start));
		}
		
		Version4 sp = new Version4(cd, 100, 500);
		start = System.currentTimeMillis();
		SequentialCutOffBuildGrid(sp, 220000);
		end = System.currentTimeMillis();
		System.out.println("Parallel build grid with sequential cut-off = 220000: " + (end - start));
	}
}