package com.kdimitrov.edentist.server.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public class GraphApiExecutor {

    private final Logger logger = LogManager.getLogger(GraphApiExecutor.class);

    public <T> T execute(Callable<T> callable, Method method) {
        try {
            switch (method) {
                case CREATE:
                    T result = callable.call();
                    logger.info("Creating instance {} via the Graph API.", result.toString());

                    return result;
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum Method {
        CREATE
    }
}
