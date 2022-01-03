import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gard {
	public static class Interval implements Comparable<Interval> {
		public int start;
		public int end;
		
		public Interval(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean contains(Interval other) {
			if (other.start >= this.start && other.end <= this.end) {
				return true;
			}
			
			return false;
		}

		@Override
		public int compareTo(Interval other) {
			if (this.start == other.start) {
				// descending end
				return other.end - this.end;
			}
			
			// ascending start
			return this.start - other.start;
		}
	}

	public static int n;
	public static List<Interval> intervals = new ArrayList<>();
	
	public static void readInput() {
		try {
			BufferedReader reader;
			reader = Files.newBufferedReader(Paths.get("gard.in"));

			n = Integer.parseInt(reader.readLine());

			for (int i = 0; i < n; i++) {
				String[] input = reader.readLine().split(" ");

				int start = Integer.parseInt(input[0]);
				int end = Integer.parseInt(input[1]);
				
				intervals.add(new Interval(start, end));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int solve() {
		Collections.sort(intervals); // merge sort O(n*log(n))
		
		Interval refInterval = intervals.get(0); // reference
		
		int uselessIntervals = 0;
		
		for (Interval currInterval : intervals) {
			if (refInterval == currInterval) {
				// first interval
				continue;
			}
			
			if (refInterval.contains(currInterval)) {
				// currInterval is useless
				uselessIntervals++;
			} else {
				// update the reference interval
				refInterval = currInterval;
			}
		}
		
		return uselessIntervals;
	}
	
	public static void printOutput() {
		try {
			System.setOut(new PrintStream("gard.out"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(solve());
	}

	public static void main(String[] args) {
		readInput();
		printOutput();
	}
}