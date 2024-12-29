package com.uzair.todolist.repository;

import com.uzair.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("Select t from Task t where t.user.userId= :userId")
    List<Task> findTaskByUserId(@Param("userId") Long userId);
}
