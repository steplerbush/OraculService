package org.oracul.service.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Prediction not found") 
public class PredictionNotFoundException extends RuntimeException {

}
