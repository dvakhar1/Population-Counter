import java.util.concurrent.*;

public class ParallelCorners extends RecursiveAction {
	public static final ForkJoinPool fj = new ForkJoinPool();
	public CensusData cd; 
	public int low, high; 
	public float[] c; 
	
	//Constructor that initializes the variables
	public ParallelCorners(CensusData cd, int l, int h) {
		this.cd = cd;
		low = l;
		high = h;
        c = new float[4];
	}
	
	public void compute() {
		if (high - low == 1) {
			float lat = cd.data[low].latitude;
			float lon = cd.data[low].longitude;
			c = new float[]{lon, lat, lon, lat};
		}
		else {
			ParallelCorners left = new ParallelCorners(cd, low, (low + high) / 2);
			ParallelCorners right = new ParallelCorners(cd, (low + high) / 2, high);
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
	
	public static float[] corners(CensusData cd) {
		ParallelCorners spc = new ParallelCorners(cd, 0, cd.data_size);
		fj.invoke(spc);
		return spc.c;
	}
}