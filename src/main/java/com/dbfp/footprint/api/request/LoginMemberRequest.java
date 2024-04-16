package com.dbfp.footprint.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginMemberRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
