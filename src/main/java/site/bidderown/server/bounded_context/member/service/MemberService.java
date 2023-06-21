package site.bidderown.server.bounded_context.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.repository.MemberRepository;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Member loginAsSocial(String username) {
        Optional<Member> opMember = getOptionalMember(username);
        return opMember.orElseGet(()
                -> memberRepository.save(Member.of(username)));
    }

    @Transactional
    public Member join(String username, String password) {
        Optional<Member> opMember = getOptionalMember(username);
        return opMember.orElseGet(() ->
                memberRepository.save(Member.of(username, passwordEncoder.encode(password))));
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
