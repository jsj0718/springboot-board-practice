package com.elevenhelevenm.practice.board.web;

import com.elevenhelevenm.practice.board.service.member.MemberService;
import com.elevenhelevenm.practice.board.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    //SecurityConfig 파일 생성 이후 리다이렉트 작동 멈춤
    @GetMapping("/login")
    public @ResponseBody String login() {
        return "login";
    }

/*
    @PostMapping("/join")
    public Long join(@RequestBody MemberSaveRequestDto requestDto) {
        return memberService.save(requestDto);
    }
*/

    @GetMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원가입 완료";
    }


}
