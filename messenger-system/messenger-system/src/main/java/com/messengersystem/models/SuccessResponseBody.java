package com.messengersystem.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
public class SuccessResponseBody {
  private String status;
  private String message;
}
