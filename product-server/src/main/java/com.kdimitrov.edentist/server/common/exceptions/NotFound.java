package com.kdimitrov.edentist.server.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found.")
public class NotFound extends Exception {
    private final String msg;

    public NotFound(String msg) {
        this.msg = msg;
    }
}