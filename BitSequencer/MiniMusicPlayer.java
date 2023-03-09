package BitSequencer;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import java.util.List;

/** MiniMusicPlayer re-engineered to fit the needs of BeatSequencer */

public class MiniMusicPlayer {
  private final int FOURFOUR = 4;
  private final int DOT = FOURFOUR / 2;

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
        c.programChange(0);
      }
    } catch (Exception e) {
      System.err.println("miniMusicPlayer exception: " + e.getMessage());
      System.exit(1);
    }
  
    
    setBeatRatio(0.9);
    setBpm(100);
    setDefaultVelocity(100);
    rest(1, 2);
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

  public int calculateDurationFactor(boolean dotted) {
    return dotted ? DOT + FOURFOUR : FOURFOUR;
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


  public void rest(int beats, int subdivision) {
    try {
      Thread.sleep(FOURFOUR * (long) (beats * beatPeriod) / (subdivision));
    } catch (InterruptedException e) {
      /* ignore */
    }
  }

  void play(int channel, int k, int subdivision, boolean dotted) {
    int factor = calculateDurationFactor(dotted);
    try {
      channels[channel].noteOn(k, defaultVelocity);
      Thread.sleep(factor * (beatSoundDelay / subdivision));
      channels[channel].noteOn(k, 0);
      Thread.sleep(factor * (beatSilenceDelay / subdivision));
    } catch (InterruptedException e) {
      /* ignore */
    }
  }

  public void playNote(int channel, Note note, int subdivision, boolean dotted) {
    String pitch = note.getPitch();
    if (note.isRest()) {
      rest(1, subdivision);
    } else {
      int midiKey = getMidiKeyFromPitch(pitch);
      System.out.println(pitch + " (" + midiKey + ")");
      play(channel, midiKey, subdivision, dotted);
    }
  }

  public void playNotes(int channel, List<Note> notes, int subdivision, boolean dotted) {
    int factor = calculateDurationFactor(dotted);
    try {
      for (Note note : notes) {
        String pitch = note.getPitch();
        int midiKey = getMidiKeyFromPitch(pitch);
        if (!note.isRest()) {
          System.out.println(note.getPitch() + " (" + midiKey + ")");
          channels[channel].noteOn(midiKey, defaultVelocity);
        }
      }
      Thread.sleep(factor * (beatSoundDelay / subdivision));
      for (Note note : notes) {
        if (!note.isRest()) {
          channels[channel].noteOn(getMidiKeyFromPitch(note.getPitch()), 0);
        }
      }
      Thread.sleep(factor * (beatSilenceDelay / subdivision));
    } catch (InterruptedException e) {
      /* ignore */
    }

  }
    
  void close() {
    rest(1, 1);
    synthesizer.close();
  }

  public void playPhrase(int channel, List<Measure> phrase) {
    for (Measure measure : phrase) {
      playMeasure(channel, measure);
    }
  }

  public void playMeasure(int channel, Measure measure) {
    if (measure.isChord()) {
      playNotes(channel, measure.getNotes(), measure.getSubdivision(), measure.getDotted());
    } else {
      for (Note note : measure.getNotes()) {
        playNote(channel, note, measure.getSubdivision(), measure.getDotted());
      }
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

  /* Used to verify a channel index argument is between 0 and 15 */
  public void checkChannelIndex(int channelIndex) {
    if (channelIndex < 0 || channelIndex > 15) {
      System.out.println("Error, one synthesizer only supports 16 MIDI channels. Please specify a channel between 0 and 15.");
      System.exit(-1);
    }
  }

  public void setInstrument(int channelIndex, int instrumentIndex) {
    checkChannelIndex(channelIndex);
    channels[channelIndex].programChange(instrumentIndex);
  }
}
