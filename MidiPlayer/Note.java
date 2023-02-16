package MidiPlayer;

public class Note {
  private String pitch;
  private int length;
  private MidiNoteMapSingleton instance = MidiNoteMapSingleton.INSTANCE;

  public Note(String pitch, int length) {
    this.pitch = pitch;
    this.length = length;
  }

  public String getPitch() {
    return this.pitch;
  }

  public Integer getLength() {
    return this.length;
  }

  /**
   * Modulate the note by n and return a new modulated Note instance
   */
  public Note modulate(int n) {
    int shiftedKey = instance.getMidiKey(this.pitch) + n;
    return new Note(
      instance.getNoteFromMidiKey(shiftedKey),
      this.length
    );
  }
}
