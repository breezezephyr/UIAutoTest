package com.auto.test.utils;

import com.auto.test.standard.BusinessException;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 实现描述：线程操作工具类
 */
public abstract class Threads {

    private static final Logger logger = LoggerFactory.getLogger(Threads.class);
    private static final Random random = new Random();

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SafeVarargs
    public static <T> T waitAndRetry(Callable<T> task, long timeout, long interval, int errCode,
            Class<? extends Exception>... ignoreExceptions) {
        Exception lastEx = null;

        long end = System.currentTimeMillis() + timeout;
        do {
            try {
                T value = task.call();
                if (value != null) {
                    if (!Boolean.class.equals(value.getClass()))
                        return value;
                    if (Boolean.TRUE.equals(value))
                        return value;
                }
            } catch (Exception e) {
                boolean ignore = false;
                for (Class<? extends Exception> ignoreException : ignoreExceptions) {
                    if (ignoreException.isInstance(e)) {
                        ignore = true;
                        break;
                    }
                }
                if (!ignore)
                    throw BusinessException.handle(e, errCode, "重试执行失败");
                lastEx = e;
            }
            if (System.currentTimeMillis() > end)
                break;
            sleep(interval / 2 + random.nextInt((int) interval));
        } while (!Thread.interrupted());

        if (lastEx != null)
            throw new BusinessException(errCode, "重试执行超时", lastEx);
        throw new BusinessException(errCode, "重试执行超时");
    }

    public static String execute(String input, int timeout, String command) {
        Process process = null;
        try {
            process = new ProcessBuilder(command.split("\\s")).start();
            if (StringUtils.isNotBlank(input)) {
                try (OutputStream os = process.getOutputStream()) {
                    ByteStreams.copy(new ByteArrayInputStream(input.getBytes(Charsets.UTF_8)), os);
                }
            }

            if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
                // 10002 = 执行系统命令异常
                throw new BusinessException(10002, "TIMEOUT: " + command);
            }
            String out = toString(process.getInputStream());
            String err = toString(process.getErrorStream());
            if (process.exitValue() != 0) {
                // 10002 = 执行系统命令异常
                throw new BusinessException(10002, "ERROR: " + command + "\n" + err + "\n" + out);
            }

            if (StringUtils.isNotBlank(err))
                logger.warn("WARN: " + command + "\n" + err);
            return out;
        } catch (IOException | InterruptedException e) {
            // 10002 = 执行系统命令异常
            throw new BusinessException(10002, command, e);
        } finally {
            if (process != null)
                process.destroy();
        }
    }

    private static String toString(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteStreams.copy(is, out);
        return new String(out.toByteArray(), Charsets.UTF_8).trim();
    }

}
