package org.kedar.base; /**
 * The TTASLock in Herlihy/Shavit implemented in Java.
 * The threads have to call lock and unlock on this class before
 * doing anything critical.
 * Of course, one can use AtomicBoolean directly, but just for fun ...
*/
import java.util.concurrent.atomic.AtomicBoolean;

public final class TestAndTestAndSetLock extends OurLock {
  private final AtomicBoolean theLock = new AtomicBoolean(false); //init: unlocked
  public void lock() {
    while (true) {
      while (theLock.get()) {/*spin while it's locked */}
      if (!theLock.getAndSet(true)) //sets to true, should return false
        return;
    }
  }
  public void unlock() {
    //simply, unconditionally free the lock
    theLock.set(false);
  }
}

