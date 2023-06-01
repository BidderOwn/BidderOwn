package site.bidderown.server.bounded_context.bid.entity;

import lombok.Getter;

@Getter
public enum BidResult {
    SUCCESS, WAIT, FAIL;

}
