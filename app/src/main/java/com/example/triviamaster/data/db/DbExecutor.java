package com.example.triviamaster.data.db;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DbExecutor {

    private static final DbExecutor INSTANCE = new DbExecutor();
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private DbExecutor() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static DbExecutor getInstance() {
        return INSTANCE;
    }

    public <T> void execute(Callable<T> task, DbCallback<T> callback) {
        executorService.execute(() -> {
            try {
                T result = task.call();
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onError(e);
                    }
                });
            }
        });
    }
}
