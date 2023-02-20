package BeatSequencer;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps note strings to their MIDI note numbers, and vice versa.
 * Helper methods generate the maps to save typing out 127 entries by hand.
 * Midi notes from https://studiocode.dev/resources/midi-middle-c/
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
    baseNoteToMidiKey.put("A0", 21);
    baseNoteToMidiKey.put("A#0", 22);
    baseNoteToMidiKey.put("Bb0", 22);
    baseNoteToMidiKey.put("B0", 23);
    baseNoteToMidiKey.put("C1", 24);
    baseNoteToMidiKey.put("C#1", 25);
    baseNoteToMidiKey.put("Db1", 25);
    baseNoteToMidiKey.put("D1", 26);
    baseNoteToMidiKey.put("D#1", 27);
    baseNoteToMidiKey.put("Eb1", 27);
    baseNoteToMidiKey.put("E1", 28);
    baseNoteToMidiKey.put("F1", 29);
    baseNoteToMidiKey.put("F#1", 30);
    baseNoteToMidiKey.put("Gb1", 30);
    baseNoteToMidiKey.put("G1", 31);
    baseNoteToMidiKey.put("G#1", 32);
    baseNoteToMidiKey.put("Ab1", 32);

    Map<String, Integer> tmp = new HashMap<String, Integer>();
    for (Map.Entry<String, Integer> e : baseNoteToMidiKey.entrySet()) {
      for (int i = 1; i <= 9; i++) {
        String noteName = e.getKey().substring(0, e.getKey().length() - 1);
        Integer noteNumber = Integer.parseInt(e.getKey().substring(e.getKey().length() - 1));
        String newKey = noteName + Integer.toString(noteNumber + (i * 1 - 1));
        int newValue = e.getValue() + (12 * (i - 1));
        System.out.println("New key is " + newKey);
        tmp.put(newKey, newValue);
      }
    }
    baseNoteToMidiKey.putAll(tmp);
    baseNoteToMidiKey.put("C-1", 0);
    baseNoteToMidiKey.put("C#-1", 1);
    baseNoteToMidiKey.put("Db-1", 1);
    baseNoteToMidiKey.put("D-1", 2);
    baseNoteToMidiKey.put("D#-1", 3);
    baseNoteToMidiKey.put("Eb-1", 3);
    baseNoteToMidiKey.put("E-1", 4);
    baseNoteToMidiKey.put("F-1", 5);
    baseNoteToMidiKey.put("F#-1", 6);
    baseNoteToMidiKey.put("Gb-1", 6);
    baseNoteToMidiKey.put("G-1", 7);
    baseNoteToMidiKey.put("G#-1", 8);
    baseNoteToMidiKey.put("Ab-1", 8);
    baseNoteToMidiKey.put("A-1", 9);
    baseNoteToMidiKey.put("A#-1", 10);
    baseNoteToMidiKey.put("Bb-1", 10);
    baseNoteToMidiKey.put("B-1", 11);
    baseNoteToMidiKey.put("C0", 12);
    baseNoteToMidiKey.put("C#0", 13);
    baseNoteToMidiKey.put("Db0", 13);
    baseNoteToMidiKey.put("D0", 14);
    baseNoteToMidiKey.put("D#0", 15);
    baseNoteToMidiKey.put("Eb0", 15);
    baseNoteToMidiKey.put("E0", 16);
    baseNoteToMidiKey.put("F0", 17);
    baseNoteToMidiKey.put("F#0", 18);
    baseNoteToMidiKey.put("Gb0", 18);
    baseNoteToMidiKey.put("G0", 19);
    baseNoteToMidiKey.put("G#0", 20);
    baseNoteToMidiKey.put("Ab0", 20);


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
