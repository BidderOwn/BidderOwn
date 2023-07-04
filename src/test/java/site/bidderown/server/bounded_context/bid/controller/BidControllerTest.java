package site.bidderown.server.bounded_context.bid.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.chat_room.controller.ChatRoomController;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BidControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BidService bidService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void clear() {
        bidService.clear();
    }

    //@Test
    @DisplayName("입찰 등록")
    @WithUserDetails(value = "user_0")
    void t001() throws Exception {

        Member seller = memberService.getMember("user_1");
        Item bidItem = itemRepository.save(
                Item.builder()
                        .title("title1")
                        .description("desc1")
                        .minimumPrice(10000)
                        .member(seller)
                        .build()
        );
        String jsonString = new ObjectMapper().writeValueAsString(
                BidRequest.of(
                        bidItem.getId(),
                        10000)
        );

        ResultActions resultActions = mvc
                .perform(post("/bid")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print());
        resultActions
                .andExpect(handler().handlerType(BidController.class))
                .andExpect(handler().methodName("registerBid"))
                .andExpect(status().is2xxSuccessful());

        List<BidResponse> bidList = bidService.getBids(bidItem.getId());
        Assertions.assertThat(bidList.size()).isEqualTo(1); // 상세조회 기능이 따로 없어서 size로 체크했습니다.
    }
    //@Test
    @DisplayName("입찰 목록 조회")
    void t002() throws Exception {
        //given
        Member seller = memberService.getMember("user_1");
        Member buyer = memberService.getMember("user_2");

        Item bidItem = itemRepository.save(
                Item.builder()
                        .title("title1")
                        .description("desc1")
                        .minimumPrice(10000)
                        .member(seller)
                        .build()
        );
        bidService.create(
                BidRequest.builder()
                        .itemId(bidItem.getId())
                        .itemPrice(10000)
                        .build(), buyer.getName()
        );
        //when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/bid/list")
                        .param("itemId", String.valueOf(bidItem.getId())))
                .andDo(print());
        resultActions
                .andExpect(handler().handlerType(BidController.class))
                .andExpect(handler().methodName("bidList"))
                .andExpect(status().is2xxSuccessful());


        //then
        List<BidResponse> bidList = bidService.getBids(bidItem.getId());
        assertThat(bidList.size()).isEqualTo(1);
    }
}