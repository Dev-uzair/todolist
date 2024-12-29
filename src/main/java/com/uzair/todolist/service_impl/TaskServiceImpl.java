package com.uzair.todolist.service_impl;

import com.uzair.todolist.model.Task;
import com.uzair.todolist.repository.TaskRepository;
import com.uzair.todolist.request.TaskRequest;
import com.uzair.todolist.service.TaskService;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;

    }

    @Override
    public void createTask(TaskRequest request) {
        Task task = new Task ( );
        task.setTaskName ( request.getTaskName ( ) );
        task.setTaskDescription ( request.getTaskDescription ( ) );
        task.setTaskStatus ( request.getTaskStatus ( ) );
        task.setUser ( userService.findUserByUserId ( request.getUserId ( ) ) );
        repository.save ( task );
    }

    @Override
    public Task findByTaskId(Long taskId) {
        return repository.findById ( taskId ).orElseThrow ( () -> new RuntimeException ( "Id not found" ) );
    }

    @Override
    public List<Task> findTasksByUserId(Long userId) {
        return repository.findTaskByUserId ( userId );

    }
}
