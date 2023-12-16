package com.messengersystem.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CreateLoginUserRequestBody {
  private String username;
  private String password;
}
