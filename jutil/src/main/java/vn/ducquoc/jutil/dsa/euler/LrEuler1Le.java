package vn.ducquoc.jutil.dsa.euler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LR: LeftRight, LoanRepayment, LateRotation, etc... <br>
 * </br> Maven run example:
 * 
 * <pre>
 * <b>
 * mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=vn.ducquoc.jutil.dsa.euler.LrEuler1Mp
 * </b>
 * </pre>
 * 
 * @author ducquoc
 * @see vn.ducquoc.jutil.MatrixUtil
 * @see vn.ducquoc.jutil.dsa.Pair
 * @see vn.ducquoc.jutil.dsa.CHashMap
 * @see <a href="https://github.com/ducquoc/euler-fun/tree/master/euler"
 *      target="_top">//github.com/ducquoc/euler-fun/tree/master/euler</a>
 */
@SuppressWarnings("unused")
public class LrEuler1Le {

  public static String FILENAME_LR1 = "LrEuler1.txt";
  public static String FILENAME_LR1_MIN = "LrEuler0.txt";
  public static String FILENAME_LR1_MED = "LrEuler1_Medium.txt";
  public static String FILENAME_LR1_MEDBIG = "LrEuler1_MedBig.txt";
  public static String FILENAME_LR1_BIG = "LrEuler1_Big.txt";

  public static int MAX_N = 10000000;// simple enough, no need long
  public static int MAX_S = 100000;
  public static int MAX_L = 100000;

  // may encapsulate in LrEuler1Bean to avoid static, KISS YAGNI
  public static int N;// square size, 1-based index
  public static int S;// commands list size
  public static int L;// queries list size

  public static int[] w;// array of values (typically 0 -> N^2-1)
  public static int[] flattenS;// flatten (1D) likely faster and less memory
  public static int[] targets;// values to find
  // prefer simple array over Tuple approach (Pair, Triple, ...)
  public static int[][] m;// 2d array as matrix of N (w)
  public static int[][] subM;// 2d array sub matrix of N (w)
  public static int[] flattenSubM;// flatten or sparseMatrix is faster
  public static Map<Integer, String> finalPos = new LinkedHashMap<Integer, String>();


  public static void main(String[] args) {

    long stopWatchStart = System.nanoTime();
    doLessEngineering(FILENAME_LR1);
    double milliSecs = (System.nanoTime() - stopWatchStart) / 1000000.0;
    System.out.printf("*** Solved S.I M.P L.E in %f ms \n", milliSecs);

  }


  public static void doStraightImmediately(String filename) { // S.I
    doMorePragmatic(filename);
  }

  public static void doMorePragmatic(String filename) { // M.P
    doLessEngineering(filename);
  }

  public static void doLessEngineering(String filename) { // L.E

    String line = "";
    try {// prefer DataInputStream over FileInputStream/FileReader (faster)
      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
      BufferedReader buff = new BufferedReader(new InputStreamReader(new DataInputStream(is)));

      line = readNextNotEmptyLine(buff);
      N = Integer.valueOf(line.trim());
      validateInput(N, S, L);

      line = readNextNotEmptyLine(buff);
      S = Integer.valueOf(line.trim());
      validateInput(N, S, L);
      flattenS = new int[3*S];
      for (int i = 0; i < 3*S; i=i+3) {
        line = readNextNotEmptyLine(buff);
        String[] splitS = line.trim().split("\\s");
        flattenS[i] = Integer.valueOf(splitS[0]);
        flattenS[i+1] = Integer.valueOf(splitS[1]);
        flattenS[i+2] = Integer.valueOf(splitS[2]);
      }

      line = readNextNotEmptyLine(buff);
      L = Integer.valueOf(line.trim());
      validateInput(N, S, L);
      targets = new int[L];
      for (int i = 0; i < L; i++) {
        line = readNextNotEmptyLine(buff);
        targets[i] = Integer.valueOf(line.trim());
      }

      buff.close();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }

    boolean fast1D = (N < 45161);// Math.sqrt(Integer.MAX_VALUE - 8);
    if (fast1D) {
      w = new int[N * N];
      for (int i = 0; i < N * N; i++) {
        w[i] = i;
      }
    } else { // -Xmx13g
      m = new int[N][N];//new HashMap<Integer, Pair<Integer,Integer>>();
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          m[i][j] = i * N + j;// w[i * N + j];
        }
      }
    }

    for (int i = 0; i < 3 * S; i = i + 3) {
      if (fast1D) {
        rotate1D(flattenS[i], flattenS[i + 1], flattenS[i + 2]);
      } else {
        rotate2D(flattenS[i], flattenS[i + 1], flattenS[i + 2]);
      }
    }

