package org.kedar.base; /**
 * This program tries to demonstrate the surprising sequential consistency.
 * A program is sequentially consistent as long as calls made in a single
 * thread follow the program order. Program order however, does not apply
 * to the executions across threads.
 * It's hard to demonstrate that a counterintuitive behavior might actually
 * be sequentially consistent. Let's say, we have a FIFO queue, q and two threads: A, B
 *
 * Thread A does:
 *   q.enq(base); returns at time 1.
 *   q.deq();  returns at time 3.
 *
 * Thread B does:
 *   q.enq(y); returns at time 2.
 *
 * Thus, q.enq(base) in thread A returns before q.enq(y) in thread B and yet, is it possible
 * for q.deq() in thread A to return y, while the program remaining sequentially consistent?
 * 
 * The answer is yes!
 */
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class SurprisingSequentialConsistency {
  public static void main(String[] args) { 
    AtomicInteger counter = new AtomicInteger(0);
    BlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(1<<20);
    new Thread(new ThreadA(counter, q)).start();
    new Thread(new ThreadB(counter, q)).start();
  }
}
class Reporter {
  volatile static long aput_time = 0;
  volatile static long bput_time = 0;
}
class ThreadA implements Runnable {
  private final AtomicInteger counter;
  private final BlockingQueue<Integer> q;
  public ThreadA(AtomicInteger counter, BlockingQueue<Integer> q) {
    this.counter = counter;
    this.q = q;
  }
  public void run() {
    int upto = 0;
    while(upto++ <= 100) {
      try {
        int i = counter.getAndIncrement();
        q.put(i);
        Reporter.aput_time = System.currentTimeMillis();
        int y = q.take();
        long aput_time = Reporter.aput_time;
        long bput_time = Reporter.bput_time;
        if (aput_time < bput_time && y != i) {
          System.out.println("surprising!");
          System.out.println("aput_time: " + aput_time);
          System.out.println("bput_time: " + bput_time);
          System.out.println("put value: " + i);
          System.out.println("took value: " + y);
        }
      } catch(InterruptedException ie) {//ignore
      }
    }
  }
}
class ThreadB implements Runnable {
  private final AtomicInteger counter;
  private final BlockingQueue<Integer> q;
  public ThreadB(AtomicInteger counter, BlockingQueue<Integer> q) {
    this.counter = counter;
    this.q = q;
  }
  public void run() {
    int upto = 0;
    while(upto++ <= 100) {
      try {
        int i = counter.getAndIncrement();
        q.put(i);
        Reporter.bput_time = System.currentTimeMillis();
      } catch (InterruptedException ie) { //ignore
      }
    }
  }
}
