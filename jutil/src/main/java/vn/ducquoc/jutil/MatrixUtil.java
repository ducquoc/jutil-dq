package vn.ducquoc.jutil;

/**
 * Helper class for Operations on matrix, usually int array (2D or 1D).
 * 
 * @author ducquoc
 * @see Jama.Matrix
 * @see org.apache.commons.lang3.ObjectUtils
 */
public class MatrixUtil {

  //
  // Square matrix, N = rowNum = colNum
  //
  public static void rotateRight90(int[][] m) {// clockwise
    swapRows(m);
    transpose(m);
  }

  public static void swapRows(int[][] m) {
    int N = (int) Math.sqrt(m.length);
    for (int i = 0, k = N - 1; i < k; ++i, --k) {
      int[] x = m[i];
      m[i] = m[k];
      m[k] = x;
    }
  }

  public static void transpose(int[][] m) {// flip
    int N = (int) Math.sqrt(m.length);
    for (int i = 0; i < N; i++) {
      for (int j = i; j < N; j++) {
        int x = m[i][j];
        m[i][j] = m[j][i];
        m[j][i] = x;
      }
    }
  }

  public static void rotateLeft90(int[][] m) {// counter clockwise
    transpose(m);
    swapRows(m);
  }

  public static void rotateCounterClockwise90(int[][] m) {
    int N = (int) Math.sqrt(m.length);
    int tempInt = 0; // no need transpose/swapRows
    for (int i = 0; i < N / 2; i++) {
      for (int j = i; j < N - 1 - i; j++) {
        tempInt = m[i][j];
        m[i][j] = m[j][N - 1 - i];
        m[j][N - 1 - i] = m[N - 1 - i][N - 1 - j];
        m[N - 1 - i][N - 1 - j] = m[N - 1 - j][i];
        m[N - 1 - j][i] = tempInt;
      }
    }
  }

  public static int indexOf(int[] m, int target) {
    //return java.util.Arrays.binarySearch(m, target);//sorted only
    for (int i = 0; i < m.length; i++) {
      if (m[i] == target) return i;
    }
    return -1;
  }

  public static int indexOfRow2DFrom1D(int index1D, int[] m) {
    int N = (int) Math.sqrt(m.length);
    return index1D / N;
  }

  public static int indexOfCol2DFrom1D(int index1D, int[] m) {
    int N = (int) Math.sqrt(m.length);
    return index1D % N;
  }


  //
  // General matrix, rowNum does not necessarily equal to colNum
  //
  public int[][] array1DTo2D(int[] array, int rows, int cols) {
    if (array.length != (rows * cols))
      throw new IllegalArgumentException("Invalid array length");

    int[][] bidi = new int[rows][cols];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(array, (i * cols), bidi[i], 0, cols);
    }

    return bidi;
  }

  public int[] array2DTo1D(final int[][] array) {// flatten
    int rows = array.length;
    int cols = array[0].length;
    int[] mono = new int[(rows * cols)];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(array[i], 0, mono, (i * cols), cols);
    }
    return mono;
  }

    //
    // Java 7+ methods (Eclipse/Maven may require JDK instead of JRE)
    //

}
