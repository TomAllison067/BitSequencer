package BitSequencer;

import java.util.List;

public class RunnablePlayer implements Runnable {
  private PhraseFactory phraseFactory = new PhraseFactory();
  private MiniMusicPlayer musicPlayer;
  private int channel;
  private List<Measure> phrase;

  public RunnablePlayer(int channel, int instrument, String phrase, int bpm, MiniMusicPlayer musicPlayer) {
    this.channel = channel;
    this.phrase = phraseFactory.constructMeasureListFromPhraseString(phrase);
    this.musicPlayer = musicPlayer;
    musicPlayer.setInstrument(channel, instrument);
    musicPlayer.setBpm(bpm);
  }

  public void run() {
    musicPlayer.playPhrase(this.channel, this.phrase);
  }
}
