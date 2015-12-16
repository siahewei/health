package com.healthcare.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/12
 */
public class TaskExcuteUtil {

    private ExecutorService executorService;
    public static TaskExcuteUtil instance;

    public static TaskExcuteUtil getInstance() {

        if (instance == null) {
            synchronized (TaskExcuteUtil.class) {
                if (instance == null) {
                    instance = new TaskExcuteUtil();
                }
            }
        }

        return instance;
    }

    private TaskExcuteUtil() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void excute(Runnable runnable) {
        executorService.execute(runnable);
    }

}
