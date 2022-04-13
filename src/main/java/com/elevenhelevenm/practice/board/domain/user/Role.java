package com.elevenhelevenm.practice.board.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Getter
public enum Role {
    
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    ;
    
    private final String key;
    private final String title;
}
