package org.kedar.base;

// demonstrates the behavior of JLS -- table 17.1 -- compiler/processor reorders
class IncorrectSync171 {
  static int A = 0;
  static int B = 0;
  public static void main(String[] args) throws Exception { 
    Thread t1 = new Thread() {
      public void run() {
        int r2 = A;
        B = 1;
        System.out.println("t1:run, r2 = " + r2);
      }
    };
    Thread t2 = new Thread() {
      public void run() {
        int r1 = B;
        A = 2;
        System.out.println("t2:run, r1 = " + r1);
      }
    };
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println("A: " + A + ", B: " + B);
  }
}

