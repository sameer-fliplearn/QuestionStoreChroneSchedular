package com.fliplearn.cronShedular.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*@ResponseStatus(value=HttpStatus.co, reason="No such Keyword")*/
public class RetryFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public RetryFailureException(String count){
		super(count+" retry over");
	}
}
