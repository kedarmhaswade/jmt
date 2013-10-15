package org.kedar.base;

import java.util.concurrent.locks.*;
import java.util.concurrent.Semaphore;

class AMPSemaphore extends Semaphore {
  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private final int capacity;
  int state = 0;

  public AMPSemaphore() {
    this(10);
  }
  public AMPSemaphore(int capacity) {
    super(capacity);
    this.capacity = capacity;
  }

  public void acquire() throws InterruptedException {
    lock.lock();
    try {
      if (state == capacity)
        condition.await();
      state += 1;
    } finally {
      lock.unlock();
    }
  }
  public void release() {
    lock.lock();
    try {
      state -= 1;
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }
  public static void main(String[] args) { 
  }

}
