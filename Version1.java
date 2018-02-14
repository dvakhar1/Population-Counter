public class Version1 implements TotalPopulation {
	CensusData cd;
	int ga; 
	int gb; 
	float[] c; 
	
	
	public Version1(CensusData cd, int a, int b) {
		this.cd = cd;
		ga = a;
		gb = b;
		c = Corners(cd);
	}
	
	private float[] Corners(CensusData cd) {
		float west = 190;
		float south = 80;
		float east = -190;
		float north = -80;
		for (int i = 0; i < cd.data_size; i++) {
			float lat = cd.data[i].latitude;
			float lon = cd.data[i].longitude;
			if (lat < south)
				south = lat;
			if (lat > north)
				north = lat;
			if (lon < west)
				west = lon;
			if (lon > east)
				east = lon;
		}
		return new float[]{west, south, east, north};
	}
	
	
	public int[] gridPopulation(int w, int s, int e, int n) {
		if (!valid(w, s, e, n))
			throw new IllegalArgumentException("wrong input");
		int[] result = new int[2];
		float cdis = (c[2] - c[0]) / ga; //  column 
		float rdis = (c[3] - c[1]) / gb; //  row 

		for (int i = 0; i < cd.data_size; i++) {
			float x = (cd.data[i].longitude - c[0]) / cdis + 1;
			float y = (cd.data[i].latitude - c[1]) / rdis + 1;
			if (inside(x, y, w, s, e, n))
				result[0] += cd.data[i].population;
			result[1] += cd.data[i].population;
		}
		return result;
	}
	
	public boolean valid(int w, int s, int e, int n) {
		return (w > 0 && s > 0 && (e >= w && e <= ga) && (n >= s && n <= gb));
	}
	
	public boolean inside(float x, float y, int w, int s, int e, int n) {
		return ((x >= w && x < e + 1 && y >= s && y < n + 1) || 
				(x == e + 1 && x == ga + 1 && y >= s && y < n + 1) ||
				(y == n + 1 && y == gb + 1 && x >= w && x < e + 1) || 
				(x == e + 1 && x == ga + 1 && y == n + 1 && y == gb + 1));
	}
}