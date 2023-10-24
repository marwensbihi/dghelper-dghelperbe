package com.majesteye.dghelper.models.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * @author Marwen Sbihi
 */

@Getter
public class UserDTO {
    @Size(min = 6, max = 24)
    private String username;

    @Email
    private String email;

    @Size(min = 6, max = 64)
    private String password;
}
