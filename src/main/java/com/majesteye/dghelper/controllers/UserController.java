package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.request.UserDTO;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// DISABLED
/* @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users") */
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Response> getAll() {
        return userService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "{userId}", produces = "application/json")
    public ResponseEntity<Response> getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "{userId}", produces = "application/json")
    public ResponseEntity<Response> update(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return userService.update(userId, userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "{userId}", produces = "application/json")
    public ResponseEntity<Response> deleteById(@PathVariable Long userId) {
        return userService.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "{userId}/roles", produces = "application/json")
    public ResponseEntity<Response> assignRole(@PathVariable Long userId, @RequestParam(name = "role") String role) {
        return userService.assignRole(userId, role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "{userId}/roles", produces = "application/json")
    public ResponseEntity<Response> revokeRole(@PathVariable Long userId, @RequestParam(name = "role") String role) {
        return userService.revokeRole(userId, role);
    }
}
