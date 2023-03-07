import uk.ac.rhul.cs.csle.art.util.ARTException;
import uk.ac.rhul.cs.csle.art.value.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BitSequencer.MiniMusicPlayer;
import BitSequencer.PhraseFactory;
import BitSequencer.RunnablePlayer;

public class ValueUserPlugin implements ValueUserPluginInterface {
  private MiniMusicPlayer musicPlayer;
  private PhraseFactory phraseFactory = new PhraseFactory();
  
  /* Beats per minute, defaults to 120. Set with `SET_BPM` */
  private int bpm = 120;

  /* Map of midi channels to instrument numbers. Set with `SET_INSTRUMENT` */
  private Map<Integer, Integer> channelsToInstruments = new HashMap<Integer, Integer>();
  
  /* Maps midi channels to phrases. Set with `LOAD_CONCURRENT` and cleared after played
   * by `PLAY_CONCURRENT` */
  private Map<Integer, String> concurrentChannels = new HashMap<Integer, String>();

  enum OpCode {
    SET_BPM,                      // 0
    PLAY_PHRASE,                  // 1    
    MODULATE_PHRASE,              // 2
    CONCATENATE_PHRASE,           // 3
    REPEAT_PHRASE,                // 4
    PRINT_AVAILABLE_INSTRUMENTS,  // 5
    SET_INSTRUMENT,               // 6
    LOAD_CONCURRENT,              // 7
    PLAY_CONCURRENT;              // 8
  }
  

  @Override
  public String name() {
      return "BeatSequencer";
  }

  private void _instantiate() {
    if (musicPlayer == null) {
      musicPlayer = new MiniMusicPlayer();
    }
  }

  @Override
  public Value user(Value... args) throws ARTException {
    for (Value v : args) {
      System.out.println(v.getClass() + ": " + v.value());
    }
    OpCode opCode = OpCode.values()[(int) args[0].value()];
    switch (opCode) {
      case SET_BPM:         // 0
        _instantiate();
        this.bpm = (int) args[1].value();
        musicPlayer.setBpm(this.bpm);
        break;
      case PLAY_PHRASE:     // 1
        /**
         * Argument 1: the channel
         * Argument 2: the phrase to play
         */
        _instantiate();
        int channel = (int) args[1].value();
        String phrase = (String) args[2].value();
        musicPlayer.setInstrument(channel, channelsToInstruments.getOrDefault(channel, 0));
        musicPlayer.playPhrase(channel, phraseFactory.constructMeasureListFromPhraseString(phrase));
        break;
      case MODULATE_PHRASE: // 2
        /* Argument 1: Identifier of phrase, argument 2: amount to modulate by */
        String phraseString = (String) args[1].value();
        int shift = (int) args[2].value();
        return new __string(phraseFactory.modulatePhraseFromString(phraseString, shift));
      case CONCATENATE_PHRASE: // 3
        /* Argument 1: a phrase, argument 2: a phrase
         * Concatenate phrase 2 on to the end of phrase 1
         */
        String phrase1 = (String) args[1].value();
        String phrase2 = (String) args[2].value();
        return new __string(phraseFactory.concatPhraseFromPhraseString(phrase1, phrase2));
      case REPEAT_PHRASE:   // 4
        /* Argument 1: The phrase to repeat, argument 2: number of times to repeat it */
        String phraseToRepeat = (String) args[1].value();
        int nRepeats = (int) args[2].value();
        return new __string(phraseFactory.repeatPhrase(phraseToRepeat, nRepeats));
      case PRINT_AVAILABLE_INSTRUMENTS: // 5
        _instantiate();
        musicPlayer.printAllInstruments();
        break;
      case SET_INSTRUMENT: // 6
        // Set a given midi channel (between 0-15) to an instrument (0-127)
        _instantiate();
        int channelIndex = (int) args[1].value();
        int instrument = (int) args[2].value();
        channelsToInstruments.put(channelIndex, instrument);
        break;
      case LOAD_CONCURRENT: // 7
        /* Argument 1: midi channel, argument 2: phrase */
        System.out.println("Loading concurrent channel " + args[1].value().toString() + " with phrase " + args[2].value().toString());
        concurrentChannels.put((Integer) args[1].value(), (String) args[2].value());
        break;
      case PLAY_CONCURRENT: // 8
        System.out.println("Playing concurrent channels");
        try {
          List<Thread> threads = new ArrayList<Thread>(concurrentChannels.size());
          for (Map.Entry<Integer, String> e : concurrentChannels.entrySet()) {
            threads.add(
              new Thread(
                new RunnablePlayer(e.getKey(), channelsToInstruments.getOrDefault(e.getKey(), 0), e.getValue(), this.bpm)
              )
            );
          }
          for (Thread thread : threads) {
            thread.start();
          }
          for (Thread thread : threads) {
            thread.join();
          }
        } catch (InterruptedException e) {
          /* ignore  */
        }
        concurrentChannels.clear();
        break;
      default:
        return new __bottom();
    }
    return new __done();
  }
}
