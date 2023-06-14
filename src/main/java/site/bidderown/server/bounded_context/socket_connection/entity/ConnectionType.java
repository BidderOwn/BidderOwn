package site.bidderown.server.bounded_context.socket_connection.entity;

public enum ConnectionType {
    // 상품과 관련된 알림(댓글, 입찰) 시 판매자가 받는 알림 소켓 {itemId}
    ITEM_SELLER("/sub/item/seller/notification/"),

    // 새로운 입찰 등록 시 구매자가 받는 알림 소켓 {itemId}
    ITEM_BIDDER("/sub/item/bidder/notification/"),

    // 채팅 소켓은 내 아이디를 구독
    CHAT("/sub/chat/user/");

    private final String socketPath;

    public String getSocketPath() {
        return socketPath;
    }

    ConnectionType(String socketPath) {
        this.socketPath = socketPath;
    }
}