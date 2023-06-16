package site.bidderown.server.bounded_context.socket_connection.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SocketConnection extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Long connectionId;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;

    @Builder
    public SocketConnection(Member member, Long connectionId, ConnectionType connectionType) {
        this.member = member;
        this.connectionId = connectionId;
        this.connectionType = connectionType;
    }

    public static SocketConnection of(Member member, Long connectionId, ConnectionType connectionType) {

        return SocketConnection.builder()
                .member(member)
                .connectionId(connectionId)
                .connectionType(connectionType)
                .build();
    }
}
