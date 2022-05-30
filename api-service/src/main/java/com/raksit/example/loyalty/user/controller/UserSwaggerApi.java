package com.raksit.example.loyalty.user.controller;

import com.raksit.example.loyalty.error.ErrorResponse;
import com.raksit.example.loyalty.response.Response;
import com.raksit.example.loyalty.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface UserSwaggerApi {

  @Operation(summary = "find User by ID")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Response is returned successfully"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid ID",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<Response<UserDTO>> findUserById(@Parameter(description = "user ID") String userId);
}
