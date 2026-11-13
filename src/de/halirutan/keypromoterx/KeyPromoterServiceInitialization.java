package de.halirutan.keypromoterx;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * Initializes persistent services off EDT; observations during initialization are intentionally dropped.
 */
@Service(Service.Level.APP)
public final class KeyPromoterServiceInitialization implements Disposable {
  private static final Logger LOG = Logger.getInstance(KeyPromoterServiceInitialization.class);

  private final Initialization initialization = new Initialization(
      command -> ApplicationManager.getApplication().executeOnPooledThread(command),
      () -> {
        try {
          if (isDisposed() || ApplicationManager.getApplication().isDisposed()) {
            throw new CancellationException();
          }
          ApplicationManager.getApplication().getService(KeyPromoterSettings.class);
          if (isDisposed() || ApplicationManager.getApplication().isDisposed()) {
            throw new CancellationException();
          }
          ApplicationManager.getApplication().getService(KeyPromoterStatistics.class);
        } catch (ProcessCanceledException e) {
          throw e;
        } catch (RuntimeException | Error e) {
          if (!(e instanceof CancellationException)) {
            LOG.warn("Cannot initialize Key Promoter X services", e);
          }
          throw e;
        }
      });

  static KeyPromoterServiceInitialization getInstance() {
    return ApplicationManager.getApplication().getService(KeyPromoterServiceInitialization.class);
  }

  boolean shouldSkipObservation() {
    return !initialization.ensureReady();
  }

  CompletableFuture<Boolean> whenReady() {
    return initialization.whenReady();
  }

  boolean isDisposed() {
    return initialization.disposed;
  }

  @Override
  public void dispose() {
    initialization.dispose();
  }

  /**
   * Separate state machine so scheduling and disposal can be tested without an IDE.
   */
  static final class Initialization implements Disposable {
    private final Executor executor;
    private final Runnable initializer;
    private final CompletableFuture<Boolean> completion = new CompletableFuture<>();
    private volatile boolean ready;
    private volatile boolean disposed;
    private FutureTask<Void> task;

    Initialization(Executor executor, Runnable initializer) {
      this.executor = executor;
      this.initializer = initializer;
    }

    boolean ensureReady() {
      if (!ready) {
        whenReady();
      }
      return ready && !disposed;
    }

    CompletableFuture<Boolean> whenReady() {
      FutureTask<Void> newTask;
      synchronized (this) {
        if (disposed || task != null) {
          return completion;
        }
        newTask = new FutureTask<>(() -> {
          try {
            if (!disposed) {
              initializer.run();
            }
            finish(true);
          } catch (RuntimeException | Error e) {
            finish(false);
            throw e;
          }
        }, null);
        task = newTask;
      }
      try {
        executor.execute(newTask);
      } catch (RuntimeException e) {
        newTask.cancel(false);
        finish(false);
      }
      return completion;
    }

    private void finish(boolean success) {
      boolean initialized;
      synchronized (this) {
        initialized = success && !disposed;
        ready = initialized;
      }
      // Completing a future can invoke UI-scheduling callbacks inline: never do it under our lock.
      completion.complete(initialized);
    }

    @Override
    public void dispose() {
      FutureTask<Void> pendingTask;
      synchronized (this) {
        disposed = true;
        ready = false;
        pendingTask = task;
      }
      if (pendingTask != null) {
        // Do not interrupt persistent-state I/O already running in the platform container.
        pendingTask.cancel(false);
      }
      completion.complete(false);
    }
  }
}
