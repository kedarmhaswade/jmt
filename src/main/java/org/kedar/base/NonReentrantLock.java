package org.kedar.base;

import java.util.concurrent.atomic.*;

interface SomeLock {
  void lock() throws InterruptedException;
  void unlock();
}
public class NonReentrantLock implements SomeLock {
  private volatile long acquirer = 0L; //the thread_id
  NonReentrantLock() {
  }
  public void lock() throws InterruptedException {
    long tid = ThreadID.get();
    while (acquirer != tid) {} //spin
    acquirer += 1L; //now when the same thread again asks it, it will spin
    System.out.println("Locking thread's id: " + ThreadID.get());
  }
  public void unlock() {
    acquirer += 1L;
    System.out.println("Unlocking thread's id: " + ThreadID.get());
  }
  public static void main(String[] args) { 
    SomeLock lock = new NonReentrantLock();
    Resource res = new Resource(lock);
    Thread[] threads = new Thread[1];
    for (int i = 0 ; i < threads.length ; i++) 
      threads[i] = new Worker(res);
    for (int i = 0 ; i < threads.length ; i++) 
      threads[i].start();
  }
  private static class Worker extends Thread {
    private final Resource resource;
    public Worker (Resource res) {
      this.resource = res;
    }
    public void run() {
      try {
        resource.first(); //in turn calls second method that needs the same lock
      } catch (InterruptedException iwi) {
        System.out.println("Thread " + ThreadID.get() + " was interrupted");
      }
    }
  }
}

class Resource {
  int value = 100;
  private final SomeLock lock;
  public Resource(SomeLock lock) {
    this.lock = lock;
  }
  void first() throws InterruptedException {
    lock.lock();
    try {
      value += 2;
      System.out.println("first, value incremented to: " + value + " by thread " + ThreadID.get());
      this.second();
    } finally {
      lock.unlock();
    }
  }
  void second() throws InterruptedException {
    lock.lock();
    try {
      value += 3;
      System.out.println("second, value incremented to: " + value + " by thread " + ThreadID.get());
    } finally {
      lock.unlock();
    }
  }
}

class ThreadID {
  private static final AtomicLong id = new AtomicLong(0);
  
  private static final ThreadLocal<Long> tid =
    new ThreadLocal<Long>() {
      @Override public Long initialValue() {
        return id.getAndIncrement();
      }
    };
  
  public static long get() {
    return tid.get();
  }
  public static void set(long ms) {
    tid.set(ms);
  }
}
