import java.util.concurrent.*;


public class SequentialCutOffGridBuilding extends RecursiveAction {
	public int cutOff; 
	public ParallelGridBuild v; 
	public int low, high;  
	public int[][] g;  
	
	
	public SequentialCutOffGridBuilding(ParallelGridBuild v, int l, int h, int c) {
		this.v = v;
		low = l;
		high = h;
		cutOff = c;
		g = new int[v.ga][v.gb];
	}
	
	
	public void compute() {
		if (high - low < cutOff) {
			float col_dis = (v.east - v.west) / v.ga; 
			float row_dis = (v.north - v.south) / v.gb; 
			int x, y;
			for(int i = low; i < high; i++) {
				x = (int) Math.floor((v.cGroup[i].longitude - v.west) / col_dis);
				y = (int) Math.floor((v.cGroup[i].latitude - v.south) / row_dis);
				if (x == v.ga && y == v.gb)
					g[x - 1][y - 1] += v.cGroup[i].population;
				else if (x == v.ga)
					g[x - 1][y] += v.cGroup[i].population;
				else if (y == v.gb)
					g[x][y - 1] += v.cGroup[i].population;
				else
					g[x][y] += v.cGroup[i].population;
			}
		} else {
			SequentialCutOffGridBuilding left = new SequentialCutOffGridBuilding(v, low, (low + high) / 2, cutOff);
			SequentialCutOffGridBuilding right = new SequentialCutOffGridBuilding(v, (low + high) / 2, high, cutOff);
			left.fork();
			right.compute();
			left.join();
			for (int i = 0; i < v.gb; i++) {
				for (int j = 0; j < v.ga; j++)
					g[j][i] += left.g[j][i] + right.g[j][i];
			}
		}
	}
}