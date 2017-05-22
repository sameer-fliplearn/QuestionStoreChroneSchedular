package com.fliplearn.cronShedular.Service;

import com.fliplearn.cronShedular.exception.ErrorResponse;

public interface ErrorResponseService {
	
	ErrorResponse getErrorResponseByErrorId(Integer errorId);

}
