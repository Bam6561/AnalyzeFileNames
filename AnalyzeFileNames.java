import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Splits file names into words and counts their usage from a directory and its subdirectories.
 *
 * @author Danny Nguyen
 * @version 1.0
 * @since 1.0
 */
public class AnalyzeFileNames {
  /**
   * Source directory.
   */
  private static final File source = new File("SOURCE DIRECTORY");

  /**
   * Unique words.
   */
  private static final Map<String, Integer> uniqueWords = new HashMap<>();

  /**
   * File count.
   */
  private static int files = 0;

  /**
   * Checks if the {@link #source} input is
   * valid before traversing the file system.
   *
   * @param args user provided parameters
   */
  public static void main(String[] args) {
    if (!source.isDirectory()) {
      System.out.println("Source directory does not exist.");
      return;
    }

    long start = System.currentTimeMillis();
    parseDirectory(source);
    printUniqueWords();

    long end = System.currentTimeMillis();
    System.out.println("Analyzed " + files + " files in " + millisecondsToMinutesSeconds(end - start) + ".");
  }

  /**
   * Recursively parses the directory to read file names.
   *
   * @param directory directory
   */
  private static void parseDirectory(File directory) {
    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        readFileName(file);
      } else {
        parseDirectory(file);
      }
    }
  }

  /**
   * Prints unique words by order of most frequently used.
   */
  private static void printUniqueWords() {
    List<Map.Entry<String, Integer>> uniqueWordsList = new LinkedList<>(uniqueWords.entrySet());
    uniqueWordsList.sort((i, j) -> j.getValue().compareTo(i.getValue()));

    StringBuilder entryBuilder = new StringBuilder();
    for (Map.Entry<String, Integer> entry : uniqueWordsList) {
      entryBuilder.append(entry.getValue()).append(": ").append(entry.getKey()).append("\n");
    }
    System.out.println(entryBuilder);
  }

  /**
   * Splits the file name into words and counts the word usage.
   *
   * @param file file
   */
  private static void readFileName(File file) {
    String phrase = file.getName().toLowerCase().replaceAll("[_\\-.]", " ");
    String[] words = phrase.split(" ");
    for (String word : words) {
      if (uniqueWords.containsKey(word)) {
        uniqueWords.put(word, uniqueWords.get(word) + 1);
      } else {
        uniqueWords.put(word, 1);
      }
    }
    files++;
  }

  /**
   * Converts milliseconds to minutes and seconds.
   *
   * @param duration elapsed time in milliseconds
   * @return minutes and seconds
   */
  private static String millisecondsToMinutesSeconds(long duration) {
    long minutes = duration / 60000L % 60;
    long seconds = duration / 1000L % 60;
    return ((minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "0s" : seconds + "s ")).trim();
  }
}
