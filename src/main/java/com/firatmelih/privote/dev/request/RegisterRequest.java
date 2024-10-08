package com.firatmelih.privote.dev.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username; // todo add validations
    String password;
    String email;
    String firstName;
    String lastName;
}
