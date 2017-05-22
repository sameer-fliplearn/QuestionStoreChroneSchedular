package com.fliplearn.cronShedular.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fliplearn.cronShedular.Repository.ErrorResponseRepository;
import com.fliplearn.cronShedular.exception.ErrorResponse;






@Service
public class ErrorResponseServiceImpl implements ErrorResponseService {
	
	
private ErrorResponseRepository errorResponseRepository;

	
	@Autowired
	public void setErrorResponseRepository(ErrorResponseRepository errorResponseRepository) {
		this.errorResponseRepository = errorResponseRepository;
	}

	@Override
	public ErrorResponse getErrorResponseByErrorId(Integer errorId) {
		return  errorResponseRepository.findOne(errorId);
		
	}
	

}
