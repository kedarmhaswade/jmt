package org.kedar.base;

import org.junit.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

/**
 * Tests the NonReentrantLockSemaphore.
 */
public class NonReentrantLockSemaphoreTest {
    @Test
    public void constructorOK() {
        Lock l = new NonReentrantLockSemaphore();
        assertNotNull(l);
    }
    @Test
    public void threadBlocks() throws InterruptedException {
        final DoublyLockingResource res = new DoublyLockingResource(new NonReentrantLockSemaphore());
        Thread t = new Thread(){
            public void run() {
                try {
                    res.m1();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };
        assertEquals(t.getState(), Thread.State.NEW);
        t.start();
        while(!res.holdingOnce)
            Thread.sleep(1000);
        System.out.println(t.getState());
        assertEquals(Thread.State.WAITING, t.getState());
    }

    @Test
    public void binarySemaphoreWaitsWithSameThreadAcquiringTwice() throws InterruptedException {
        Semaphore bs = new Semaphore(1, true);
        TwoTimeAcquirer target = new TwoTimeAcquirer(bs);
        Thread t = new Thread(target);
        t.start();
        while(!target.acquiredOnce)
            Thread.sleep(1000);
        assertEquals(Thread.State.WAITING, t.getState());
    }

    @Test
    public void secondThreadBlocksWithBinarySemaphore() throws InterruptedException {
        Semaphore bs = new Semaphore(1);
        OneTimeAcquirer r1 = new OneTimeAcquirer(bs);
        Thread t1 = new Thread(r1);
        OneTimeAcquirer r2 = new OneTimeAcquirer(bs);
        Thread t2 = new Thread(r2);
        t1.start();
        while (!r1.acquired)
            Thread.sleep(1000);
        assertEquals(0, bs.availablePermits());
        t2.start();
        while(!r2.running)
            Thread.sleep(1000);
        assertEquals(Thread.State.BLOCKED, t2.getState());
    }


    private static class OneTimeAcquirer implements Runnable {
        private final Semaphore s;
        volatile boolean acquired = false;
        volatile boolean running = false;
        public OneTimeAcquirer(Semaphore s) {
            this.s = s;
        }
        public void run() {
            try {
                running = true;
                s.acquire();
                this.acquired = true;
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    private static class TwoTimeAcquirer implements Runnable {
        private final Semaphore s;
        volatile boolean acquiredOnce = false;
        public TwoTimeAcquirer(Semaphore s) {
            this.s = s;
        }
        public void run() {
            try {
                s.acquire();
                acquiredOnce = true;
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    private static class DoublyLockingResource {
        private final Lock lock;
        volatile boolean holdingOnce = false;
        private int x; // to be incremented after holding lock
        public DoublyLockingResource(Lock lock) {
            this.lock = lock;
            this.x = 1;
        }
        public void m1() throws InterruptedException {
            lock.lock();
            try {
                x += 2;
                holdingOnce = true;
                m2();
            } finally {
                lock.unlock();
            }
        }

        private void m2() throws InterruptedException {
            lock.lock();
            try {
                x += 3;
            } finally {
                lock.unlock();
                holdingOnce = false;
            }
        }
    }
}
