import java.util.concurrent.*;

public class GridJoiningV4 extends RecursiveAction {
	public static final ForkJoinPool fj = new ForkJoinPool();
	public static final int SEQUENTIAL_CUTOFF = 25;
	int[][] lg, rg, res; 
	int xlow, xhigh;  
	int ylow, yhigh;  
	
	public GridJoiningV4(int[][] lg, int[][] rg, int xl, int xh, int yl, int yh, int[][] r){
		this.lg = lg;
		this.rg = rg;
		xlow = xl;
		xhigh = xh;
		ylow = yl;
		yhigh = yh;
		res = r;
	}
	
	public void compute() {
		if (xhigh - xlow < SEQUENTIAL_CUTOFF && yhigh - ylow < SEQUENTIAL_CUTOFF) {
			for (int i = ylow; i < yhigh; i++) {
				for (int j = xlow; j < xhigh; j++)
					res[j][i] += lg[j][i] + rg[j][i];
			}
		} else {
			GridJoiningV4 lowerLeft = new GridJoiningV4(lg, rg, xlow, (xlow + xhigh) / 2, 
					ylow, (ylow + yhigh) / 2, res);
			GridJoiningV4 lowerRight = new GridJoiningV4(lg, rg, xlow, (xlow + xhigh) / 2, 
					(ylow + yhigh) / 2, yhigh, res);
			GridJoiningV4 upperLeft = new GridJoiningV4(lg, rg, (xlow + xhigh) / 2, xhigh, 
					ylow, (ylow + yhigh) / 2, res);
			GridJoiningV4 upperRight = new GridJoiningV4(lg, rg, (xlow + xhigh) / 2, xhigh, 
					(ylow + yhigh) / 2, yhigh, res);
			lowerLeft.fork();
			lowerRight.compute();
			upperLeft.compute();
			upperRight.compute();
			lowerLeft.join();
		}
	}
	
	public static int[][] combine(int[][] lg, int[][] rg) {
		int[][] r = new int[lg.length][lg[0].length];
		GridJoiningV4 t = new GridJoiningV4(lg, rg, 0, lg.length, 0, lg[0].length, r);
		fj.invoke(t);
		return t.res;
	}
}