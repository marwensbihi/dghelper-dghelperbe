package com.majesteye.dghelper.services;

import com.majesteye.dghelper.models.enums.ERole;
import com.majesteye.dghelper.models.Role;
import com.majesteye.dghelper.models.User;
import com.majesteye.dghelper.models.request.UserDTO;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.RoleRepository;
import com.majesteye.dghelper.repository.UserRepository;
import com.majesteye.dghelper.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marwen Sbihi
 */

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> getAll() {
        try {
            List<User> users = userRepository.findAll();

            users.forEach(user -> user.setPassword(null));

            HashMap<String, List<User>> data = new HashMap<>();
            data.put("users", users);

            return utils.handleResponse("All users retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Response> getById(Long userId) {
        try {
            User user = userRepository.findById(userId).map(u -> {
                u.setPassword(null);
                return u;
            }).orElseThrow(() -> new NoSuchElementException("User with id: '" + userId + "' does not exist."));

            HashMap<String, User> data = new HashMap<>();
            data.put("user", user);

            return utils.handleResponse("User: '" + userId + "' retrieved successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Response> deleteById(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id: '" + userId + "' does not exist"));

            user.getRoles().removeAll(user.getRoles());

            userRepository.deleteById(userId);

            return utils.handleResponse("User: '" + userId + "' deleted successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Response> update(Long userId, UserDTO userDTO) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id: '" + userId + "' does not exist"));

            String username = userDTO.getUsername();
            String email = userDTO.getEmail();
            String password = userDTO.getPassword();

            if (username != null && !username.equals(user.getUsername()) && utils.isUsernameValid(username)) {
                user.setUsername(username);
            }

            if (email != null && !email.equals(user.getEmail()) && utils.isEmailValid(email)) {
                user.setEmail(email);
            }

            if (password != null && !password.isEmpty() && utils.isPasswordValid(password)) {
                user.setPassword(encoder.encode(password));
            }

            return utils.handleResponse("User updated successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Response> assignRole(Long userId, String roleToAdd) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id: '" + userId + "' does not exist"));

            ERole eRole;

            switch (roleToAdd) {
                case "ADMIN":
                    eRole = ERole.ROLE_ADMIN;
                    break;
                case "USER":
                    eRole = ERole.ROLE_USER;
                    break;
                default:
                    throw new NoSuchElementException("Role: '" + roleToAdd.toUpperCase() + "' does not exist");
            }

            Role role = roleRepository.findByName(eRole);

            user.getRoles().add(role);

            return utils.handleResponse("Role: '" + roleToAdd.toUpperCase() + "' assigned successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Response> revokeRole(Long userId, String roleToRevoke) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id: '" + userId + "' does not exist"));

            ERole eRole;

            switch (roleToRevoke) {
                case "ADMIN":
                    eRole = ERole.ROLE_ADMIN;
                    break;
                case "USER":
                    eRole = ERole.ROLE_USER;
                    break;
                default:
                    throw new NoSuchElementException("Role: '" + roleToRevoke.toUpperCase() + "' does not exist");
            }

            Role role = roleRepository.findByName(eRole);

            user.getRoles().remove(role);

            return utils.handleResponse("Role: '" + roleToRevoke.toUpperCase() + "' revoked successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
