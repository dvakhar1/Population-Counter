public class Version4 extends Version2 {
	public int[][] g;  
	protected boolean checkBuild; 
	
	
	public Version4(CensusData cd, int a, int b) {
		super(cd, a, b);
		checkBuild = false;
		g = null;
	}
	
	public void gridBuilding() {
		GridBuildingV4 sgb = new GridBuildingV4(
				new ParallelGridBuild(cd.data, ga, gb, c[0], c[1], c[2], c[3]), 0, cd.data_size);
		fj.invoke(sgb);
		int[][] temp = sgb.g;
		Version3.gridBuildingStepTwo(temp);
		g = temp;
		checkBuild = true;
	}
	
	public int[] gridPopulation(int w, int s, int e, int n) {
		if (!checkBuild)
			gridBuilding();
		if (!valid(w, s, e, n))
			throw new IllegalArgumentException("Invalid input");
		int[] r = new int[2];
		int tl, br, ll; 
		if (s - 2 < 0)
			tl = 0;
		else
			tl = g[e - 1][s - 2];
		if (w - 2 < 0)
			br = 0;
		else
			br = g[w - 2][n - 1];
		if (s - 2 < 0 || w - 2 < 0)
			ll = 0;
		else
			ll = g[w - 2][s - 2];

		r[0] = g[e - 1][n - 1] - tl - br + ll;
		r[1] = g[ga - 1][gb - 1];
		return r;
	}
}