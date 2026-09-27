package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.ToDo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    Response createTodo(ToDo todo);

    Response updateTodo(ToDo todo);

    Response deleteTodo(Long id);

    List<ToDo> getAllTodoByUser();


    void deleteTodoByUserid();

    void deleteById(Long id);
}
