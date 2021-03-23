package com.kdimitrov.edentist.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Operation failed.")
public class OperationUnsuccessful extends RuntimeException {
}
