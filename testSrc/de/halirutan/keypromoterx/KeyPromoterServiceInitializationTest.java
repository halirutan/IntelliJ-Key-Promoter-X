package de.halirutan.keypromoterx;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class KeyPromoterServiceInitializationTest extends TestCase {
  public void testSkipsObservationsUntilInitializationCompletes() {
    List<Runnable> tasks = new ArrayList<>();
    AtomicInteger initialized = new AtomicInteger();
    var initialization = new KeyPromoterServiceInitialization.Initialization(tasks::add, initialized::incrementAndGet);

    assertFalse(initialization.ensureReady());
    assertFalse(initialization.ensureReady());
    assertEquals(1, tasks.size());
    assertEquals(0, initialized.get());

    tasks.getFirst().run();

    assertTrue(initialization.ensureReady());
    assertTrue(initialization.whenReady().getNow(false));
    assertEquals(1, initialized.get());
    assertEquals(1, tasks.size());
  }

  public void testConcurrentRequestsScheduleOnlyOneInitialization() throws Exception {
    AtomicInteger submissions = new AtomicInteger();
    var initialization = new KeyPromoterServiceInitialization.Initialization(task -> submissions.incrementAndGet(), () -> {
    });
    CountDownLatch start = new CountDownLatch(1);
    List<Thread> callers = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      Thread caller = new Thread(() -> {
        try {
          start.await();
          initialization.ensureReady();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      callers.add(caller);
      caller.start();
    }
    start.countDown();
    for (Thread caller : callers) {
      caller.join(5000);
      assertFalse(caller.isAlive());
    }
    assertEquals(1, submissions.get());
    initialization.dispose();
  }

  public void testFailureDoesNotPublishReadinessOrRetryOnEachObservation() {
    List<Runnable> tasks = new ArrayList<>();
    var initialization = new KeyPromoterServiceInitialization.Initialization(tasks::add, () -> {
      throw new IllegalStateException("Cannot load state");
    });
    CompletableFuture<Boolean> completion = initialization.whenReady();
    tasks.getFirst().run();

    assertFalse(completion.getNow(true));
    assertFalse(initialization.ensureReady());
    assertEquals(1, tasks.size());
  }

  public void testRejectedSubmissionCompletesWithoutBlockingOrRetrying() {
    AtomicInteger submissions = new AtomicInteger();
    var initialization = new KeyPromoterServiceInitialization.Initialization(task -> {
      submissions.incrementAndGet();
      throw new RejectedExecutionException();
    }, () -> fail("Rejected task must not run"));

    assertFalse(initialization.ensureReady());
    assertFalse(initialization.whenReady().getNow(true));
    assertFalse(initialization.ensureReady());
    assertEquals(1, submissions.get());
  }

  public void testCancellationDoesNotPublishReadiness() {
    var initialization = new KeyPromoterServiceInitialization.Initialization(Runnable::run, () -> {
      throw new CancellationException();
    });

    assertFalse(initialization.ensureReady());
    assertFalse(initialization.whenReady().getNow(true));
  }

  public void testDisposalBeforeFirstRequestDoesNotScheduleWork() {
    var initialization = new KeyPromoterServiceInitialization.Initialization(task -> fail("Disposed service must not submit tasks"),
        () -> fail("Disposed initializer must not run"));
    initialization.dispose();

    assertFalse(initialization.ensureReady());
    assertFalse(initialization.whenReady().getNow(true));
  }

  public void testDisposalBeforeExecutionCancelsQueuedInitialization() {
    List<Runnable> tasks = new ArrayList<>();
    var initialization = new KeyPromoterServiceInitialization.Initialization(tasks::add, () -> fail("Disposed task must not run"));
    CompletableFuture<Boolean> completion = initialization.whenReady();

    initialization.dispose();
    tasks.getFirst().run();

    assertFalse(completion.getNow(true));
    assertFalse(initialization.ensureReady());
    assertEquals(1, tasks.size());
  }

  public void testDisposalDuringInitializationDoesNotPublishReadiness() throws Exception {
    List<Runnable> tasks = new ArrayList<>();
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    AtomicBoolean interrupted = new AtomicBoolean();
    var initialization = new KeyPromoterServiceInitialization.Initialization(tasks::add, () -> {
      started.countDown();
      try {
        if (!release.await(5, TimeUnit.SECONDS)) {
          throw new AssertionError("Initializer was not released");
        }
      } catch (InterruptedException e) {
        interrupted.set(true);
        Thread.currentThread().interrupt();
        throw new AssertionError("Disposal must not interrupt state loading", e);
      }
    });
    CompletableFuture<Boolean> completion = initialization.whenReady();
    Thread worker = new Thread(tasks.getFirst());
    worker.start();
    try {
      assertTrue(started.await(5, TimeUnit.SECONDS));
      initialization.dispose();
      assertFalse(completion.getNow(true));
    } finally {
      release.countDown();
      worker.join(5000);
    }
    assertFalse(worker.isAlive());
    assertFalse(interrupted.get());
    assertFalse(initialization.ensureReady());
  }

  public void testCompletionCallbacksRunOutsideLifecycleLock() {
    List<Runnable> tasks = new ArrayList<>();
    var initialization = new KeyPromoterServiceInitialization.Initialization(tasks::add, () -> {
    });
    CompletableFuture<Boolean> callback = initialization.whenReady().thenApply(ready -> {
      assertFalse(Thread.holdsLock(initialization));
      return ready;
    });

    tasks.getFirst().run();

    assertTrue(callback.getNow(false));
  }
}
