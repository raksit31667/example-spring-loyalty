package com.raksit.example.loyalty.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {

  private T data;

  public Response(T data) {
    this.data = data;
  }
}
