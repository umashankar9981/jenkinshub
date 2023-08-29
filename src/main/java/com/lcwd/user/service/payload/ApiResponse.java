package com.lcwd.user.service.payload;

import org.springframework.http.HttpStatus;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
   private String message;
   private boolean success;
   private HttpStatus status;

}
