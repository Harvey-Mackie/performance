package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ResponseMetadata
 */
@Data
@AllArgsConstructor
public class ResponseMetadata {
  private Long timestamp;
  private String response;
}
