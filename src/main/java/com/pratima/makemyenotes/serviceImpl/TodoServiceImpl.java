package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.ToDo;
import com.pratima.makemyenotes.exceptions.NotFoundException;
import com.pratima.makemyenotes.repository.TodoRepository;
import com.pratima.makemyenotes.service.TodoService;
import com.pratima.makemyenotes.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserUtility utility;

    @Override
    public Response createTodo(ToDo todo) {
        Response response = null;
        todo.setUser(utility.getLoggedInUser());
        try {
          ToDo todoDB = todoRepository.save(todo);
        }catch (Exception e){
            return Response
                    .builder()
                    .status(500)
                    .message("Todo creation failed.").build();
        }
        return Response
                .builder()
                .status(500)
                .message("Todo created.").build();
    }

    @Override
    public Response updateTodo(ToDo todo) {
        return null;
    }

    @Override
    public Response deleteTodo(Long id) {
        ToDo toDo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback Not Found."));
        try{
            todoRepository.deleteById(id);
        } catch (Exception e) {
            return Response.builder()
                    .status(500)
                    .message("Exception in feedback deletion.").build();
        }

        return Response.builder()
                .status(200)
                .message("Feedback deleted Successfully.").build();
    }


    @Override
    public List<ToDo> getAllTodoByUser() {
        List<ToDo> toDoList = todoRepository.findByUserId(utility.getLoggedInUser().getId());

        if(toDoList!=null && toDoList.size()>0){
            return toDoList;
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteTodoByUserid() {
        todoRepository.deleteByUserId(utility.getLoggedInUser().getId());
    }

    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }


}
