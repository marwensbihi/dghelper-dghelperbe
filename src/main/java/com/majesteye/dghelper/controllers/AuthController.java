package com.majesteye.dghelper.controllers;

import com.majesteye.dghelper.models.request.LoginRequest;
import com.majesteye.dghelper.models.request.SignupRequest;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping(value = "login", produces = "application/json")
    public ResponseEntity<Response> login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(value = "register", produces = "application/json")
    public ResponseEntity<Response> register(@RequestBody @Valid SignupRequest signUpRequest) {
        return authService.register(signUpRequest);
    }
}
