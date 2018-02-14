import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PopulationQuery {
	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;
	static long start;
	static long end;
	
	// parse the input file into a large array held in a CensusData object
	public static CensusData parse(String filename) {
		CensusData result = new CensusData();
		
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line

            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0)
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
            }

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
	}

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
	// argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		if (args.length != 4) {
			 System.err.println("Please enter 4 Arguements Wrong number of arguments");
	         System.exit(1);
		}
		
		int a = 0, b = 0, k = 0;
		
		
		try {
			a = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		
		try {
			b = Integer.parseInt(args[2]);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		
		if (a < 1 || b < 1)
			throw new IllegalArgumentException();
		
		CensusData cd = parse(args[0]);
		TotalPopulation tp = null;

		if (k == args[3].compareTo("-v1"))
			tp = new Version1(cd, a, b);
		else if (k == args[3].compareTo("-v2"))
			tp = new Version2(cd, a, b);
		else if (k == args[3].compareTo("-v3")) {
			Version3 ss = new Version3(cd, a, b);
			ss.gridBuilding();
			tp = ss;
		}
		else if (k == args[3].compareTo("-v4")) {
			Version4 sp = new Version4(cd, a, b);
			sp.gridBuilding();
			tp = sp;
		} 
		else if (k == args[3].compareTo("-v5")) {
			Version5 slb = new Version5(cd, a, b);
			slb.gridBuilding();
			tp = slb;
		}
		else
			throw new UnsupportedOperationException();
		
		int[] sides = new int [4];
		int[] res = new int[2];
		Scanner reader = new Scanner(System.in);
		
		while (true) {
			System.out.println("Please give west, south, east, north coordinates of your query rectangle:");
			String input = reader.nextLine();
start = System.currentTimeMillis();

			String[] literals = input.split(" ");
			if (literals.length != 4) {
				reader.close();
				System.exit(1);
			}
			for (int i = 0; i < literals.length; i++) {
				try {
					sides[i] = Integer.parseInt(literals[i]);
				} catch(NumberFormatException e) {
					reader.close();
					System.exit(1);
				}
			}
			
			try {
				res = tp.gridPopulation(sides[0], sides[1], sides[2], sides[3]);
			} catch(IllegalArgumentException e) {
				reader.close();
				System.exit(1);
			}
end = System.currentTimeMillis();
			System.out.println("Population of rectangle: " + res[0]);
			System.out.format("Percent of total population: %.2f", Math.round(10000.0 * res[0] / res[1]) / 100.0);
System.out.println();
System.out.println("Time Elapsed: " + (end - start));
			
		}
	}
public static Pair<Integer, Float> singleInteraction(int w, int s, int e, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void preprocess(String filename, int columns, int rows, int versionNum) {
		// TODO Auto-generated method stub
		
	}

}