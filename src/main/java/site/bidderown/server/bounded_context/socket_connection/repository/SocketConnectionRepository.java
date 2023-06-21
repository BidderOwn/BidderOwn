package site.bidderown.server.bounded_context.socket_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;

import java.util.List;
import java.util.Optional;

public interface SocketConnectionRepository extends JpaRepository<SocketConnection, Long> {
    List<SocketConnection> findByMember(Member member);

    Optional<SocketConnection> findByMemberAndConnectionType(Member member, ConnectionType connectionType);

    List<SocketConnection> findAllByConnectionIdAndConnectionType(Long connectionId, ConnectionType connectionType);
}
