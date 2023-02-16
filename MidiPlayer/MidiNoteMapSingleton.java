package MidiPlayer;

import java.util.HashMap;
import java.util.Map;

public enum MidiNoteMapSingleton {
  INSTANCE();

  /* Array positions to their note values, i.e. 0-127 */
  private int[] midiNoteArray = new int[128];

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
    baseNoteToMidiKey.put("C", 0);
    baseNoteToMidiKey.put("C#", 1);
    baseNoteToMidiKey.put("Db", 1);
    baseNoteToMidiKey.put("D", 2);
    baseNoteToMidiKey.put("D#", 3);
    baseNoteToMidiKey.put("Eb", 3);
    baseNoteToMidiKey.put("E", 4);
    baseNoteToMidiKey.put("F", 5);
    baseNoteToMidiKey.put("F#", 6);
    baseNoteToMidiKey.put("Gb", 6);
    baseNoteToMidiKey.put("G", 7);
    baseNoteToMidiKey.put("G#", 8);
    baseNoteToMidiKey.put("Ab", 8);
    baseNoteToMidiKey.put("A", 9);
    baseNoteToMidiKey.put("A#", 10);
    baseNoteToMidiKey.put("Bb", 10);
    baseNoteToMidiKey.put("B", 11);

    Map<String, Integer> tmp = new HashMap<String, Integer>();
    for (Map.Entry<String, Integer> e : baseNoteToMidiKey.entrySet()) {
      for (int i = 1; i <= 9; i++) {
        String newKey = e.getKey() + Integer.toString(i);
        int newValue = e.getValue() + (12 * i);
        tmp.put(newKey, newValue);
      }
    }
    baseNoteToMidiKey.putAll(tmp);
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
