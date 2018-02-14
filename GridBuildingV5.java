public class GridBuildingV5 extends java.lang.Thread {
	public ParallelGridBuild vals; 
	public int low, high;  
	public int[][] g;  
	public Integer[][] gl;  
	
	
	public GridBuildingV5(ParallelGridBuild v, int l, int h, int[][] g, Integer[][] gl) {
		vals = v;
		low = l;
		high = h;
		this.g = g;
		this.gl = gl;
	}
	
	
	public void run() {
		float col_dis = (vals.east - vals.west) / vals.ga;  
		float row_dis = (vals.north - vals.south) / vals.gb;  		int x, y;
		for(int i = low; i < high; i++) {
			x = (int) Math.floor((vals.cGroup[i].longitude - vals.west) / col_dis);
			y = (int) Math.floor((vals.cGroup[i].latitude - vals.south) / row_dis);
			if (x == vals.ga && y == vals.gb) {
				synchronized (gl[x - 1][y - 1]) {
					g[x - 1][y - 1] += vals.cGroup[i].population;
				}
			} else if (x == vals.ga) {
				synchronized (gl[x - 1][y]) {
					g[x - 1][y] += vals.cGroup[i].population;
				}
			} else if (y == vals.gb) {
				synchronized (gl[x][y - 1]) {
					g[x][y - 1] += vals.cGroup[i].population;
				}
			} else {
				synchronized (gl[x][y]) {
					g[x][y] += vals.cGroup[i].population;
				}
			}
		}
	}
}