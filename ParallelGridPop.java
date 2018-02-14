import java.util.concurrent.*;


 
public class ParallelGridPop extends RecursiveAction {
	public CensusData cd; 
	public float[] c; 
	public int ga, gb, low, high; 
	public int[] r; 
	public int[] pop;
	
	
	public ParallelGridPop(CensusData cd, float[] c, int[] r, int x, int y, int l, int h) {
		this.cd = cd;
		this.c = c;
		this.r = r;
		ga = x;
		gb = y;
		low = l;
		high = h;
		pop = new int[2];
	}

	
	public boolean grid(float a, float b) {
		return ((a >= r[0] && a < r[2] + 1 && b >= r[1] && b < r[3] + 1) || 
				(a == r[2] + 1 && a == ga + 1 && b >= r[1] && b < r[3] + 1) ||
				(b == r[3] + 1 && b == gb + 1 && a >= r[0] && a < r[2] + 1) || 
				(a == r[2] + 1 && a == ga + 1 && b == r[3] + 1 && b == gb + 1));
	}
	
	
	public void compute() {
		if (high - low == 1) {
			float col_dis = (c[2] - c[0]) / ga; 
			float row_dis = (c[3] - c[1]) / gb; 
			float x = (cd.data[low].longitude - c[0]) / col_dis + 1;
			float y = (cd.data[low].latitude - c[1]) / row_dis + 1;
			if (grid(x, y))
				pop[0] += cd.data[low].population;
			pop[1] += cd.data[low].population;
		} else {
			ParallelGridPop left = new ParallelGridPop(cd, c, r, ga, gb, low, (low + high) / 2);
			ParallelGridPop right = new ParallelGridPop(cd, c, r, ga, gb, (low + high) / 2, high);
			left.fork();
			right.compute();
			left.join();
			pop[0] = left.pop[0] + right.pop[0];
			pop[1] = left.pop[1] + right.pop[1];
		}
	}
}