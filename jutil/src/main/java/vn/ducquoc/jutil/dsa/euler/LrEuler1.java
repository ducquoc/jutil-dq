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
 * LR: LeftRight, LoanRepayment, LateRotation, etc...
 * 
 * @author ducquoc
 * @see vn.ducquoc.euler.EulerChallenge081
 * @see vn.ducquoc.euler.EulerThread
 */
@SuppressWarnings("unused")
public class LrEuler1 {

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
    public static int[] a;// array rows of w
    public static int[] b;// array cols of w
    public static int[] d;// arrays of offsets

    // prefer simple array over Tuple approach (Pair, Triple, ...)
    public static int[][] m;// 2d array as matrix of N
    public static int[][] abd;// 2d array as matrix of S
    public static int[] targets;// w(i) to find
    public static Map<Integer, String> initPos = new LinkedHashMap<Integer, String>();
    public static Map<Integer, String> finalPos = new LinkedHashMap<Integer, String>();


  public static void main(String[] args) {

    long stopWatchStart = System.nanoTime();
    // doStraightImmediately("LrEuler0.txt");
    doStraightImmediately(FILENAME_LR1);
    double milliSecs = (System.nanoTime() - stopWatchStart) / 1000000.0;
//    System.out.printf("*** Solved S.I M.P L.E in %f ms \n", milliSecs);
  }


  public static void doStraightImmediately(String filename) { // S.I
    loadFileToBean(filename);

    // findInitPosition
    for (int k = 0; k < L; k++) {
      Integer target = targets[k];
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          if (m[i][j] == target) {
            initPos.put(target, (i + 1) + " " + (j + 1));
          }
        }
      }
    }
    // System.out.println("initPos: " + initPos);

    for (int k = 0; k < S; k++) {
      rotate(abd[k][0], abd[k][1], abd[k][2]);
    }

    // findFinalPosition
    for (int k = 0; k < L; k++) {
      Integer target = targets[k];
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          if (m[i][j] == target) {
            finalPos.put(target, (i + 1) + " " + (j + 1));
          }
        }
      }
    }
    // System.out.println("final: " + finalPos);

    // final output
    for (int k = 0; k < L; k++) {
      Integer target = targets[k];
      if (finalPos != null && finalPos.containsKey(target)) {
        System.out.println(finalPos.get(target));
      }
    }
  }

  public static void loadFileToBean(String filepath) {
    String line = "";
    try {// prefer DataInputStream over FileInputStream or FileReader
      // BufferedReader buff = new BufferedReader(new FileReader(new File(filepath)));
      // BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
      InputStream inputStream = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(filepath);
      DataInputStream dataIs = new DataInputStream(inputStream);
      BufferedReader buff = new BufferedReader(new InputStreamReader(dataIs));

      line = buff.readLine();// readNextNotEmptyLine
      while (line == null || line.trim().length() == 0) {
        line = buff.readLine();
      }
      N = Integer.valueOf(line.trim());
      validateInput(N, S, L);
      w = new int[N * N];// no need big size, i.e. NegativeArraySizeException
      for (int i = 0; i < N * N; i++) {
        w[i] = i;
      }
      m = new int[N][N];
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          m[i][j] = i * N + j;
        }
      }
      // System.out.println("w: " + java.util.Arrays.toString(w));
      // System.out.println("m: " + java.util.Arrays.deepToString(m));

      line = buff.readLine();// readNextNotEmptyLine
      while (line == null || line.trim().length() == 0) {
        line = buff.readLine();
      }
      S = Integer.valueOf(line.trim());
      validateInput(N, S, L);
      abd = new int[S][3];
      for (int i = 0; i < S; i++) {
        line = buff.readLine();// readNextNotEmptyLine
        while (line == null || line.trim().length() == 0) {
          line = buff.readLine();
        }
        String[] splitS = line.trim().split("\\s");
        for (int j = 0; j < 3; j++) {
          abd[i][j] = Integer.valueOf(splitS[j]);
        }
      }
      // System.out.println("abd: " + java.util.Arrays.deepToString(abd));

      line = buff.readLine();// readNextNotEmptyLine
      while (line == null || line.trim().length() == 0) {
        line = buff.readLine();
      }
      L = Integer.valueOf(line.trim());
      validateInput(N, S, L);
      targets = new int[L];
      for (int i = 0; i < L; i++) {
        line = buff.readLine();// readNextNotEmptyLine
        while (line == null || line.trim().length() == 0) {
          line = buff.readLine();
        }
        targets[i] = Integer.valueOf(line.trim());
      }
      // System.out.println("targets: " + java.util.Arrays.toString(targets));

      buff.close();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  private static void validateInput(int N, int S, int L) {
    if (N < 0 || N > MAX_N || S < 0 || S > MAX_S || L < 0 || L > MAX_L) {
      throw new IllegalArgumentException("Invalid input!");
    }
  }

  public static void rotate(int ai, int bi, int di) {
    int aIndex = ai - 1;
    int bIndex = bi - 1;
    // System.out.println("m: BEFORE " + java.util.Arrays.deepToString(m));
    m = rotateRight90(m, N, aIndex, bIndex, di);
    // System.out.println("m: AFTER " + java.util.Arrays.deepToString(m));
  }

  public static int[][] rotateRight90(int[][] m, int N, int aIndex, int bIndex,
      int di) {
    int[][] result = new int[N][N];
    int[][] subM = new int[1 + di][1 + di];// int dIndex = 1+di;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        result[i][j] = m[i][j];
        if (i >= aIndex && i <= aIndex + di && j >= bIndex && j <= bIndex + di) {
          subM[i - aIndex][j - bIndex] = m[i][j];
        }
      }
    }
    // System.out.println("subM: " + java.util.Arrays.deepToString(subM));
    rotateRight90(subM, 1 + di);
    // System.out.println("subM: AFTER " + java.util.Arrays.deepToString(subM));
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (i >= aIndex && i <= aIndex + di && j >= bIndex && j <= bIndex + di) {
          result[i][j] = subM[i - aIndex][j - bIndex];
        }
      }
    }

    return result;
  }

  //
  // extra helpers
  //
  public static void rotateRight90(int[][] m, int N) {
    swapRows(m, N);
    transpose(m, N);
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

}