//    flattenS = null;// free memory for GC

    // printFinalPosition
    for (int target : targets) {
      if (fast1D) {
        int i = indexOf(w, target);
        int rowIdx = indexOfRow2DFrom1D(i, w);
        int colIdx = indexOfCol2DFrom1D(i, w);
        // finalPos.put(target, va(1+rowIdx) + " " + (1+colIdx));
        System.out.println((1 + rowIdx) + " " + (1 + colIdx));
      } else {
        for (int i = 0; i < N; i++) {
          for (int j = 0; j < N; j++) {
            if (m[i][j] == target) {
              //finalPos.put(target, (i + 1) + " " + (j + 1));
              System.out.println((1 + i) + " " + (1 + j));
            }
          }
        }
      }
    }

  }


  private static String readNextNotEmptyLine(BufferedReader buff)
      throws IOException {
    String line = buff.readLine();// readNextNotEmptyLine
    while (line == null || line.trim().length() == 0) {
      line = buff.readLine();
    }
    return line;
  }

  private static void validateInput(int N, int S, int L) {
    if (N < 0 || N > MAX_N || S < 0 || S > MAX_S || L < 0 || L > MAX_L) {
      throw new IllegalArgumentException("Invalid input!");
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

  public static void rotate1D(int ai, int bi, int di) {
    int aIndex = ai - 1;
    int bIndex = bi - 1;

    subM = new int[1 + di][1 + di];// int dIndex = 1+di;
    for (int i = aIndex; i < aIndex + 1 + di; i++) {
      for (int j = bIndex; j < bIndex + 1 + di; j++) {
        subM[i - aIndex][j - bIndex] = w[i*N+j];//m[i][j];
      }
    }
    rotateRight90(subM, 1 + di);
    for (int i = aIndex; i < aIndex + 1 + di; i++) {
      for (int j = bIndex; j < bIndex + 1 + di; j++) {
        w[i*N+j] = subM[i - aIndex][j - bIndex];//m[i][j];
      }
    }
//    subM = null;// free memory for GC
  }

  public static void rotate2D(int ai, int bi, int di) {
    int aIndex = ai - 1;
    int bIndex = bi - 1;

    subM = new int[1 + di][1 + di];// int dIndex = 1+di;
    for (int i = aIndex; i < aIndex + 1 + di; i++) {
      for (int j = bIndex; j < bIndex + 1 + di; j++) {
        subM[i - aIndex][j - bIndex] = m[i][j];//rotatedW[i*N+j];
      }
    }
    rotateRight90(subM, 1 + di);
    for (int i = aIndex; i < aIndex + 1 + di; i++) {
      for (int j = bIndex; j < bIndex + 1 + di; j++) {
        m[i][j] = subM[i - aIndex][j - bIndex];//rotatedW[i*N+j];
      }
    }
//    subM = null;// free memory for GC
  }

  //
  // extra helpers
  //
  public static void rotateRight90(int[][] m, int N) {
    swapRows(m, N);
    transpose(m, N);
  }

  public static void rotateLeft90(int[][] m, int N) {
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

  public static void rotateClockwise90(int[][] m, int N) {
    swapRows(m, N);
    transpose(m, N);
  }

  public static void rotateCounterClockwise90(int[][] m, int N) {
    transpose(m, N);
    swapRows(m, N);
  }

  public static void swapRows(int[][] m, int N) {
    for (int i = 0, k = N - 1; i < k; ++i, --k) {
      int[] x = m[i];
      m[i] = m[k];
      m[k] = x;
    }
  }

  public static void transpose(int[][] m, int N) {
    for (int i = 0; i < N; i++) {
      for (int j = i; j < N; j++) {
        int x = m[i][j];
        m[i][j] = m[j][i];
        m[j][i] = x;
      }
    }
  }

}
