package org.kedar.base; /** A class that embodies a critical section the 'execute' method of which must be
 * run by only one thread at a time. */
import java.util.*;
public final class CriticalSection {
  
  public static void execute() {
    Integer[] a = new Integer[1_000_000];
    for (int i = 0 ; i < a.length ; i++)
      a[i] = i;
    List<Integer> l = Arrays.asList(a);
    Collections.shuffle(l);
    Arrays.sort(l.toArray(a));
    System.out.println(a[0] + ", " + a[a.length-1]);
  }
  public static void report() {
  }
}

