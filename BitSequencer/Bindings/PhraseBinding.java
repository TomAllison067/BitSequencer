package BitSequencer.Bindings;

public class PhraseBinding implements BindingInterface {
  private Object value;

  public PhraseBinding(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }
}
