package org.kedar.base;

import java.util.concurrent.Semaphore;

public class UsingBinarySemaphore {
  public static void main(String[] args) throws Exception { 
    Semaphore bin = new Semaphore(1); //a binary semaphore
    SemResource r = new SemResource(bin);
    Thread t1 = new Putter(r);
    Thread t2 = new Getter(r);
    t1.start();
    Thread.sleep(3000);
    t2.start();
  }
  private static class Putter extends Thread {
    private final SemResource r;
    public Putter(SemResource r) {
      this.r = r;
    }
    public void run() {
      try {
        r.put(423);
      } catch (InterruptedException i) {
        System.out.println(Thread.currentThread().getName() + " interrupted ...");
      }
    }
  }
  private static class Getter extends Thread {
    private final SemResource r;
    public Getter(SemResource r) {
      this.r = r;
    }
    public void run() {
      System.out.println(Thread.currentThread().getName() + " got " + r.get());
    }
  }
}

class SemResource {
  private final Semaphore s;
  private int x;
  public SemResource(Semaphore s) {
    this.s = s;
    this.x = 12;
  }
  public void put(int x) throws InterruptedException {
    s.acquire();
    Thread.currentThread().sleep(200000);
    this.x = x; 
  }
  public int get() {
    System.out.println("Before release: #permits available = " + s.availablePermits());
    s.release();
    System.out.println("After release: #permits available = " + s.availablePermits());
    System.out.println(Thread.currentThread().getName() + " gets it");
    return x;
  }
}
