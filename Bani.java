import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Bani {
	public static final int resMod = (int) Math.pow(10, 9) + 7;
	
	// bills: 10, 50, 100, 200, 500
	public static final int billTypes = 5;
	
	public static int n;
	public static int type;
	
	public static void readInput() {
		try {
			BufferedReader reader;
			reader = Files.newBufferedReader(Paths.get("bani.in"));
			
			String[] input = reader.readLine().split(" ");

			type = Integer.parseInt(input[0]);
			n = Integer.parseInt(input[1]);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static long modSum(long a, long b) {
		return (a + b) % resMod;
	}

	public static long modPow(long base, long exponent) {
		long result = 1;

		// just to make sure
		base %= resMod;

		while (exponent > 0) {
			if (exponent % 2 == 1) {
				// odd exponent, multiply the result with the base
				result = (result * base) % resMod;
			}

			// regardless, split the exponent and square up the base
			exponent /= 2;
			base = (base * base) % resMod;
		}

		return result;
	}
	
	public static long solve() {
		if (type == 1) {
			// each type of bill has 2 possible successors
			// compute 'billTypes * 2^(n - 1)'; apply modulo when necessary

			return (long) (billTypes * modPow(2, n - 1)) % resMod;
		}
		
		/*
		 *  dp[i][j] == number of solutions of size 'j + 1' starting with bill
		 *  'i'
		 */
		long[][] dp = new long[billTypes][n];
	
		for (int i = 0; i < billTypes; i++) {
			// exactly one solution for size 1 (0 + 1)
			dp[i][0] = 1;
		}
		
		for (int j = 1; j < n; j++) {
			// for every size

			/*
			 *  let X(Y) mean BILL(ITS INDEX); e.g. 10(0) => Bill 10 is at
			 *  index 0
			 */
			
			// 10(0) has successors 50(1) and 100(2)
			dp[0][j] = modSum(dp[1][j - 1], dp[2][j - 1]);
			
			// 50(1) has successors 10(0) and 200(3)
			dp[1][j] = modSum(dp[0][j - 1], dp[3][j - 1]);
			
			// 100(2) has successors 10(0) and 100(2)
			dp[2][j] = modSum(dp[0][j - 1], dp[2][j - 1]);
			
			// 200(3) has successors 50(1), 100(2) ...
			dp[3][j] = modSum(dp[1][j - 1], dp[2][j - 1]);
			// ... and 500(4)
			dp[3][j] = modSum(dp[3][j], dp[4][j - 1]);
			
			// 500(4) has successors 10(0) and 200(3)
			dp[4][j] = modSum(dp[0][j - 1], dp[3][j - 1]);
		}
		
		long result = 0;
		// the result is the sum of the last row
		for (int i = 0; i < billTypes; i++) {
			result = modSum(result, dp[i][n - 1]);
		}
		
		return result;
	}
	
	public static void printOutput() {
		try {
			System.setOut(new PrintStream("bani.out"));
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