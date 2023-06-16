package site.bidderown.server.bounded_context.socket_connection.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionRequest;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionResponse;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;
import site.bidderown.server.bounded_context.socket_connection.repository.SocketConnectionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SocketConnectionService {

    private final SocketConnectionRepository socketConnectionRepository;
    private final MemberService memberService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public List<SocketConnectionResponse> getConnections(String memberName) {
        Member member = memberService.getMember(memberName);

        socketConnectionRepository.findByMemberAndConnectionType(member, ConnectionType.CHAT)
                .orElseGet(() -> create(SocketConnection.of(member, member.getId(), ConnectionType.CHAT)));

        List<SocketConnection> socketConnections = socketConnectionRepository.findByMember(member);

        return socketConnections.stream()
                .map(SocketConnectionResponse::of)
                .collect(Collectors.toList());
    }

    public Long create(String memberName, SocketConnectionRequest request) {
        Member member = memberService.getMember(memberName);
        return socketConnectionRepository.save(
                SocketConnection.of(
                        member,
                        request.getConnectionId(),
                        request.getConnectionType()
                )).getId();
    }

    private SocketConnection create(SocketConnection socketConnection) {
        return socketConnectionRepository.save(socketConnection);
    }
}
