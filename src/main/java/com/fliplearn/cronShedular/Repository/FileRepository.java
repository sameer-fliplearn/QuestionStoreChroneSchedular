package com.fliplearn.cronShedular.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fliplearn.cronShedular.model.FileInformationS3;

public interface FileRepository extends CrudRepository<FileInformationS3,Integer>  {


public FileInformationS3 findTop1ByFileStatusAndAssignedCron(@Param("fileStatus")Boolean fileStatus,@Param("assignedCron")Integer assignedCron);

@Transactional
@Modifying
@Query("update FileInformationS3 fe set fe.fileStatus=:fileStatus WHERE fe.id =:id")
int updateFileStatus(@Param("fileStatus")Boolean fileStatus,@Param("id")Integer id);
}
