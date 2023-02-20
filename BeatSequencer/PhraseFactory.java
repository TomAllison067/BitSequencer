package BeatSequencer;

import java.util.ArrayList;
import java.util.List;

public class PhraseFactory {
  private final String MEASURE_SEPARATOR = ", ";
  private final String CHORD_NOTE_SEPARATOR = "/";

  public List<Measure> constructMeasureListFromPhraseString(String phraseString) {
    System.out.println("constructMeasureListFromPhraseString: " + phraseString);
    phraseString = phraseString.substring(1, phraseString.length() - 1);
    String[] phraseStringArray = phraseString.split(",[ ]+");
    List<Measure> phrase = new ArrayList<Measure>();
    for (String note : phraseStringArray) {
      String[] components = note.split(":");
      int length = Integer.parseInt(components[1]);
      phrase.add(new Measure(
        parseComponentToNote(components[0]), length));
    }
    return phrase;
  }

  public List<Note> parseComponentToNote(String component) {
    List<Note> notes = new ArrayList<Note>();
    if (component.startsWith("{") && component.endsWith("}")) {
      /* Case 1: chords */
      String[] noteStrings = component.substring(1, component.length() - 1).split(CHORD_NOTE_SEPARATOR);
      for (String noteString : noteStrings) {
        notes.add(new Note(noteString));
      }
    } else {
      /* Case 2: Single notes */
      notes.add(new Note(component));
    } 
    return notes;
  }

  public String modulatePhraseFromString(String phrase, int n) {
    List<Measure> modulatedPhrase = _modulatePhrase(constructMeasureListFromPhraseString(phrase), n);
    String result = constructPhraseStringFromMeasureList(modulatedPhrase);    
    System.out.println("Modulation: " + result);
    return result;
  }

  /*
   * Shift all notes in a phrase by n, and return a new phrase.
   */
  private List<Measure> _modulatePhrase(List<Measure> originalPhrase, int n) {
    List<Measure> shiftedPhrase = new ArrayList<Measure>();
    for (Measure measure : originalPhrase) {
      shiftedPhrase.add(measure.modulate(n));
    }
    return shiftedPhrase;
  }

  public String constructPhraseStringFromMeasureList(List<Measure> phrase) {
    StringBuilder sb = new StringBuilder("[");
    String separator = "";
    for (Measure measure : phrase) {
      if (measure.isChord()) {
        sb.append(separator);
        separator = "";
        sb.append("{");
        for (Note note : measure.getNotes()) {
          sb.append(separator + note.getPitch());
          separator = CHORD_NOTE_SEPARATOR;
        }
        sb.append("}");
        sb.append(":" + measure.getSubdivision());
        separator = MEASURE_SEPARATOR;
      } else {
        for (Note note : measure.getNotes()) {
          sb.append(separator + note.getPitch() + ":" + measure.getSubdivision());
          separator = MEASURE_SEPARATOR;
        }
      }
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
    concatenated.append(MEASURE_SEPARATOR);
    concatenated.append(phrase2suffix);
    return concatenated.toString();
  }

  public String repeatPhrase(String phrase, int n) {
    if (n <= 0) {
      System.out.println("Invalid argument to repeatPhrase - n should be >= 0");
      System.exit(-1);
    }
    String repeated = phrase;
    for (int i = 0; i < n - 1; i++) {
      repeated = concatPhraseFromPhraseString(repeated, phrase);
    }
    return repeated;
  }
}
