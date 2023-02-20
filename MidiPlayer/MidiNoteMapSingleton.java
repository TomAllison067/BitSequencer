package MidiPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps note strings to their MIDI note numbers, and vice versa.
 * Helper methods generate the maps to save typing out 127 entries by hand.
 */
public enum MidiNoteMapSingleton {
  INSTANCE();

  /* Array positions to their note values, i.e. 0-127 */
  private int[] midiNoteArray;

  /* Note names to their midi keys */
  private HashMap<String, Integer> baseNoteToMidiKey = new HashMap<String, Integer>();

  /* Midi keys to note names */
  private HashMap<Integer, String> inverseMap = new HashMap<Integer, String>();

  /* Notation for a rest */
  private final String REST_STRING = ".";

  private MidiNoteMapSingleton() {
    initBaseNoteToMidiKey();
    initMidiNoteArray();
    initInverseMap();
  }

  private void initBaseNoteToMidiKey() {
    baseNoteToMidiKey.put("A", 21);
    baseNoteToMidiKey.put("A#", 22);
    baseNoteToMidiKey.put("Bb", 22);
    baseNoteToMidiKey.put("B", 23);
    baseNoteToMidiKey.put("C", 24);
    baseNoteToMidiKey.put("C#", 25);
    baseNoteToMidiKey.put("Db", 25);
    baseNoteToMidiKey.put("D", 26);
    baseNoteToMidiKey.put("D#", 27);
    baseNoteToMidiKey.put("Eb", 27);
    baseNoteToMidiKey.put("E", 28);
    baseNoteToMidiKey.put("F", 29);
    baseNoteToMidiKey.put("F#", 30);
    baseNoteToMidiKey.put("Gb", 30);
    baseNoteToMidiKey.put("G", 31);
    baseNoteToMidiKey.put("G#", 32);
    baseNoteToMidiKey.put("Ab", 32);

    Map<String, Integer> tmp = new HashMap<String, Integer>();
    for (Map.Entry<String, Integer> e : baseNoteToMidiKey.entrySet()) {
      for (int i = 0; i <= 9; i++) {
        String newKey = e.getKey() + Integer.toString(i);
        int newValue = e.getValue() + (12 * (i - 1));
        tmp.put(newKey, newValue);
      }
    }
    baseNoteToMidiKey.putAll(tmp);
    midiNoteArray = new int[baseNoteToMidiKey.size()];
  }

  private void initInverseMap() {
    for (Map.Entry<String, Integer> e : baseNoteToMidiKey.entrySet()) {
      inverseMap.put(e.getValue(), e.getKey());
    }
  }

  private void initMidiNoteArray() {
    for (int i = 0; i < midiNoteArray.length; i++){
      midiNoteArray[i] = i;
    }
  }

  public String getRestString() {
    return this.REST_STRING;
  }

  public int getMidiKey(String pitch) {
    return midiNoteArray[baseNoteToMidiKey.get(pitch)];
  }

  public String getNoteFromMidiKey(int midiKey) {
    return inverseMap.get(midiKey);
  }
}
