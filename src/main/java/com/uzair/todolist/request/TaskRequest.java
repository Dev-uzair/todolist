package com.uzair.todolist.request;

import com.uzair.todolist.util.TaskStatus;
import lombok.Getter;

@Getter
public class TaskRequest {
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;
    private Long userId;
}
