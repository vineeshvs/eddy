abstract class X {
  public void x();

  static X makeX() {
    X r = new X()<caret> {
      public void x() {}
    }

    return r;
  }
}