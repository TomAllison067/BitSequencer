package MidiPlayer;

import java.util.ArrayList;
import java.util.List;

public class PhraseFactory {
  private final String SEPARATOR = ", ";

  public List<Note> constructNoteListFromPhraseString(String phraseString) {
    System.out.println("constructNoteListFromPhraseString: " + phraseString);
    phraseString = phraseString.substring(1, phraseString.length() - 1);
    String[] notes = phraseString.split(",[ ]+");
    List<Note> phrase = new ArrayList<Note>();
    for (String note : notes) {
      String[] components = note.split(":");
      phrase.add(new Note(components[0], Integer.parseInt(components[1])));
    }
    return phrase;
  }

  public String modulatePhraseFromString(String phrase, int n) {
    List<Note> modulatedPhrase = _modulatePhrase(constructNoteListFromPhraseString(phrase), n);
    String result = constructPhraseStringFromNoteList(modulatedPhrase);    
    System.out.println("Modulation: " + result);
    return result;
  }

  /*
   * Shift all notes in a phrase by n, and return a new phrase.
   */
  private List<Note> _modulatePhrase(List<Note> originalPhrase, int n) {
    List<Note> shiftedPhrase = new ArrayList<Note>();
    for (Note note : originalPhrase) {
      shiftedPhrase.add(note.modulate(n));
    }
    return shiftedPhrase;
  }

  public String constructPhraseStringFromNoteList(List<Note> phrase) {
    StringBuilder sb = new StringBuilder("[");
    String separator = "";
    for (Note note : phrase) {
      sb.append(separator + note.getPitch() + ":" + note.getLength());
      separator = SEPARATOR;
    }
    sb.append("]");
    return sb.toString();
  }

  /** Concatenate two strings, e.g. "[Cb3:2, E4:8, G5:8]" + "[D4:8, .:8]"
   *  concatenates to "[Cb3:2, E4:8, G5:8, D4:8, .:8]"
   */
  public String concatPhraseFromPhraseString(String phrase1, String phrase2) {
    String phrase1prefix = phrase1.substring(0, phrase1.length() - 1);
    String phrase2suffix = phrase2.substring(1);
    StringBuilder concatenated = new StringBuilder(phrase1prefix);
    concatenated.append(SEPARATOR);
    concatenated.append(phrase2suffix);
    return concatenated.toString();
  }

  public String repeatPhrase(String phrase, int n) {
    if (n <= 1) {
      System.out.println("Invalid argument to repeatPhrase - n should be >= 1");
      System.exit(-1);
    }
    String repeated = phrase;
    for (int i = 0; i < n; i++) {
      repeated = concatPhraseFromPhraseString(repeated, phrase);
    }
    return repeated;
  }
}
