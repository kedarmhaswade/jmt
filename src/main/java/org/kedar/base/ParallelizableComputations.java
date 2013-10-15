package org.kedar.base;

/** Embodies certain Parallelizable Computations */
public final class ParallelizableComputations {
  private ParallelizableComputations() {};

  public static boolean isPrimeSlow(long n) {
    for (long i = 2L; i*i < n ; i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }
  public static void main(String[] args) throws Exception {
    System.out.println(isPrimeSlow(Long.valueOf(args[0])));
  }
}

