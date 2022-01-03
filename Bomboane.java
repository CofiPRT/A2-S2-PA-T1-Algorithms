import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Bomboane {
	public static class Student {
		public int start;
		public int end;
		
		public Student(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean contains(int num) {
			return num >= start && num <= end;
		}
	}

	public static final int resMod = (int) Math.pow(10, 9) + 7;

	public static int n;
	public static int m;
	public static List<Student> students = new ArrayList<>();
	
	public static void readInput() {
		try {
			BufferedReader reader;
			reader = Files.newBufferedReader(Paths.get("bomboane.in"));
			
			String[] input = reader.readLine().split(" ");
			n = Integer.parseInt(input[0]);
			m = Integer.parseInt(input[1]);
			
			for (int i = 0; i < n; i++) {
				input = reader.readLine().split(" ");

				int start = Integer.parseInt(input[0]);
				int end = Integer.parseInt(input[1]);
				
				students.add(new Student(start, end));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static long modSum(long a, long b) {
		return (a + b) % resMod;
	}

	public static long solve() {
		/*
		 *  dp[i][j] == in how many ways can we give the first i+1 students
		 *  j pieces of candy
		 *  therefore, dp[n - 1][m] stores the result to the initial problem
		 */
		long[][] dp = new long[n][m + 1];
		
		/*
		 *  initialization: having j pieces of candy, in how many ways can we
		 *  give them to the first student?
		 *
		 *  if j is in their interval, a single way: give them all j pieces
		 *  of candy
		 */
		Student firstStud = students.get(0);
		for (int j = 0; j < m + 1; j++) {
			if (firstStud.contains(j)) {
				dp[0][j] = 1; // a single way
			} else {
				dp[0][j] = 0;
			}
		}
		
		// fill the rest with zeros
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < m + 1; j++) {
				dp[i][j] = 0;
			}
		}
		
		for (int i = 1; i < n; i++) {
			// for every student after the first one
			Student student = students.get(i);
			
			for (int j = student.start; j <= student.end; j++) {
				/*
				 *  for every possible value for the pieces of candy they can
				 *  receive
				 */
				for (int k = j; k < m + 1; k++) {
					/*
					 *  in how many ways can we give k pieces of candy to this
					 *  student supposing we gave k-j to the previous?
					 *  
					 *  we keep adding (+=) the answer because it will possibly
					 *  build up depending on all the possible pieces of candy
					 *  this student can receive (j)
					 */
					dp[i][k] = modSum(dp[i][k], dp[i - 1][k - j]);
				}
			}
		}
		
		return dp[n - 1][m];
	}
	
	public static void printOutput() {
		try {
			System.setOut(new PrintStream("bomboane.out"));
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