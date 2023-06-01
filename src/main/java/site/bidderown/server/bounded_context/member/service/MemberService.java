package site.bidderown.server.bounded_context.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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

    private final MemberRepository memberRepository;

    @Transactional
    public Member loginAsSocial(String name) {
        return getOptionalMember(name)
                .orElseGet(() -> memberRepository.save(Member.of(name)));
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId + ""));
    }

    public Member getMember(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
    }

    public MemberDetail getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId + ""));
        return MemberDetail.of(member);
    }

    public void clear() {
        memberRepository.deleteAll();
    }

    private Optional<Member> getOptionalMember(String name) {
        return memberRepository.findByName(name);
    }
}
