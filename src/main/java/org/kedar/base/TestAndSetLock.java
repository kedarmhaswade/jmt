package org.kedar.base; /**
 * The TASLock in Herlihy/Shavit implemented in Java.
 * The threads have to call lock and unlock before
 * doing anything critical.
 * Of course, one can use AtomicBoolean directly, but just for fun ...
*/
import java.util.concurrent.atomic.AtomicBoolean;

public final class TestAndSetLock extends OurLock {
  private final AtomicBoolean theLock = new AtomicBoolean(false);
  public void lock() {
    while (theLock.getAndSet(true)) {/*spin*/}
    //we locked it, proceed
  }
  public void unlock() {
    //simply, unconditionally free the lock
    theLock.set(false);
  }
}

