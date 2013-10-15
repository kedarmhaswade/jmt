package org.kedar.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
public class DiningPhilosophers {

  volatile static boolean done;
  final private Integer[] chopsticks;

  public DiningPhilosophers(int n) {
    chopsticks = new Integer[n];
    for (int i = 0 ; i < n ; i++)
      chopsticks[i] = i;
  }
  public DiningPhilosophers start() {
    CountDownLatch startSignal = new CountDownLatch(1);
    for (int i = 0 ; i < chopsticks.length ; i++) {
      new Thread(new DiningPhilosopher(i, chopsticks, startSignal)).start();
    }
    startSignal.countDown();
    return this;
  }
  public DiningPhilosophers run(long ms) {
    try {
      Thread.sleep(ms);
    } catch(InterruptedException ie) {
      System.out.println(Thread.currentThread().getName() + " was interrupted!");
    }
    DiningPhilosophers.done = true;
    return this;
  }
  public void report() {
  }
  public static void main(String[] args) throws Exception { 
    if (args.length != 2) {
      System.out.println("java org.kedar.base.DiningPhilosophers <no-of-philosophers> <time-of-simulation-ms>");
      System.exit(1);
    }
    new DiningPhilosophers(Integer.valueOf(args[0])).start().run(Integer.valueOf(args[1])).report();
  }
}


class DiningPhilosopher implements Runnable {

  private final int id; // id's start at 0
  private final Integer[] chopsticks; //individual elements are shared
  private final CountDownLatch startSignal;
  public DiningPhilosopher(int id, Integer[] chopsticks, CountDownLatch startSignal) {
    if (id < 0 || chopsticks == null || id >= chopsticks.length) {
      throw new IllegalArgumentException("Provide proper array and proper id for me -- dining philosopher");
    }
    this.id = id;
    this.chopsticks = chopsticks;
    this.startSignal = startSignal;
  }
  public int leftChopstickIndex() {
    return id; 
  }
  public int rightChildIndex() {
    return (id + 1) % chopsticks.length;
  }

  public void run() {
    try {
      startSignal.await(); //wait for the latch
    } catch(InterruptedException ie) {
      System.out.println(Thread.currentThread().getName() + " was interrupted");
    }
    while(!DiningPhilosophers.done) {
      //deadlock prone -- grab the chocksticks and well, eat!
      synchronized(chopsticks[leftChopstickIndex()]) {
        synchronized(chopsticks[rightChildIndex()]) {
          eat();
        }
      }
      //release the chopsticks and well, think!
      think();
    }
    System.out.println("DP " + id + " ate for: " + EatingTime.get() + " ms");
    System.out.println("DP " + id + " thought for: " + ThinkingTime.get() + " ms");
  }

  public void eat() {
    long t1 = System.currentTimeMillis();
    for (int i = 2 ; i < 10_000 ; i++) {
        ParallelizableComputations.isPrimeSlow(i);
    }
    //System.out.println("DP " + id + " done eating");
    EatingTime.set(EatingTime.get() + (System.currentTimeMillis() - t1));
  }
  public void think() {
    long t1 = System.currentTimeMillis();
    for (int i = 2 ; i < 20_000 ; i++) {
        ParallelizableComputations.isPrimeSlow(i);
    }
    //System.out.println("DP " + id + " done thinking");
    ThinkingTime.set(ThinkingTime.get() + (System.currentTimeMillis() - t1));
  }
}

class EatingTime {
  private static final AtomicLong total = new AtomicLong(0);
  
  private static final ThreadLocal<Long> eatingTime =
    new ThreadLocal<Long>() {
      @Override public Long initialValue() {
        //return total.getAndIncrement();
        return 0L;
      }
      @Override public void set(Long ms) {
        total.set(ms);
      }
      @Override public Long get() {
        return total.get();
      }
    };
  
  public static long get() {
    return eatingTime.get();
  }
  public static void set(long ms) {
    eatingTime.set(ms);
  }
}

class ThinkingTime {
  private static final AtomicLong total = new AtomicLong(0);
  
  private static final ThreadLocal<Long> thinkingTime =
    new ThreadLocal<Long>() {
      @Override public Long initialValue() {
        //return total.getAndIncrement();
        return 0L;
      }
      @Override public void set(Long ms) {
        total.set(ms);
      }
      @Override public Long get() {
        return total.get();
      }
    };
  
  public static long get() {
    return thinkingTime.get();
  }
  public static void set(long ms) {
    thinkingTime.set(ms);
  }
}
