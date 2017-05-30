package com.fliplearn.cronShedular.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fliplearn.cronShedular.model.FileInformationS3;

public interface FileRepository extends CrudRepository<FileInformationS3,Integer>  {


public FileInformationS3 findTop1ByCronStatusAndAssignedCron(@Param("cronStatus")Boolean cronStatus,@Param("assignedCron")Integer assignedCron);

@Transactional
@Modifying
@Query("update FileInformationS3 fe set fe.cronStatus=:cronStatus WHERE fe.id =:id")
int updateCronStatus(@Param("cronStatus")Boolean cronStatus,@Param("id")Integer id);
}
