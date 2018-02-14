public class Version5 extends Version4 {
	
	
	public Version5(CensusData cd, int a, int b) {
		super(cd, a, b);
	}
	
	
	public void gridBuilding() {
		
		ParallelGridBuild vals = new ParallelGridBuild(cd.data, ga, gb, c[0], c[1], c[2], c[3]);
		int[][] tg = new int[ga][gb];  
		Integer[][] gl = new Integer[ga][gb];
		for (int i = 0; i < gl.length; i++) {
			for (int j = 0; j < gl[0].length; j++)
				gl[i][j] = 0;
		}
		
		final int numofthreads = 4;  
		GridBuildingV5 sgb[] = new GridBuildingV5[numofthreads];
		
		for (int i = 0; i < numofthreads - 1; i++) {
            sgb[i] = new GridBuildingV5(vals, i * (cd.data_size /  numofthreads),  
            		(i + 1) * (cd.data_size / numofthreads), tg, gl);
            sgb[i].start();
        }
		
        sgb[numofthreads - 1] = new GridBuildingV5(vals, (numofthreads - 1) * (cd.data_size / numofthreads),  
        		cd.data_size, tg, gl);
        sgb[numofthreads-1].start();
        
        for (int i = 0; i < numofthreads; i++) {
            try {
				sgb[i].join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
        }
        
		Version3.gridBuildingStepTwo(tg); 
		g = tg;
		checkBuild = true;
	}
}