import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Sala {
	public static class Dumbbell implements Comparable<Dumbbell> {
		public long weight;
		public long reps;
		
		public Dumbbell(long weight, long reps) {
			this.weight = weight;
			this.reps = reps;
		}

		@Override
		public int compareTo(Dumbbell other) {
			return (int) (other.weight - this.weight);
		}
		
		public long getReps() {
			return reps;
		}
	}
	
	public static class MyPriorityQueue {
		public Queue<Dumbbell> queue;
		public int maxSize = 0;
		// O(1) access of the sum of the reps in the queue
		public long repSum = 0;
		
		public MyPriorityQueue(int maxSize, Comparator<Dumbbell> comparator) {
			queue = new PriorityQueue<>(comparator);
			this.maxSize = maxSize;
		}
		
		/*
		 *  returns true if the dumbbell has more reps than
		 *  the first element of the queue
		 */
		public boolean hasBetterReps(Dumbbell dumbbell) {
			if (queue.isEmpty()) {
				return true;
			}
			
			if (dumbbell.reps > queue.element().reps) {
				return true;
			}
			
			return false;
		}
		
		/*
		 *  returns true if there is still space in this queue
		 */
		public boolean hasSpace() {
			if (queue.size() < maxSize) {
				return true;
			}
			
			return false;
		}
		
		/*
		 *  adds an element to the queue if there is space;
		 *  also updates the repsum
		 */
		public boolean offer(Dumbbell dumbbell) {
			if (!hasSpace()) {
				// can't add
				return false;
			}
			
			queue.offer(dumbbell);
			
			// update the repSum
			repSum += dumbbell.reps;
			
			return true;
		}
		
		/*
		 *  removes and element from the queue;
		 *  also updates the repsum
		 */
		public Dumbbell poll() {
			if (queue.isEmpty()) {
				return null;
			}
			
			// update the repSum
			repSum -= queue.peek().reps;
			
			return queue.poll();
		}
	}

	public static int n;
	public static int m;
	
	public static List<Dumbbell> dumbbells = new ArrayList<>();
	
	public static void readInput() {
		try {
			BufferedReader reader;
			reader = Files.newBufferedReader(Paths.get("sala.in"));
			
			String[] input = reader.readLine().split(" ");
			n = Integer.parseInt(input[0]);
			m = Integer.parseInt(input[1]);
			
			for (int i = 0; i < n; i++) {
				input = reader.readLine().split(" ");

				int weight = Integer.parseInt(input[0]);
				int reps = Integer.parseInt(input[1]);
				
				dumbbells.add(new Dumbbell(weight, reps));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static long solve() {
		Collections.sort(dumbbells); // merge sort O(n*log(n))
		
		/*
		 *  create a priority queue based on reps (the head will have the least);
		 *  we will use this priority queue to manage the dumbbells that we use
		 *  in the final solution
		 *  we use a priority queue for its O(log(n)) insertion
		 */
		Comparator<Dumbbell> byReps = Comparator.comparing(Dumbbell::getReps);
		MyPriorityQueue queue = new MyPriorityQueue(m, byReps);
		
		long solution = Long.MIN_VALUE; // actual solution
		
		for (Dumbbell dumbbell : dumbbells) {
			// for every dumbbell
			
			if (queue.hasSpace()) {
				// there is still space 
				queue.offer(dumbbell);
				
				/*
				 *  test if the solution is improved; we don't need any
				 *  variables such as minWeight because we sorted the dumbbells
				 *  in the descending order of their weight - therefore, this
				 *  dumbbell has the lowest weight SO FAR
				 *  
				 *  even if it doesn't aid Gigel in impressing girls, we still
				 *  keep the dumbbell into the queue to test it along other
				 *  dumbbells; maybe a certain combination has enough reps in
				 *  order to compensate for the low minimum weight
				 */
				long tempSolution = queue.repSum * dumbbell.weight;
				solution = Math.max(solution, tempSolution);
				
			} else if (queue.hasBetterReps(dumbbell)) {
				/*
				 *  even if it proves to bring Gigel more muscle growth or not,
				 *  this dumbbell has more reps than a dumbbell in the queue;
				 *  there is no reason why we shouldn't use this in the future
				 *  instead
				 */
				
				queue.poll(); // remove the dumbbell with lower reps
				queue.offer(dumbbell); // add this one
				
				// now test whether or not it gives Gigel more muscle growth
				long tempSolution = queue.repSum * dumbbell.weight;
				solution = Math.max(solution, tempSolution);
			}
			
			/*
			 *  if no branch was entered, this dumbbell is literally useless;
			 *  it doesn't give Gigel more muscle growth, it doesn't help him
			 *  impress the girls and doesn't have a single chance to do so in
			 *  the future - skip this abomination of a dumbbell
			 */
		}
		
		return solution;
	}
	
	public static void printOutput() {
		try {
			System.setOut(new PrintStream("sala.out"));
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