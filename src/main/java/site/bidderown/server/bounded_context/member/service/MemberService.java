package site.bidderown.server.bounded_context.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.member.controller.dto.MemberResponse;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member join(String name) {
        Assert.hasText(name, "Member's name must be provided");
        return findOpByName(name)
                .orElseGet(() -> memberRepository.save(Member.of(name)));
    }

    public MemberResponse findByName(String name) {
        Member member = findOpByName(name)
                .orElseThrow(() -> new NotFoundException(name));
        return MemberResponse.from(member);
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
