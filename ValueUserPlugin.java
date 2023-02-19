import uk.ac.rhul.cs.csle.art.util.ARTException;
import uk.ac.rhul.cs.csle.art.value.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MidiPlayer.MiniMusicPlayer;
import MidiPlayer.PhraseFactory;
import MidiPlayer.RunnablePlayer;

public class ValueUserPlugin implements ValueUserPluginInterface {
  private MiniMusicPlayer musicPlayer = new MiniMusicPlayer();
  private PhraseFactory phraseFactory = new PhraseFactory();
  
  private int bpm = 120;

  private Map<Integer, Integer> channelsToInstruments = new HashMap<Integer, Integer>();
  
  enum OpCode {
    SET_BPM,                      // 0
    PLAY_PHRASE,                  // 1    
    MODULATE_PHRASE,              // 2
    CONCATENATE_PHRASE,           // 3
    REPEAT_PHRASE,                // 4
    PRINT_AVAILABLE_INSTRUMENTS,  // 5
    SET_INSTRUMENT,               // 6
    PLAY_PARALLEL;                // 7
  }
  

  @Override
  public String name() {
      return "BeatSequencer";
  }

  @Override
  public Value user(Value... args) throws ARTException {
    for (Value v : args) {
      System.out.println(v.getClass() + ": " + v.value());
    }
    OpCode opCode = OpCode.values()[(int) args[0].value()];
    switch (opCode) {
      case SET_BPM:         // 0
        this.bpm = (int) args[1].value();
        musicPlayer.setBpm(this.bpm);
        break;
      case PLAY_PHRASE:     // 1
        /**
         * Argument 1: the channel
         * Argument 2: the phrase to play
         */
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
        musicPlayer.printAllInstruments();
        break;
      case SET_INSTRUMENT: // 6
        // Set a given midi channel (between 0-15) to an instrument (0-127)
        int channelIndex = (int) args[1].value();
        int instrument = (int) args[2].value();
        channelsToInstruments.put(channelIndex, instrument);
        break;
      case PLAY_PARALLEL: // 7
        /* Argument 1: the map */
        try {
          System.out.println("Type of arg 1 is " + args[1].getClass());
          Value map = args[1];
          List<Thread> threads;
          if (map.value() instanceof HashMap<?, ?>) {
            threads = new ArrayList<Thread>(((HashMap<?, ?>) map.value()).size());
            for (Map.Entry<?, ?> e : ((HashMap<?, ?>) map.value()).entrySet()) {
              int key = (int) ((Value) e.getKey()).value();
              String val = (String) ((Value) e.getValue()).value();
              threads.add(
                new Thread(new RunnablePlayer(key, channelsToInstruments.getOrDefault(key, 0), val, this.bpm))
              );
            }
            for (Thread thread : threads) {
              thread.start();
            }
            for (Thread thread : threads) {
              thread.join();
            }
          }
      } catch (InterruptedException e) {
        /* ignore  */
      }
        break;
      default:
        return new __bottom();
    }
    return new __done();
  }
}
