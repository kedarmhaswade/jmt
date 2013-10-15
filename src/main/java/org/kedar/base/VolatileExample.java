package org.kedar.base;

public class VolatileExample {
  int x = 0;
  volatile boolean v = false;

  void write() {
    x = 42;
    v = true;
  }
  void read() {
    if (v) {
      int y = 100/x;
      System.out.println("y = " + y);
    }
  }
  public static void main(String[] args) { 
    VolatileExample ve = new VolatileExample();
    new Thread(new Writer(ve)).start();
    new Thread(new Reader(ve)).start();
  }
}

class Writer implements Runnable {
  private final VolatileExample ve;
  Writer(VolatileExample ve) {
    this.ve = ve;
  }
  public void run() {
    ve.write();
    ParallelizableComputations.isPrimeSlow(new java.util.Random().nextInt(1_000_000));
  }
}

class Reader implements Runnable {
  private final VolatileExample ve;
  Reader(VolatileExample ve) {
    this.ve = ve;
  }
  public void run() {
    ve.read();
    ParallelizableComputations.isPrimeSlow(new java.util.Random().nextInt(2_000_000));
  }
}
