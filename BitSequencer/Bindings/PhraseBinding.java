package BitSequencer.Bindings;

public class PhraseBinding implements BindingInterface {
  private String id;
  private Object value;

  public String getId() {
    return this.id;
  }

  public Object getValue() {
    return this.value;
  }
}
