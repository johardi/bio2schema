package org.bio2schema.pipeline.clinicaltrials;

public class StringUtils {

  /*
   * Check if the input text has the specified string ignoring case consideration.
   * Code taken from:
   * https://stackoverflow.com/questions/86780/how-to-check-if-a-string-contains-another-string-in-a
   * -case-insensitive-manner-in
   */
  public static boolean containsIgnoreCase(String text, String s) {
    final int length = s.length();
    if (length == 0) {
      return true; // Empty string is contained
    }
    final char firstLo = Character.toLowerCase(s.charAt(0));
    final char firstUp = Character.toUpperCase(s.charAt(0));

    for (int i = text.length() - length; i >= 0; i--) {
      final char ch = text.charAt(i);
      if (ch != firstLo && ch != firstUp) {
        continue;
      }
      if (text.regionMatches(true, i, s, 0, length)) {
        return true;
      }
    }
    return false;
  }

  /*
   * Retrieve the first occurrence of the specified string over a text ignoring case consideration
   * Code taken from:
   * https://stackoverflow.com/questions/1126227/indexof-case-sensitive
   */
  public static int indexOfIgnoreCase(String text, String s) {
    if (s.isEmpty() || text.isEmpty()) {
      return text.indexOf(s);
    }
    for (int i = 0; i < text.length(); ++i) {
      if (i + s.length() > text.length()) {
        return -1;
      }
      int j = 0;
      int ii = i;
      while (ii < text.length() && j < s.length()) {
        char c = Character.toLowerCase(text.charAt(ii));
        char c2 = Character.toLowerCase(s.charAt(j));
        if (c != c2) {
          break;
        }
        j++;
        ii++;
      }
      if (j == s.length()) {
        return i;
      }
    }
    return -1;
  }
}
