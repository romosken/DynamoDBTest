package br.com.test.dynamo.dynamodbtest.controller;

import br.com.test.dynamo.dynamodbtest.model.Task;
import br.com.test.dynamo.dynamodbtest.repository.TaskRepository;
import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TaskController {
    private final TaskRepository repository;

    @GetMapping
    public ResponseEntity<Iterable<Task>> getAllTasks(){
        return ResponseEntity.ok(repository.findAll());
    }
}
