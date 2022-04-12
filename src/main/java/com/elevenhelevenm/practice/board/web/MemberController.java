package com.elevenhelevenm.practice.board.web;

import com.elevenhelevenm.practice.board.config.auth.PrincipalDetails;
import com.elevenhelevenm.practice.board.domain.member.Member;
import com.elevenhelevenm.practice.board.repository.MemberRepository;
import com.elevenhelevenm.practice.board.service.member.MemberService;
import com.elevenhelevenm.practice.board.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/user")
    public @ResponseBody String user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("principal : {}", principalDetails.getMember().getId());
        log.info("principal : {}", principalDetails.getMember().getEmail());
        log.info("principal : {}", principalDetails.getMember().getPassword());

        return "user";
    }

    @GetMapping("/manager/reports")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/admin/users")
    public List<Member> admin() {
        return memberRepository.findAll();
    }

    @PostMapping("/join")
    public String join(@RequestBody MemberSaveRequestDto requestDto) {
        memberService.save(requestDto);
        return "회원가입 완료";
    }

/*
    //SecurityConfig 파일 생성 이후 리다이렉트 작동 멈춤
    @GetMapping("/login")
    public @ResponseBody String login() {
        return "login";
    }

    @PostMapping("/join")
    public Long join(@RequestBody MemberSaveRequestDto requestDto) {
        return memberService.save(requestDto);
    }

    @GetMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원가입 완료";
    }

*/



}
