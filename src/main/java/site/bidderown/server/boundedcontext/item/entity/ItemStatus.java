package site.bidderown.server.boundedcontext.item.entity;

public enum ItemStatus {
    BIDDING("경매중"),
    SOLDOUT("판매완료"),
    BID_END("경매종료");

    private final String status;

    ItemStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
