package com.springboot.sion.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "LoginDto Model Information"
)
public class LoginDto {

    @Schema(
            description = "Blog Login Username or Email Credential"
    )
    private String usernameOrEmail;

    @Schema(
            description = "Blog Login Password"
    )
    private String password;

}
