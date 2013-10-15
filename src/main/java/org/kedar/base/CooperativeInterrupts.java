package org.kedar.base;

import java.util.concurrent.TimeUnit;
import java.math.BigInteger;
public class CooperativeInterrupts {
  static volatile long ctr = 0;
  public static void main(String[] args) throws Exception { 
    Thread[] threads = new Thread[2];
    threads[0] = new Sleeper();
    threads[1] = new Waker(threads[0]);
    for (int i = 0; i < 2; ++i) // start threads
      threads[i].start();
  }
}

class Sleeper extends Thread {

  Sleeper() {
  }
  public void run() {
    try {
      TimeUnit.MINUTES.sleep(2);
    } catch(InterruptedException alarm) {
      throw new RuntimeException(alarm);
    }
  }
}

class Waker extends Thread {
  final Thread sleeper;
  Waker(Thread sleeper) {
    this.sleeper = sleeper;
  }
  public void run() {
    BigInteger p = new BigInteger("1000000000000000000000");
    System.out.println(p.nextProbablePrime());
    sleeper.interrupt();
  }
}
