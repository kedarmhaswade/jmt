package org.kedar.base;

class CreateTime {
  private static final ThreadLocal<Long> t =
    new ThreadLocal<Long>() {
      @Override protected Long initialValue() {
        return System.currentTimeMillis();
      }
    };
    // Returns the current thread's unique ID, assigning it if necessary
    public static Long get() {
         return t.get();
    }
}

public class UseThreadLocal {
    private static class Runner extends Thread {
      public void run() {
        int i = 0;
        while(true) {
          System.out.println("Name: " + Thread.currentThread().getName() + ", created: " + CreateTime.get());
          System.out.println("i: " + (i++));
          try {
            Thread.sleep(12000);
          } catch(InterruptedException ie) {}
        }
      }
    }
  public static void main(String[] args) throws Exception { 
    new Runner().start();
    Thread.sleep(1000);
    new Runner().start();
  }
}

