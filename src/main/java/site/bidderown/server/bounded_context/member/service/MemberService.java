package site.bidderown.server.bounded_context.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member loginAsSocial(String name) {
        return getOptionalMember(name)
                .orElseGet(() -> memberRepository.save(Member.of(name)));
    }
    @Transactional
    public Member join(String username, String password) {
        //if (memberRepository.findByName(username).isPresent()) // TODO 중복처리 해야함
        Member member = Member.builder()
                .name(username)
                .password(passwordEncoder.encode(password)).build();
        memberRepository.save(member);

        return member;
    }
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId));
    }

    public Member getMember(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
    }

    public void clear() {
        memberRepository.deleteAll();
    }

    private Optional<Member> getOptionalMember(String name) {
        return memberRepository.findByName(name);
    }
}
