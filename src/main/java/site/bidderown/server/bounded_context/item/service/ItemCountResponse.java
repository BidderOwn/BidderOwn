package site.bidderown.server.bounded_context.item.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCountResponse {
    private int bidCount;
    private int commentCount;
    private int heartCount;
}
