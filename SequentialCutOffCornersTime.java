public class SequentialCutOffCornersTime {
	public static final CensusData cd = PopulationQuery.parse("CenPop2010.txt");
	
	public static void main(String[] args) {
		double start, end;
		float[] c;
		
	
		for (int i = 0; i < 5; i++) {
			start = System.currentTimeMillis();
			c = SequentialCutoffCorners.SequentialCutOffFindCorners(cd, 50);
			end = System.currentTimeMillis();
			System.out.println("Parallel find corners with sequential cut-off = 50: " + (end - start));
		}
		
		for (int i = 2; i < 200000; i *= 2) {
			start = System.currentTimeMillis();
			c = SequentialCutoffCorners.SequentialCutOffFindCorners(cd, i);
			end = System.currentTimeMillis();
			System.out.println("Parallel find corners with sequential cut-off = " + i + ": " + (end - start));
			System.out.println(c[0] + "," + c[1] + "," + c[2] + "," + c[3]);
			
		}
		
		start = System.currentTimeMillis();
		c = SequentialCutoffCorners.SequentialCutOffFindCorners(cd, 220000);
		end = System.currentTimeMillis();
		System.out.println("Parallel find corners with sequential cut-off = 220000: " + (end - start));
		System.out.println(c[0] + "," + c[1] + "," + c[2] + "," + c[3]);
	}
}