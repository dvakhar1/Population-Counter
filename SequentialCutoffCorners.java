import java.util.concurrent.*;


public class  SequentialCutoffCorners extends RecursiveAction {
	public static final ForkJoinPool fj = new ForkJoinPool();
	public int cutOff; 
	public CensusData cd; 
	public int low, high; 
	public float[] c; 
	
	
	public SequentialCutoffCorners(CensusData cd, int l, int h, int cf) {
		this.cd = cd;
		low = l;
		high = h;
		cutOff = cf;
		c = new float[4];
	}
	

	public void compute() {
		if (high - low < cutOff) {
			float west = cd.data[low].longitude;
			float south = cd.data[low].latitude;
			float east = west;
			float north = south;
			for (int i = low + 1; i < high; i++) {
				if (cd.data[i].longitude < west)
					west = cd.data[i].longitude;
				if (cd.data[i].longitude > east)
					east = cd.data[i].longitude;
				if (cd.data[i].latitude < south)
					south = cd.data[i].latitude;
				if (cd.data[i].latitude > north)
					north = cd.data[i].latitude;
			}
			c = new float[]{west, south, east, north};
		} else {
			SequentialCutoffCorners left = new SequentialCutoffCorners(cd, low, (low + high) / 2, cutOff);
			SequentialCutoffCorners right = new SequentialCutoffCorners(cd, (low + high) / 2, high, cutOff);
			left.fork();
			right.compute();
			left.join();
			for(int i = 0; i < 4; i++) {
				if (i < 2)
					c[i] = Math.min(left.c[i], right.c[i]);
				else
					c[i] = Math.max(left.c[i], right.c[i]);
			}
		}
	}
	
	public static float[] SequentialCutOffFindCorners(CensusData cd, int c) {
		SequentialCutoffCorners fc = new SequentialCutoffCorners(cd, 0, cd.data_size, c);
		fj.invoke(fc);
		return fc.c;
	}
}