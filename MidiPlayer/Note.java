package MidiPlayer;

public class Note {
  private String pitch;
  private MidiNoteMapSingleton instance = MidiNoteMapSingleton.INSTANCE;

  public Note(String pitch) {
    this.pitch = pitch;
  }

  public String getPitch() {
    return this.pitch;
  }

  /**
   * Modulate the note by n and return a new modulated Note instance
   */
  public Note modulate(int n) {
    if (!this.isRest()) {
      System.out.println("Modulating note " + this.pitch);
      int shiftedKey = instance.getMidiKey(this.pitch) + n;
      return new Note(
        instance.getNoteFromMidiKey(shiftedKey)
      );
    } else {
      return new Note(this.pitch);
    }
  }
  
  public boolean isRest() {
    return this.pitch.equals(".");
  }
}
