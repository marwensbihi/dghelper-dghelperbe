package com.majesteye.dghelper.services;


import com.majesteye.dghelper.models.enums.ERole;
import com.majesteye.dghelper.models.Role;
import com.majesteye.dghelper.models.User;
import com.majesteye.dghelper.models.request.LoginRequest;
import com.majesteye.dghelper.models.request.SignupRequest;
import com.majesteye.dghelper.models.response.JwtResponse;
import com.majesteye.dghelper.models.response.Response;
import com.majesteye.dghelper.repository.RoleRepository;
import com.majesteye.dghelper.repository.UserRepository;
import com.majesteye.dghelper.security.jwt.JwtUtils;
import com.majesteye.dghelper.security.services.UserDetailsImpl;
import com.majesteye.dghelper.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Marwen Sbihi
 */

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    Utils utils;

    public ResponseEntity<Response> register(SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                throw new IllegalStateException("Username already taken");
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                throw new IllegalStateException("E-mail already taken");
            }

            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName(ERole.ROLE_USER);
            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);

            return utils.handleResponse("User registered successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            HashMap<String, JwtResponse> data = new HashMap<>();
            data.put("user", new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));

            return utils.handleResponse("Logged in successfully", HttpStatus.OK, data);
        } catch (Exception e) {
            return utils.handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
