package org.kedar.base;

class InterruptWaiter {
  final static Object lock = new Object();
  public static void main(String[] args) throws Exception { 
    Thread t = new Thread() {
      public void run() {
        try {
          synchronized (lock) {
            lock.wait(); //wait until notification comes
            System.out.println("I was notified");
            System.out.println("Am I interrupted? " + Thread.interrupted());
          }
        } catch(InterruptedException e) {
          System.out.println(Thread.currentThread().getName()  + " was interrupted");
        }
      }
    };
    t.start();
    Thread.sleep(100);
    t.interrupt();
    synchronized (lock) {
      lock.notify();
    }
    t.join();
    System.out.println(t.isInterrupted());
  }
}

