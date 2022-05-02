import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EasyUse {

  /**
   * Main method where the user will
   * interact/enter their guesses
   *
   * @param args - unused
   */
  public static void main(String[] args) {
    ArrayList<String> wordList = new ArrayList<>(16500);
    File file = new File("/Users/acarrig/CompSciStuff/GetWords/wordle.txt");
    Scanner scnr;

    try {

      // Add the words to an ArrayList
      scnr = new Scanner(file);
      while (scnr.hasNextLine()) {
        wordList.add(scnr.nextLine());
      }

      // Getting an inital guess
      Scanner userScnr = new Scanner(System.in);
      String guess = provideGuess(wordList);
      System.out.println("Computer's first guess: " + guess);
      String input = "";

      // While loop where user will enter the results of each guess
      while (!input.equalsIgnoreCase("done")) {
        System.out.println("Enter the results of the previous guess " + "" +
            "(eg gybbg would represent a green yellow black black green result) or \"done\"");
        input = userScnr.nextLine();
        input = input.toLowerCase(Locale.ROOT); // Have the users input be lower case letters

        // Break if user wants to quit or word has been found
        if (input.equals("done") || input.equals("ggggg")) break;

        // See if valid input
        if (input.length() != 5) System.out.println("Invalid input");

        // Go through input 1 char at a time
        for (int i = 0; i < 5; i++) {
          if (input.charAt(i) == 'g') { // When input[i] is 'g' ---------------- CASE 1 ------------

            // Loop through wordList and remove every word where the char at i
            // is not the same as the char at i in guess
            for (int j = wordList.size() - 1; j >= 0; j--) {
              String currWord = wordList.get(j);
              if (currWord.charAt(i) != guess.charAt(i)) {
                wordList.remove(j);
              }
            }
          } else if (input.charAt(i) == 'y') { // When input[i] is 'y' --------- CASE 2 ------------

            // Loop through wordList and make sure each word contains the letter at index i
            // but not at index i
            for (int j = wordList.size() - 1; j >= 0; j--) {
              String currWord = wordList.get(j);
              if (currWord.charAt(i) == guess.charAt(i)) { // Remove words with guess(i) at index i
                wordList.remove(j);
              }

              // Remove words that do not contain char
              if (i == 4) { // When we are examining the last slot in input
                if (!currWord.contains(guess.substring(i))) {
                  wordList.remove(j);
                }
              } else { // When we are examining any other spot in input
                if (!currWord.contains(guess.substring(i, i + 1))) {
                  wordList.remove(j);
                }
              }
            }
          } else { // When input[i] is 'b' ----------------------------------- CASE 3 -------------

            // Check if char occurs elsewhere in the word, if it does, but that spot is 'b'
            // normal case, but if that other spot is NOT 'b', treat like case 2
            boolean multipleOccur = false;
            for(int currInd = 0; currInd < 5; currInd++) { // Search through guess
              if(currInd == i) continue; // Skip when same index
              if(guess.charAt(currInd) == guess.charAt(i)) { // If char is found somewhere else
                if(input.charAt(currInd) != 'b') {
                  multipleOccur = true;
                  for(int wordInd = wordList.size() -1; wordInd >= 0; wordInd--) {
                    if(wordList.get(wordInd).charAt(i) == guess.charAt(i))
                      wordList.remove(wordInd);
                  }
                }
              }
            }

            // If char only appears once in word, remove all words containing it
            if(!multipleOccur) {
              if(i < 4) {
                String charToFind = guess.substring(i, i + 1);
                for (int wordInd = wordList.size() - 1; wordInd >= 0; wordInd--) {
                  if(wordList.get(wordInd).contains(charToFind))
                    wordList.remove(wordInd);
                }
              } else { // i == 4
                String charToFind = guess.substring(i);
                for (int wordInd = wordList.size() - 1; wordInd >= 0; wordInd--) {
                  if(wordList.get(wordInd).contains(charToFind))
                    wordList.remove(wordInd);
                }
              }
            }

          }
        }
        guess = provideGuess(wordList);
        System.out.println("Next guess: " + guess);
        System.out.println("List size: " + wordList.size());
      } // end of while loop

      // Print wordList
      for (int index = 0; index < wordList.size(); index++) {
        System.out.println(wordList.get(index));
      }

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
  }

  /**
   * Provide a guess to the user
   * Implementation:
   * The guess is a string from a random
   * index in the wordsList arrayList
   *
   * @param words - an arrayList of Strings containing possible words
   * @return The recommended guess as a string
   */
  public static String provideGuess(ArrayList<String> words) {
    if (words.size() == 0) return "Out of Luck";
    Random rand = new Random();
    return words.get(rand.nextInt(words.size()));
  }

}