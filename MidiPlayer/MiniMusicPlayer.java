package MidiPlayer;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import java.util.List;

/**
TODO: You can set instruments via the programChange method of a MidiChannel.
  For the language, we could specify an instrument - e.g. drums, synth, .etc, and then use some sort of tabular / drum tab notation, parse that.. would that work?
  A text-based sequencer??? Or a text based drum machine? Could do REPEAT bars, store phrases in the symbol table.. it might go horribly wrong but it could be fun.
*/


public class MiniMusicPlayer {
  private Synthesizer synthesizer;
  private MidiChannel[] channels;
  private int defaultOctave = 5;
  private int defaultVelocity = 50;
  private int bpm;
  private double bps;
  private double beatPeriod;
  private double beatRatio = 0.9;
  private int beatSoundDelay = (int) (1000.0 * beatRatio / bps);
  private int beatSilenceDelay = (int) (1000.0 * (1.0 - beatRatio) / bps);
  
  private MidiNoteMapSingleton midiNoteMap = MidiNoteMapSingleton.INSTANCE;

  public MiniMusicPlayer() {
    System.out.println("MINIPLAYER CONSTRUCT");
    try {
      System.out.print(MidiSystem.getMidiDeviceInfo());
      synthesizer = MidiSystem.getSynthesizer();
      synthesizer.open();
      channels = synthesizer.getChannels();
      System.out.println("MIDI channel count: " + channels.length);
      for (MidiChannel c : channels) {
        c.programChange(57);
      }
    } catch (Exception e) {
      System.err.println("miniMusicPlayer exception: " + e.getMessage());
      System.exit(1);
    }
  
    
    setBeatRatio(0.9);
    setBpm(100);
    setDefaultVelocity(100);
    rest(2);
  }

  public Synthesizer getSynthesizer() {
    return this.synthesizer;
  }

  public int getDefaultOctave() {
    return defaultOctave;
  }
  
  public void setDefaultOctave(int defaultOctave) {
    this.defaultOctave = defaultOctave;
  }

  public void setDefaultVelocity(int defaultVelocity) {
    this.defaultVelocity = defaultVelocity;
  }

  public int getDefaultVelocity() {
    return this.defaultVelocity;    
  }

  public int getBpm() {
    return bpm;
  }

  public void setBpm(int bpm) {
    this.bpm = bpm;
    bps = bpm / 60.0;
    beatPeriod = 1000.0 / bps;
    beatSoundDelay = (int) (beatRatio * beatPeriod);
    beatSilenceDelay = (int) ((1.0 - beatRatio) * beatPeriod);
  }

  public void setBeatRatio(double beatRatio) {
    this.beatRatio = beatRatio;
    beatSoundDelay = (int) (beatRatio * beatPeriod);
    beatSilenceDelay = (int) ((1.0 - beatRatio) * beatPeriod);
  }


  public void rest(int beats) {
    try {
      Thread.sleep((long) (beats * beatPeriod));
    } catch (InterruptedException e) {
      /* ignore */
    }
  }

  public void rest(int beats, int length) {
    try {
      Thread.sleep((long) (beats * beatPeriod) / (length / 4));
    } catch (InterruptedException e) {
      /* ignore */
    }
  }

  void play(int k, int length) {
    System.out.println("Playing " + k);
    try {
      channels[1].noteOn(k, defaultVelocity);
      Thread.sleep(beatSoundDelay / (length / 4));
      channels[1].noteOn(k, 0);
      Thread.sleep(beatSilenceDelay / (length / 4));
    } catch (InterruptedException e) {
      /* ignore */
    }
  }
 
  // Arrays of notes 
  void play(int[] k) {
    try {
      for (int i = 0; i < k.length; i++)
        channels[1].noteOn(k[i], defaultVelocity);
      Thread.sleep(beatSoundDelay);
      for (int i = 0; i < k.length; i++)
        channels[1].noteOn(k[i], 0);
      Thread.sleep(beatSilenceDelay);
    } catch (InterruptedException e) {
      /* ignore */
    }
  }
    
  void close() {
    rest(3);
    synthesizer.close();
  }

  public void playPhrase(List<Note> phrase) {
    System.out.println("PLAYING PHRASE");
    for (Note note : phrase) {
      playNote(note);
    }
  }

  public void playNote(Note note) {
    String pitch = note.getPitch();
    Integer length = note.getLength();
    if (note.getPitch().equals(".")) {
      rest(1, length);
    } else {
      int midiKey = getMidiKeyFromPitch(pitch);
      play(midiKey, length);
    }
  }

  public int getMidiKeyFromPitch(String pitch){
    return midiNoteMap.getMidiKey(pitch);
  }

  public void printAllInstruments() {
    Instrument[] instruments = synthesizer.getAvailableInstruments();
    StringBuilder sb = new StringBuilder();
    String eol = System.getProperty("line.separator");
    sb.append("Instruments: " + instruments.length);
    sb.append(eol);
    for (Instrument instrument : instruments) {
      sb.append(instrument.toString());
      sb.append(eol);
    }
    System.out.print(sb.toString());
  }

  public void setInstrument(int channelIndex, String instrument) {
    if (channelIndex < 0 || channelIndex > 15) {
      System.out.println("Error, one synthesizer only supports 16 MIDI channels. Please specify a channel between 0 and 15.");
      System.exit(-1);
    }
    // todo get instrument from name
    // set channel to instrument
  }
}
