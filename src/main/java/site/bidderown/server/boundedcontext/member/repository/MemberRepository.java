package site.bidderown.server.boundedcontext.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
}
