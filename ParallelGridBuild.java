
public class ParallelGridBuild {
	public CensusGroup[] cGroup;  
	public int ga, gb;  
	public float west, south, east, north;
	
	
	public ParallelGridBuild(CensusGroup[] cg, int a, int b, float w, float s, float e, float n) {
		cGroup = cg;
		ga = a;
		gb = b;
		west = w;
		south = s;
		east = e;
		north = n;
	}
}