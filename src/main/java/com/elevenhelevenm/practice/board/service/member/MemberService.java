package com.elevenhelevenm.practice.board.service.member;

import com.elevenhelevenm.practice.board.domain.member.Member;
import com.elevenhelevenm.practice.board.repository.MemberRepository;
import com.elevenhelevenm.practice.board.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public Member loadMemberByEmail(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 이메일입니다."));
    }

    @Transactional
    public Long save(MemberSaveRequestDto requestDto) {
        Member member = requestDto.toEntity();
        member.encodePassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member).getId();
    }

}
