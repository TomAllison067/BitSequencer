package MidiPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * A measure consists of one or more notes, or a rest.
 */
public class Measure {
  private List<Note> notes;
  int length;

  public Measure(List<Note> notes, int length) {
    this.notes = new ArrayList<>();
    this.notes = notes;
    this.length = length;
  }

  public List<Note> getNotes() {
    return this.notes;
  }

  public Measure modulate(int n) {
    List<Note> modulatedNotes = new ArrayList<Note>();
    for (Note note : this.notes) {
      modulatedNotes.add(note.modulate(n));
    }
    return new Measure(modulatedNotes, this.length);
  }

  public int getLength() {
    return this.length;
  }

  public boolean isChord() {
    return this.notes.size() > 1;
  }
}
