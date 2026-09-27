package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.dto.LoginRequest;
import com.pratima.makemyenotes.dto.RegisterRequest;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.dto.UserDTO;
import com.pratima.makemyenotes.entity.User;
import com.pratima.makemyenotes.enums.AuthProvider;
import com.pratima.makemyenotes.enums.UserRole;
import com.pratima.makemyenotes.exceptions.InvalidCredentialsException;
import com.pratima.makemyenotes.exceptions.NotFoundException;
import com.pratima.makemyenotes.repository.UserRepository;
import com.pratima.makemyenotes.security.JwtUtils;
import com.pratima.makemyenotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {

        UserRole role = UserRole.MANAGER;

        if (registerRequest.getRole() != null) {
            role=registerRequest.getRole();
        }

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("user created successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
       User user = userRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow(()-> new NotFoundException("Email not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("password does not match");
        }
        String token = jwtUtils.generateToken(user.getEmail());


        return Response.builder()
                .status(200)
                .message("user logged in successfully")
                .role(user.getRole())
                .userName(user.getName())
                .token(token)
                .expirationTime("Next 24 Hours")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        return user;
    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if (userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPhoneNumber(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User Successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {

         userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User Not Found"));
         userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User Successfully Deleted")
                .build();
    }

    @Override
    public UserDetails findOrCreateSocialUser(String email, String name, String provider) {

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            log.info("Existing social user found: {}", email);
            return existingUser.get();
        }

        log.info("Creating new social user: email={}, provider={}", email, provider);

        // First time social login → create new user
        User newUser = User.builder()
                .email(email)
                .name(name)
                .password(null)                              // ✅ no password for social users
                .phoneNumber(null)                           // ✅ no phone for social users
                .role(UserRole.MANAGER)                      // ✅ use UserRole enum (default role)
                .provider(AuthProvider.valueOf(provider.toUpperCase()))  // ✅ "google" → AuthProvider.GOOGLE
                .emailVerified(true)                         // ✅ Google emails are pre-verified
                .build();

        return userRepository.save(newUser);
    }



}
