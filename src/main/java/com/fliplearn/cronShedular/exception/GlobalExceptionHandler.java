package com.fliplearn.cronShedular.exception;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fliplearn.cronShedular.DTO.ResponseDTO;
import com.fliplearn.cronShedular.Service.ErrorResponseService;




@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
/*	@ExceptionHandler(value=Exception.class)
public ResponseEntity<ResponseDTO> handleException(Exception e){
	ResponseDTO responseObject =new ResponseDTO();
	responseObject.setError(e.getMessage());
	responseObject.setError(e.toString()+"Cause :"+ e.getCause());
	e.printStackTrace();
	System.out.println("inside exception handler");
	responseObject.setTimesStamp(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()).toString());
	return ResponseEntity.accepted().body(responseObject);
}*/
	
	@Autowired
	private ErrorResponseService  errorResponseService;

    @Autowired
    public void  setErrorResponseService(ErrorResponseService  errorResponseService) {
        this.errorResponseService = errorResponseService;
    }
	
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class,ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		ResponseDTO responseDto=new ResponseDTO();
		int errorCode=0;
		if(StringUtils.isNumeric(ex.getMessage())){
			errorCode=Integer.parseInt(ex.getMessage());
		}else{
			errorCode=7;
		}
		
		ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
        responseDto.setError(errorResponse);
        System.out.println("Exception"+ex);
        
        return handleExceptionInternal(ex, responseDto, 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
	
	
	@ExceptionHandler(value = { SQLException.class,DataAccessException.class,TypeMismatchDataAccessException.class })
    protected ResponseEntity<Object> handleSQLException(RuntimeException ex, WebRequest request) {
		ResponseDTO responseDto=new ResponseDTO();
		int errorCode=0;
		if(StringUtils.isNumeric(ex.getMessage())){
			errorCode=Integer.parseInt(ex.getMessage());
		}else{
			errorCode=14;
		}
		
		ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
        responseDto.setError(errorResponse);
                
        responseDto.setError(errorResponse);
        System.out.println("Exception"+ex);
        
        return handleExceptionInternal(ex, responseDto, 
          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
	
	@ExceptionHandler(value = {DataIntegrityViolationException.class,DuplicateKeyException.class})
	 @ResponseStatus(value=HttpStatus.CONFLICT)  
    protected ResponseEntity<Object> handleConflictException(RuntimeException ex, WebRequest request) {
		ResponseDTO responseDto=new ResponseDTO();
		int errorCode=0;
		if(StringUtils.isNumeric(ex.getMessage())){
			errorCode=Integer.parseInt(ex.getMessage());
		}else{
			errorCode=10;
		}
		
		ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
        responseDto.setError(errorResponse);
        
        
        responseDto.setError(errorResponse);
        System.out.println("Exception"+ex);
        
        return handleExceptionInternal(ex, responseDto, 
          new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
	
	@ExceptionHandler(value = {InvalidInvocationException.class,InvocationTargetException.class})
	 @ResponseStatus(value=HttpStatus.CONFLICT)  
   protected ResponseEntity<Object> handleInvocationException(RuntimeException ex, WebRequest request) {
		ResponseDTO responseDto=new ResponseDTO();
		int errorCode=0;
		if(StringUtils.isNumeric(ex.getMessage())){
			errorCode=Integer.parseInt(ex.getMessage());
		}else{
			errorCode=10;
		}
		
		ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
       responseDto.setError(errorResponse);
       
       
       responseDto.setError(errorResponse);
       System.out.println("Exception"+ex);
       
       return handleExceptionInternal(ex, responseDto, 
         new HttpHeaders(), HttpStatus.CONFLICT, request);
   }
	
	 @Override
	    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		 ResponseDTO responseDto=new ResponseDTO();
			int errorCode=0;
			if(StringUtils.isNumeric(ex.getMessage())){
				errorCode=Integer.parseInt(ex.getMessage());
			}else{
				errorCode=2;
			}
			
			ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
	        responseDto.setError(errorResponse);
	        
	       
	       responseDto.setError(errorResponse);
	       System.out.println("Exception"+ex);
	        return new ResponseEntity<Object>(responseDto,HttpStatus.NOT_FOUND);
	    }
	
	 
	 	@Override
	    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	 		 ResponseDTO responseDto=new ResponseDTO();
				int errorCode=0;
				if(StringUtils.isNumeric(ex.getMessage())){
					errorCode=Integer.parseInt(ex.getMessage());
				}else{
					errorCode=9;
				}
				
				ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
		        responseDto.setError(errorResponse);
		        
		       
		       responseDto.setError(errorResponse);
		       System.out.println("Exception"+ex);
		        return new ResponseEntity<Object>(responseDto,HttpStatus.BAD_REQUEST);
	    }
	
	 	 @ExceptionHandler(RetryFailureException.class)
	 	protected ResponseEntity<Object> handleS3Exception(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	 		 ResponseDTO responseDto=new ResponseDTO();
				int errorCode=0;
				if(StringUtils.isNumeric(ex.getMessage())){
					errorCode=Integer.parseInt(ex.getMessage());
				}else{
					errorCode=9;
				}
				
				ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
		        responseDto.setError(errorResponse);
		        
		       
		       responseDto.setError(errorResponse);
		       System.out.println("Exception"+ex);
		        return new ResponseEntity<Object>(responseDto,HttpStatus.BAD_REQUEST);
	    }
	
	 	 @ExceptionHandler(PropertyLoadException.class)
		 	protected ResponseEntity<Object> handlePropertyRelatedException(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		 		 ResponseDTO responseDto=new ResponseDTO();
					int errorCode=0;
					if(StringUtils.isNumeric(ex.getMessage())){
						errorCode=Integer.parseInt(ex.getMessage());
					}else{
						errorCode=9;
					}
					
					ErrorResponse errorResponse = errorResponseService.getErrorResponseByErrorId(errorCode);
			        responseDto.setError(errorResponse);
			        
			       
			       responseDto.setError(errorResponse);
			       System.out.println("Exception"+ex);
			        return new ResponseEntity<Object>(responseDto,HttpStatus.BAD_REQUEST);
		    }
	   
}
