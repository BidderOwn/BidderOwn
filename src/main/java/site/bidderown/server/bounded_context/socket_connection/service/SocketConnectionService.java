package site.bidderown.server.bounded_context.socket_connection.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionRequest;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionResponse;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;
import site.bidderown.server.bounded_context.socket_connection.repository.SocketConnectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SocketConnectionService {

    private final SocketConnectionRepository socketConnectionRepository;
    private final MemberService memberService;

    public List<SocketConnectionResponse> getConnections(String memberName) {
        Member member = memberService.getMember(memberName);
        return socketConnectionRepository.findByMember(member).stream()
                .map(SocketConnectionResponse::of)
                .collect(Collectors.toList());
    }

    public Long create(String memberName, SocketConnectionRequest request) {
        Member member = memberService.getMember(memberName);
        return socketConnectionRepository.save(
                SocketConnection.of(
                        member,
                        request.getConnectionId(),
                        resolveConnectionType(request.getConnectionType())
                )).getId();
    }

    private ConnectionType resolveConnectionType(String connectionType) {
        switch (connectionType) {
            case "CHAT" -> {
                return ConnectionType.CHAT;
            }
            case "COMMENT" -> {
                return ConnectionType.COMMENT;
            }
            default -> {
                return ConnectionType.BID;
            }
        }
    }
}
