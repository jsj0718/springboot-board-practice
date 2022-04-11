package com.elevenhelevenm.practice.board.web;

import com.elevenhelevenm.practice.board.service.member.MemberService;
import com.elevenhelevenm.practice.board.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long join(@RequestBody MemberSaveRequestDto requestDto) {
        return memberService.save(requestDto);
    }
}
