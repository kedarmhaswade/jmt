package org.kedar.base;

public final class FinalVsVolatile {
  int xField;
 // volatile int xField;
  public FinalVsVolatile(int xField) {
    this.xField = xField;
  }
  public static void main(String[] args) { 
    new FinalVsVolatile(4);
    int y;
  }
}
