package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.dto.TodoDTO;
import com.pratima.makemyenotes.entity.ToDo;
import com.pratima.makemyenotes.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {


    @Autowired
    private TodoService todoService;

    @PostMapping("/todo")
    public ResponseEntity<Response> saveTodo(@RequestBody ToDo todo){
        return ResponseEntity.ok(todoService.createTodo(todo));
    }

    @PostMapping("/todo-list")
    public ResponseEntity<Response> saveTodoList(@RequestBody List<TodoDTO> todoList){
        List<ToDo> finalTodoListSave = new ArrayList<>();

        Map<Long, ToDo> toDoMap = todoList.stream()
                .filter(dto -> dto.getId() != null)   // ignore new todos
                .collect(Collectors.toMap(
                        TodoDTO::getId,
                        dto -> {
                            ToDo todo = new ToDo();
                            todo.setId(dto.getId());
                            todo.setTask(dto.getTask());
                            todo.setCompleted(dto.getCompleted());
                            return todo;
                        }
                ));

        List<ToDo> finalTodoList = todoList.stream()
                .filter(dto -> dto.getId() == null)   // only NEW todos
                .map(dto -> {
                    ToDo todo = new ToDo();
                    todo.setTask(dto.getTask());
                    todo.setCompleted(dto.getCompleted());
                    return todo;
                })
                .collect(Collectors.toList());

        List<ToDo> toDoToDelete = new ArrayList<>();
        List<ToDo> originalUserTodoList = todoService.getAllTodoByUser();
        for(ToDo todo : originalUserTodoList){
            ToDo todoApi = toDoMap.get(todo.getId());
            if(todoApi!=null){
                todoApi.setCreatedBy(todo.getCreatedBy());
                todoApi.setCreatedDate(todo.getCreatedDate());
                finalTodoList.add(todoApi);
            }else {
                toDoToDelete.add(todo);
            }
        }

        for (ToDo todo : toDoToDelete){
            todoService.deleteById(todo.getId());
        }

       // todoService.deleteTodoByUserid();

        for(ToDo toDo:finalTodoList){
            ResponseEntity.ok(todoService.createTodo(toDo));
        }
        return ResponseEntity.ok(Response.builder().status(200).message("Activities has been added.").build());
    }

    @GetMapping("/todo")
    public ResponseEntity<Response> getTodo(){
        return ResponseEntity.ok(Response.builder().status(200).toDoList(todoService.getAllTodoByUser()).message("Todo list retrieved succesfully.").build());
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Response> deleteTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.deleteTodo(id));
    }

}
