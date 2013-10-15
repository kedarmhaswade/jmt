package org.kedar.base;

import java.util.concurrent.CountDownLatch;
public class Experiment {
  public static void main(String[] args) throws Exception { 
    
       CountDownLatch startSignal = new CountDownLatch(1);
       int nt = Integer.valueOf(args[0]);
       CountDownLatch doneSignal = new CountDownLatch(nt);
       OurLock lock = null;
       int with = Integer.valueOf(args[1]);
       if (with == 1) {
         lock = new TestAndSetLock();
       } else if (with == 2) {
         lock = new TestAndTestAndSetLock();
       } else {
         System.out.println("java org.kedar.base.Experiment no-of-threads lock-id (1:TAS, 2:TTAS)");
         System.exit(1);
       }
       long t1 = System.currentTimeMillis();
       for (int i = 0; i < nt; ++i) // create and start threads
         new Thread(new Worker(startSignal, doneSignal, lock)).start();
  
       doSomethingElse("start");            // don't let run yet
       startSignal.countDown();      // let all threads proceed
       doneSignal.await();           // wait for all to finish
       doSomethingElse("end");
       long t2 = System.currentTimeMillis();
       System.out.println("Time taken: " + (t2 - t1));
  }
  static void doSomethingElse(String s) throws Exception {
    System.out.println(s);
    Thread.sleep(10);
  }
}

class Worker implements Runnable {
     private final CountDownLatch startSignal;
     private final CountDownLatch doneSignal;
     private final OurLock lock;
     Worker(CountDownLatch startSignal, CountDownLatch doneSignal, OurLock lock) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.lock = lock;
     }
     public void run() {
        try {
          startSignal.await();
          doWork();
          doneSignal.countDown();
        } catch (InterruptedException ex) {} // return;
     }
  
     void doWork() { 
      try {
        lock.lock();
        CriticalSection.execute();
      } catch(Exception e) {
      } finally {
        lock.unlock();
      }

     }
}

