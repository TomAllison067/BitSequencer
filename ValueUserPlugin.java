import uk.ac.rhul.cs.csle.art.util.ARTException;
import uk.ac.rhul.cs.csle.art.value.*;

import MidiPlayer.MiniMusicPlayer;
import MidiPlayer.PhraseFactory;

public class ValueUserPlugin implements ValueUserPluginInterface {
  private MiniMusicPlayer musicPlayer = new MiniMusicPlayer();
  private PhraseFactory phraseFactory = new PhraseFactory();
  
  enum OpCode {
    SET_BPM,                      // 0
    PLAY_PHRASE,                  // 1    
    MODULATE_PHRASE,              // 2
    CONCATENATE_PHRASE,           // 3
    REPEAT_PHRASE,                // 4
    PRINT_AVAILABLE_INSTRUMENTS,  // 5
    SET_INSTRUMENT;               // 6
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
        musicPlayer.setBpm((int) args[1].value());
        break;
      case PLAY_PHRASE:     // 1
        /**
         * Argument 1: the channel
         * Argument 2: the phrase to play
         */
        int channel = (int) args[1].value();
        String phrase = (String) args[2].value();
        musicPlayer.playPhrase(channel, phraseFactory.constructNoteListFromPhraseString(phrase));
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
      case PRINT_AVAILABLE_INSTRUMENTS:
        musicPlayer.printAllInstruments();
        break;
      case SET_INSTRUMENT:
        // Set a given midi channel (between 0-15) to an instrument (0-127)
        int channelIndex = (int) args[1].value();
        int instrument = (int) args[2].value();
        musicPlayer.setInstrument(channelIndex, instrument);
        break;
      default:
        return new __bottom();
    }
    return new __done();
  }
}
