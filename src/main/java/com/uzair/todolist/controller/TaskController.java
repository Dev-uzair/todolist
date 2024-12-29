package com.uzair.todolist.controller;

import com.uzair.todolist.model.Task;
import com.uzair.todolist.request.TaskRequest;
import com.uzair.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;


    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<String> createTask(@RequestBody TaskRequest request) {
        try {
            service.createTask ( request );
            return new ResponseEntity<> ( "created successfully", HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( "request failed", HttpStatus.BAD_REQUEST );
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> findTaskByTaskId(@PathVariable Long taskId) {
        try {
            return new ResponseEntity<> ( service.findByTaskId ( taskId ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( new Task ( ), HttpStatus.BAD_REQUEST );

        }
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Task>> findTasksByUserId(@PathVariable Long userId) {
        try {
            return new ResponseEntity<> ( service.findTasksByUserId ( userId ), HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( new ArrayList<> ( ), HttpStatus.BAD_REQUEST );

        }
    }

}
