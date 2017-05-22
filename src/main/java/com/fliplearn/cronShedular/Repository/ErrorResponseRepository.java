package com.fliplearn.cronShedular.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fliplearn.cronShedular.exception.ErrorResponse;

public interface ErrorResponseRepository extends CrudRepository<ErrorResponse, Integer> {

}
