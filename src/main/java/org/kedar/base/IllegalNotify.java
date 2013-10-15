package org.kedar.base;

class IllegalNotify {
  public static void main(String[] args) { 
    Object lock = new Object();
    lock.notify();
  }
}

