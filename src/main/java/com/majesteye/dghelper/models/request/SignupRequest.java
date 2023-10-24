package com.majesteye.dghelper.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {

  @NotEmpty(message = "Username cannot be NULL/empty")
  @Size(min = 3, max = 20)
  private String username;

  @NotEmpty(message = "E-mail cannot be NULL/empty")
  @Size(max = 50)
  @Email
  private String email;

  @NotEmpty(message = "Password cannot be NULL/empty")
  @Size(min = 6, max = 40)
  private String password;
}
