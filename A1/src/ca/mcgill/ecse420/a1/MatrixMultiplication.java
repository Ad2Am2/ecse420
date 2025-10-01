package ca.mcgill.ecse420.a1;

public class MatrixMultiplication {
	
	private static int NUMBER_THREADS = 16;  // Best performing from 1.4
	private static int MATRIX_SIZE = 4000;

        public static void main(String[] args) {
		
		// Test different matrix sizes
		int[] matrixSizes = {100, 200, 500, 1000, 2000, 3000, 4000};
		
		System.out.println("Running experiments with varying matrix sizes");
		System.out.println("Using " + NUMBER_THREADS + " threads for parallel execution");
		System.out.println("========================================\n");
		
		for (int size : matrixSizes) {
			MATRIX_SIZE = size;
			
			// Generate matrices for this size
			double[][] a = generateRandomMatrix(size, size);
			double[][] b = generateRandomMatrix(size, size);
			
			// Measure sequential time
			long startTime = System.currentTimeMillis();
			double[][] seqResult = sequentialMultiplyMatrix(a, b);
			long seqTime = System.currentTimeMillis() - startTime;
			
			// Measure parallel time
			startTime = System.currentTimeMillis();
			double[][] parResult = parallelMultiplyMatrix(a, b);
			long parTime = System.currentTimeMillis() - startTime;
			
			double speedup = (double) seqTime / parTime;
			System.out.println("Matrix size: " + size + "x" + size);
			System.out.println("Sequential time: " + seqTime + " ms");
			System.out.println("Parallel time: " + parTime + " ms");
			System.out.println("Speedup: " + String.format("%.2f", speedup) + "x\n");
		}
	}
	
	/**
	 * Measures and compares execution time for sequential and parallel matrix multiplication
	 * @param a is the first matrix
	 * @param b is the second matrix
	 */
	public static void measureExecutionTime(double[][] a, double[][] b) {
		// Test sequential multiplication
		long startTime = System.currentTimeMillis();
		double[][] seqResult = sequentialMultiplyMatrix(a, b);
		long seqTime = System.currentTimeMillis() - startTime;
		
		// Test parallel multiplication
		startTime = System.currentTimeMillis();
		double[][] parResult = parallelMultiplyMatrix(a, b);
		long parTime = System.currentTimeMillis() - startTime;
		
		// Display results
		System.out.println("Matrix size: " + MATRIX_SIZE + "x" + MATRIX_SIZE);
		System.out.println("Number of threads: " + NUMBER_THREADS);
		System.out.println("Sequential time: " + seqTime + " ms");
		System.out.println("Parallel time: " + parTime + " ms");
		System.out.println("Speedup: " + String.format("%.2f", (double) seqTime / parTime) + "x");
		
		// Validate correctness by comparing results
		boolean correct = true;
		for (int i = 0; i < seqResult.length && correct; i++) {
			for (int j = 0; j < seqResult[0].length && correct; j++) {
				if (Math.abs(seqResult[i][j] - parResult[i][j]) > 0.0001) {
					correct = false;
				}
			}
		}
		System.out.println("Results match: " + correct);
	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		int rowsA = a.length;
		int colsA = a[0].length;
		int colsB = b[0].length;
		
		double[][] result = new double[rowsA][colsB];
		
		// Standard triple-loop matrix multiplication
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				for (int k = 0; k < colsA; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
        public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		int rowsA = a.length;
		int colsA = a[0].length;
		int colsB = b[0].length;
		
		double[][] result = new double[rowsA][colsB];
		Thread[] threads = new Thread[NUMBER_THREADS];
		
		// Divide rows among threads
		int rowsPerThread = rowsA / NUMBER_THREADS;
		
		for (int i = 0; i < NUMBER_THREADS; i++) {
			final int startRow = i * rowsPerThread;
			final int endRow = (i == NUMBER_THREADS - 1) ? rowsA : (i + 1) * rowsPerThread;
			
			threads[i] = new Thread(() -> {
				// Each thread computes a subset of rows
				for (int row = startRow; row < endRow; row++) {
					for (int col = 0; col < colsB; col++) {
						for (int k = 0; k < colsA; k++) {
							result[row][col] += a[row][k] * b[k][col];
						}
					}
				}
			});
			
			threads[i].start();
		}
		
		// Wait for all threads to complete
		try {
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return result;
	}
        
        /**
         * Populates a matrix of given size with randomly generated integers between 0-10.
         * @param numRows number of rows
         * @param numCols number of cols
         * @return matrix
         */
        private static double[][] generateRandomMatrix (int numRows, int numCols) {
             double matrix[][] = new double[numRows][numCols];
        for (int row = 0 ; row < numRows ; row++ ) {
            for (int col = 0 ; col < numCols ; col++ ) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }
	
}
