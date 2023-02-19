package MidiPlayer;

import java.util.List;

public class RunnablePlayer implements Runnable {
  private PhraseFactory phraseFactory = new PhraseFactory();
  private MiniMusicPlayer musicPlayer;
  private int channel;
  private List<Measure> phrase;

  public RunnablePlayer(int channel, int instrument, String phrase, int bpm) {
    this.channel = channel;
    this.phrase = phraseFactory.constructMeasureListFromPhraseString(phrase);
    musicPlayer = new MiniMusicPlayer();
    musicPlayer.setInstrument(channel, instrument);
    musicPlayer.setBpm(bpm);
  }

  public void run() {
    musicPlayer.playPhrase(this.channel, this.phrase);
  }
}
