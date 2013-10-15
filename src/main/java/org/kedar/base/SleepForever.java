package org.kedar.base;

class SleepForever {
  static boolean done = false; //look ma! no volatile
  public static void main(String[] args) throws Exception { 
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        while (!done)
          try {
            long st = 4000;
            long t1 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(st);
            System.out.print("Was asked to sleep for: " + st + " ms, and ");
            System.out.println("Had been sleeping for: " + (System.currentTimeMillis() - t1) + " ms");
          } catch(InterruptedException ie) {
            System.out.println("Uh oh! " + Thread.currentThread().getName() + " interrupted");
          }
      }
    });
    t1.start();
    Thread.sleep(10);
    done = true;
  }
}

