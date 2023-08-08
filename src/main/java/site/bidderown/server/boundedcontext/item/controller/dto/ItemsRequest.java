package site.bidderown.server.boundedcontext.item.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsRequest {
    private int s = 1;
    private String q = "";
    private boolean filter = false;
    @Parameter(name = "id", description = "마지막상품의 id")
    private Long id;
}
