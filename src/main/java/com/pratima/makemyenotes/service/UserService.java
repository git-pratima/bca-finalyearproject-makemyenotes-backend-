package com.pratima.makemyenotes.service;
import com.pratima.makemyenotes.dto.LoginRequest;
import com.pratima.makemyenotes.dto.RegisterRequest;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.dto.UserDTO;
import com.pratima.makemyenotes.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getCurrentLoggedInUser();
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
    UserDetails findOrCreateSocialUser(String email, String name, String provider);
}
