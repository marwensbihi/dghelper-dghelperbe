package com.majesteye.dghelper.utils;

import com.majesteye.dghelper.models.*;
import com.majesteye.dghelper.models.response.*;
import com.majesteye.dghelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.regex.Pattern;

import static java.time.LocalDateTime.now;

/**
 * @author Marwen Sbihi
 */

public class Utils {
    @Autowired
    UserRepository userRepository;



    public void checkDataLevel(Integer level, Integer maxLevel) {
        if (level < 0 || level > maxLevel) {
            throw new IllegalStateException("Level can only be > 0 and <= " + maxLevel);
        }
    }

    public void processCategoryDataLevel(Category category, Integer level) {
        if (level == 0) {
            category.setDomains(null);
        } else {
            for (Domain domain : category.getDomains()) {
                processDomainDataLevel(domain, level);
            }
        }
    }

    public void processDomainDataLevel(Domain domain, Integer level) {
        if (level == 1) {
            domain.setControls(null);
        } else {
            for (Control control : domain.getControls()) {
                processControlDataLevel(control, level);
            }
        }
    }

    public void processControlDataLevel(Control control, Integer level) {
        control.setDomain(null);
        if (level == 2) {
            control.setSpecifications(null);
        } else {
            for (Specification specification : control.getSpecifications()) {
                processSpecificationDataLevel(specification, level);
            }
        }
    }

    public void processSpecificationDataLevel(Specification specification, Integer level) {
        specification.setControl(null);
        if (level == 3) {

            specification.setInformations(null);


        } else {

            for (Information information : specification.getInformations()) {
                   information.setSpecification(null);
                   for (Paragraph paragraph : information.getParagraphs()) {
                     paragraph.setInformation(null);
                   }
            }

        }
    }



    public ResponseEntity<Response> handleResponse(String message, HttpStatus httpStatus, HashMap<?, ?> data) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder()
                .timestamp(now().toString())
                .data(data)
                .message(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build());
    }

    public ResponseEntity<Response> handleException(Exception e, HttpStatus httpStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder()
                .timestamp(now().toString())
                .exception(e.getMessage())
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build());
    }

    public boolean isUsernameValid(String username) {
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");

        if (regex.matcher(username).find()) {
            throw new IllegalStateException("Username should not contain any special characters");
        }
        if (username.contains(" ")) {
            throw new IllegalStateException("Username should not contain any whitespaces");
        }
        if (username.length() < 6) {
            throw new IllegalStateException("Username should contain between 6 and 24 characters");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already taken");
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        if (email.contains(" ")) {
            throw new IllegalStateException("E-mail should not contain any whitespaces");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("E-mail already taken");
        }
        return true;
    }

    public boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            throw new IllegalStateException("Password should contain minimum 6 characters");
        }
        if (password.contains(" ")) {
            throw new IllegalStateException("Password should not contain any whitespaces");
        }
        return true;
    }
}
