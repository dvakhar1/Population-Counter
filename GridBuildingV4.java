import java.util.concurrent.*;


public class GridBuildingV4 extends RecursiveAction {
	public static final int SEQUENTIAL_CUTOFF = 57500;
	public ParallelGridBuild values; 
	public int low, high;  
	public int[][] g;  
	
	//constructor that initializes the variables
	
	public GridBuildingV4(ParallelGridBuild v, int l, int h) {
		values = v;
		low = l;
		high = h;
		g = new int[v.ga][v.gb];
	}
	
	//building the grid using fork join
	public void compute() {
		if (high - low < SEQUENTIAL_CUTOFF) {
			float col_dis = (values.east - values.west) / values.ga; // interval of the column of the grids
			float row_dis = (values.north - values.south) / values.gb; // interval of the row of the grids
			int a, b;
			for(int i = low; i < high; i++) {
				a = (int) Math.floor((values.cGroup[i].longitude - values.west) / col_dis);
				b = (int) Math.floor((values.cGroup[i].latitude - values.south) / row_dis);
				if (a == values.ga && b == values.gb)
					g[a - 1][b - 1] += values.cGroup[i].population;
				else if (a == values.ga)
					g[a - 1][b] += values.cGroup[i].population;
				else if (b == values.gb)
					g[a][b - 1] += values.cGroup[i].population;
				else
					g[a][b] += values.cGroup[i].population;
			}
		} else {
			GridBuildingV4 left = new GridBuildingV4(values, low, (low + high) / 2);
			GridBuildingV4 right = new GridBuildingV4(values, (low + high) / 2, high);
			left.fork();
			right.compute();
			left.join();
			g = GridJoiningV4.combine(left.g, right.g);
		}
	}
}