package com.elevenhelevenm.practice.board.web.dto;

import com.elevenhelevenm.practice.board.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSaveRequestDto {

    private String email;

    private String password;

    private String name;

    @Builder
    public MemberSaveRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
