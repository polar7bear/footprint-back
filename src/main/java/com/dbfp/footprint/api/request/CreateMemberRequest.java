package com.dbfp.footprint.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberRequest {

    @NotNull
    @Size(max = 30)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Size(max = 30)
    private String nickname;
}
