package com.uzair.todolist.service;

import com.uzair.todolist.model.Task;
import com.uzair.todolist.request.TaskRequest;

import java.util.List;

public interface TaskService {
    void createTask(TaskRequest request);

    Task findByTaskId(Long taskId);

    List<Task> findTasksByUserId(Long userId);
}
