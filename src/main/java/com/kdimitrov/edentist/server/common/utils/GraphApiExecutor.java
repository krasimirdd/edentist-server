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
            T result = callable.call();

            switch (method) {
                case CREATE:
                    logger.info("Creating instance {} via the Graph API.", result.toString());
                case GET:
                    logger.info("Getting data for entity via the Graph API");
                case FILTER:
                    logger.info("Filtering data for entity via the Graph API.");
                default:

            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum Method {
        CREATE,
        GET,
        FILTER,
    }
}
