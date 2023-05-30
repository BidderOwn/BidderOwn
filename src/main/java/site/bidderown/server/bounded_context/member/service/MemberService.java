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

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member join(String name) {
        Assert.hasText(name, "Member's name must be provided");
        return findOpByName(name)
                .orElseGet(() -> memberRepository.save(Member.of(name)));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId + ""));
    }

    public MemberDetail findMemberDetailById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId + ""));
        return MemberDetail.from(member);
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name));
    }

    public void clear() {
        memberRepository.deleteAll();
    }

    private Optional<Member> findOpByName(String name) {
        return memberRepository.findByName(name);
    }

    @Transactional
    public Member loginAsSocial(String name) {
        return findOpByName(name)
                .orElseGet(() -> join(name));
    }
}
