import java.io.File;
import java.util.*;

/**
 * Splits file names into words and counts their usage from a directory and its subdirectories.
 *
 * @author Danny Nguyen
 * @version 1.1
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
    List<String> sortedWords = new ArrayList<>(uniqueWords.keySet());
    Collections.sort(sortedWords);

    Map<Integer, List<String>> wordFrequency = new HashMap<>();
    for (String word : sortedWords) {
      int frequency = uniqueWords.get(word);
      if (wordFrequency.containsKey(frequency)) {
        wordFrequency.get(frequency).add(word);
      } else {
        wordFrequency.put(frequency, new ArrayList<>(List.of(word)));
      }
    }

    List<Integer> sortedFrequencies = new ArrayList<>(wordFrequency.keySet());
    sortedFrequencies.sort(Collections.reverseOrder());

    StringBuilder wordFrequencyBuilder = new StringBuilder();
    for (int frequency : sortedFrequencies) {
      List<String> words = wordFrequency.get(frequency);
      wordFrequencyBuilder.append(frequency).append(": ").append(words.get(0));
      for (int i = 1; i < words.size(); i++) {
        wordFrequencyBuilder.append(", ").append(words.get(i));
      }
      wordFrequencyBuilder.append("\n");
    }
    System.out.println(wordFrequencyBuilder);
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
