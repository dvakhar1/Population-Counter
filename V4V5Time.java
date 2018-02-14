public class V4V5Time {
	public static final CensusData cd = PopulationQuery.parse("CenPop2010.txt");
	
	public static void main(String[] args) {
		Version4 sp;
		Version5 sl;
		long start, end;
		
		for (int i = 0; i < 5; i++) {
			start = System.currentTimeMillis();
			sp = new Version4(cd, 100, 100);
			sp.gridBuilding();
			end = System.currentTimeMillis();
			System.out.println("Version 4 with grid size 100 x 100: " + (end - start));
			start = System.currentTimeMillis();
			sl = new Version5(cd, 100, 100);
			sl.gridBuilding();
			end = System.currentTimeMillis();
			System.out.println("Version 5 with grid size 100 x 100: " + (end - start));
		}
		
		for(int i = 1; i <= 2048; i *= 2) {
			start = System.currentTimeMillis();
			sp = new Version4(cd, i, i);
			sp.gridBuilding();
			end = System.currentTimeMillis();
			System.out.println("Version 4 with grid size " + i + " x " + i + ": " + (end - start));
			start = System.currentTimeMillis();
			sl = new Version5(cd, i, i);
			sl.gridBuilding();
			end = System.currentTimeMillis();
			System.out.println("Version 5 with grid size " + i + " x " + i + ": " + (end - start));
		}
	}
}