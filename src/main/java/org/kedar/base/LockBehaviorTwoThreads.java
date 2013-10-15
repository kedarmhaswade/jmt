package org.kedar.base;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class LockBehaviorTwoThreads {
  static volatile long ctr = 0;
  public static void main(String[] args) throws Exception { 
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(2);
    Lock lock = new ReentrantLock();
    for (int i = 0; i < 2; ++i) // create and start threads
      new Thread(new LockCounter(startSignal, doneSignal, lock)).start();
    ParallelizableComputations.isPrimeSlow(542342341);
    startSignal.countDown();
    doneSignal.await();
    System.out.println("counter: " + ctr);
  }
}

class LockCounter implements Runnable {
  private final CountDownLatch startSignal;
  private final CountDownLatch doneSignal;
  private final Lock lock;

  LockCounter(CountDownLatch startSignal, CountDownLatch doneSignal, Lock lock) {
    this.startSignal = startSignal;
    this.doneSignal = doneSignal;
    this.lock = lock;
  }
  public void run() {
    try {
      startSignal.await();
    } catch(InterruptedException ie) {
      //ignore
    }
    lock.lock();
    try {
      for (int i = 0 ; i < 500_000 ; i++) 
        LockBehaviorTwoThreads.ctr++;
    } finally {
      lock.unlock();
    }
    doneSignal.countDown();
  }
}

