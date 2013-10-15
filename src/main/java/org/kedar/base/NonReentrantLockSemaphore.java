package org.kedar.base;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * An implementation of a non-reentrant lock using binary semaphore.
 */
public class NonReentrantLockSemaphore implements Lock {

    private final Semaphore s;

    public NonReentrantLockSemaphore() {
        this.s = new Semaphore(1);
    }
    @Override
    public void lock() {
        try {
            s.acquire();
        } catch (InterruptedException i) {
            //ignore
        }
    }

    @Override
    public void unlock() {
        s.release();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
