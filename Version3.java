public class Version3 extends Version1 {
	public int[][] g; 
	private boolean checkBuild;
	
	
	public Version3(CensusData cd, int a, int b) {
		super(cd, a, b);
		checkBuild = false;
		g = null;
	}
	
	//Grid census data use karre che
	public void gridBuilding() {
		g = gridBuildingStepOne(cd);
		checkBuild = true;
	}
	
	private int[][] gridBuildingStepOne(CensusData cd) {
		int[][] tg = new int[ga][gb];
		float cdis = (c[2] - c[0]) / ga;  
		float rdis = (c[3] - c[1]) / gb;  
		int a, b;
		for(int i = 0; i < cd.data_size; i++) {
			a = (int) Math.floor((cd.data[i].longitude - c[0]) / cdis);
			b = (int) Math.floor((cd.data[i].latitude - c[1]) / rdis);
			if (a == gb && b == ga)
				tg[a - 1][b - 1] += cd.data[i].population;
			else if (a == ga)
				tg[a - 1][b] += cd.data[i].population;
			else if (b == gb)
				tg[a][b - 1] += cd.data[i].population;
			else
				tg[a][b] += cd.data[i].population;
		}
		gridBuildingStepTwo(tg);
		return tg;
	}
	
	public static void gridBuildingStepTwo(int[][] gridOne) {
		for (int i = 1; i < gridOne[0].length; i++)
			gridOne[0][i] += gridOne[0][i - 1];
		for (int j = 1; j < gridOne.length; j++)
			gridOne[j][0] += gridOne[j - 1][0];
		for (int k = 1; k < gridOne[0].length; k++) {
			for (int l = 1; l < gridOne.length; l++)
				gridOne[l][k] += gridOne[l - 1][k] + gridOne[l][k - 1] - gridOne[l - 1][k - 1];
		}
	}
	
	//Tottal Population anne population calculate karre che
	public int[] gridPopulation(int w, int s, int e, int n) {
		if (!checkBuild)
			gridBuilding();
		if (!valid(w, s, e, n))
			throw new IllegalArgumentException("Invalid input");
		
		int[] res = new int[2];
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

		res[0] = g[e - 1][n - 1] - tl - br + ll;
		res[1] = g[ga - 1][gb - 1];
		return res;
	}
}