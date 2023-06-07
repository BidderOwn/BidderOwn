package site.bidderown.server.bounded_context.notification.entity;

public enum NotificationType {
    BID("입찰"),
    SOLDOUT("판매완료"),
    BID_END("경매종료"),
    COMMENT("댓글");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
