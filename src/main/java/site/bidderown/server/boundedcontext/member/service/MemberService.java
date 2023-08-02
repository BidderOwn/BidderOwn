package site.bidderown.server.boundedcontext.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member loginAsSocial(String username) {
        Optional<Member> opMember = getOptionalMember(username);
        return opMember.orElseGet(()
                -> memberRepository.save(Member.from(username)));
    }

    @Transactional
    public Member join(String username, String password) {
        Optional<Member> opMember = getOptionalMember(username);
        return opMember.orElseGet(() ->
                memberRepository.save(
                        Member.of(username, passwordEncoder.encode(password))
                )
        );
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다.", memberId + ""));
    }

    public Member getMember(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다.", name));
    }

    public void clear() {
        memberRepository.deleteAll();
    }

    private Optional<Member> getOptionalMember(String name) {
        return memberRepository.findByName(name);
    }
}
