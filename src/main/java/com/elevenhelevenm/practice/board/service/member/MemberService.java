package com.elevenhelevenm.practice.board.service.member;

import com.elevenhelevenm.practice.board.domain.member.Member;
import com.elevenhelevenm.practice.board.repository.MemberRepository;
import com.elevenhelevenm.practice.board.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member loadMemberByEmail(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 이메일입니다."));
    }

    @Transactional
    public Long save(MemberSaveRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }

}
