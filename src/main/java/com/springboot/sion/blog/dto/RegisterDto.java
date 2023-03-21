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
        description = "RegisterDto Model Information"
)
public class RegisterDto {

    @Schema(
            description = "Blog Registration Name"
    )
    private String name;

    @Schema(
            description = "Blog Registration Username"
    )
    private String username;

    @Schema(
            description = "Blog Registration Email"
    )
    private String email;

    @Schema(
            description = "Blog Registration Password"
    )
    private String password;
}
