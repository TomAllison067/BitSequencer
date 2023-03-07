package BitSequencer.Bindings;

public class IntegerBinding implements BindingInterface{
  private Object value;

  public IntegerBinding(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }
}
