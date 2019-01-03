package org.bio2schema.reconcilers.dbpedia;

/*
 * The code below has been slightly modified. The original source code can be found
 * at https://itssmee.wordpress.com/2010/06/28/java-example-of-damerau-levenshtein-distance
 */
public class NameSimilarity {

  public static boolean evaluate(String s1, String s2, double threshold) {
    return normDistance(s1, s2) > threshold;
  }

  public static double normDistance(String s1, String s2) {
    return 1.0 - distance(s1, s2) / (double) Math.max(s1.length(), s2.length());
  }

  public static int distance(String s1, String s2) {
    int cost = -1;
    int del, sub, ins;

    int[][] matrix = new int[s1.length() + 1][s2.length() + 1];

    for (int i = 0; i <= s1.length(); i++) {
      matrix[i][0] = i;
    }

    for (int i = 0; i <= s2.length(); i++) {
      matrix[0][i] = i;
    }

    for (int i = 1; i <= s1.length(); i++) {
      for (int j = 1; j <= s2.length(); j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          cost = 0;
        } else {
          cost = 1;
        }

        del = matrix[i - 1][j] + 1;
        ins = matrix[i][j - 1] + 1;
        sub = matrix[i - 1][j - 1] + cost;

        matrix[i][j] = minimum(del, ins, sub);

        if ((i > 1) && (j > 1) && (s1.charAt(i - 1) == s2.charAt(j - 2))
            && (s1.charAt(i - 2) == s2.charAt(j - 1))) {
          matrix[i][j] = minimum(matrix[i][j], matrix[i - 2][j - 2] + cost);
        }
      }
    }
    return matrix[s1.length()][s2.length()];
  }

  private static int minimum(int d, int i, int s) {
    int m = Integer.MAX_VALUE;
    if (d < m) {
      m = d;
    }
    if (i < m) {
      m = i;
    }
    if (s < m) {
      m = s;
    }
    return m;
  }

  private static int minimum(int d, int t) {
    int m = Integer.MAX_VALUE;
    if (d < m) {
      m = d;
    }
    if (t < m) {
      m = t;
    }
    return m;
  }
}
