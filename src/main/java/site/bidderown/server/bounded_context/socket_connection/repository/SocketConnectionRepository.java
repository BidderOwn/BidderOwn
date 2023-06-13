package site.bidderown.server.bounded_context.socket_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;

import java.util.List;

public interface SocketConnectionRepository extends JpaRepository<SocketConnection, Long> {
    List<SocketConnection> findByMember(Member member);
}
