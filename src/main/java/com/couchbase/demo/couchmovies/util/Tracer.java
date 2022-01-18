package com.couchbase.demo.couchmovies.util;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;


public class Tracer<T> {

    public static final String PROGRESS_STATUS_CHECKPOINT = "CHECKPOINT";
    public static final String PROGRESS_STATUS_START = "START ----------";
    public static final String PROGRESS_STATUS_END = "END ------------";
    private static final String PROGRESS_STATUS_MSG = "[%s] %s OK: [%s] ERRORS: [%s]";
    private static final String PROGRESS_STATUS_ERROR_MSG = "[%s] %s";
    public static int DEFAULT_CHECKPOINT = 50000;
    private int checkpoint;
    private Logger logger;
    private AtomicInteger oks, errors;
    private String task;

    public Tracer(Logger logger, String task, int checkpoint) {

        this.logger = logger;
        this.checkpoint = checkpoint;
        this.task = task;
        oks = new AtomicInteger(0);
        errors = new AtomicInteger(0);
        log(PROGRESS_STATUS_START);

    }

    public Tracer(Logger logger, String task) {
        this(logger, task, DEFAULT_CHECKPOINT);
    }

    public void onComplete() {
        log(PROGRESS_STATUS_END);
    }

    public void onError(Throwable t) {
        errors.getAndIncrement();
        if (errors.get() % checkpoint == 0) {
            log(PROGRESS_STATUS_CHECKPOINT);
            logError(t);
        }

    }

    public void onNext(T t, boolean isConsumer) {
        if (t != null) {

            oks.getAndIncrement();
            if (oks.get() % checkpoint == 0)
                log(PROGRESS_STATUS_CHECKPOINT);

        } else

            errors.getAndIncrement();


    }

    public void onNext(T t) {
        this.onNext(t, false);
    }


    public void logError(Throwable t) {
        logger.error(String.format(PROGRESS_STATUS_ERROR_MSG, task, t.getMessage()), t);
    }

    private void log(String status) {

        logger.debug(String.format(PROGRESS_STATUS_MSG, task, status, oks, errors));

    }
}
