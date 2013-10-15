package org.kedar.base;

import java.util.concurrent.*;
public class SynchronizedBehaviorTwoThreads {
  static volatile long ctr = 0;
  public static void main(String[] args) throws Exception { 
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(2);
    for (int i = 0; i < 2; ++i) // create and start threads
      new Thread(new Counter(startSignal, doneSignal)).start();
    ParallelizableComputations.isPrimeSlow(542342341);
    startSignal.countDown();
    doneSignal.await();
    System.out.println("counter: " + ctr);
  }
}

class Counter implements Runnable {
  private final CountDownLatch startSignal;
  private final CountDownLatch doneSignal;

  Counter(CountDownLatch startSignal, CountDownLatch doneSignal) {
    this.startSignal = startSignal;
    this.doneSignal = doneSignal;
  }
  public void run() {
    try {
      startSignal.await();
    } catch(InterruptedException ie) {
      //ignore
    }
    synchronized(SynchronizedBehaviorTwoThreads.class) {
      for (int i = 0 ; i < 500_000 ; i++) 
        SynchronizedBehaviorTwoThreads.ctr++;
    }
    doneSignal.countDown();
  }
}

