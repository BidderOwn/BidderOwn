package site.bidderown.server.bounded_context.chat_room.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void clear() {
        chatRoomService.clear();
    }

    @Test
    @DisplayName("채팅방 리스트 페이지로 이동(파라미터가 있을 때)")
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/chat/list")
                        .param("id", "1"))
                .andDo(print());
        resultActions
                .andExpect(handler().handlerType(ChatRoomController.class))
                .andExpect(handler().methodName("showChatRoomList"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("채팅방 리스트 페이지로 이동(파라미터가 없을 때)")
    void t002() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/chat/list"))
                .andDo(print());
        resultActions
                .andExpect(handler().handlerType(ChatRoomController.class))
                .andExpect(handler().methodName("showChatRoomList"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("채팅방이 없으면 생성하고 그 방으로 리다이렉팅")
    void t003() throws Exception {
        //given
        Member seller = memberService.findByName("user_0");
        Member buyer = memberService.findByName("user_1");

        Item givenItem = itemService.create(Item
                .builder()
                .title("test1")
                .description("desc1")
                .expireAt(LocalDateTime.now())
                .minimumPrice(1000)
                .build());

        String jsonString = new ObjectMapper().writeValueAsString(
                ChatRoomRequest.from(
                        seller.getId(),
                        buyer.getId(),
                        givenItem.getId())
        );

        //when
        ResultActions resultActions = mvc
                .perform(post("/api/v1/chat-room")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print());

        MvcResult mvcResult = resultActions.andReturn();
        String url = mvcResult.getResponse().getRedirectedUrl();

        //then
        resultActions
                .andExpect(handler().handlerType(ChatRoomController.class))
                .andExpect(handler().methodName("handleChatRoom"))
                .andExpect(status().is3xxRedirection());

        assertThat(url).contains("?id=" + givenItem.getId());
    }

    @Test
    @DisplayName("채팅방 아이템 정보, 채팅 내역 가져오는 테스트")
    void t004() throws Exception {
        //given
        Member seller = memberService.findByName("user_0");
        Member buyer = memberService.findByName("user_1");

        String title = "title1";
        String desc = "desc1";
        LocalDateTime now = LocalDateTime.now();
        int price = 1000;

        Item givenItem = itemService.create(Item
                .builder()
                .title(title)
                .description(desc)
                .expireAt(now)
                .minimumPrice(price)
                .build());

        ChatRoom savedChatRoom = chatRoomService.create(
                ChatRoomRequest.from(
                        seller.getId(),
                        buyer.getId(),
                        givenItem.getId()
                )
        );

        //when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/chat/" + savedChatRoom.getId()))
                .andDo(print());

        MvcResult mvcResult = resultActions.andReturn();

        //then
        resultActions
                .andExpect(handler().handlerType(ChatRoomController.class))
                .andExpect(handler().methodName("joinChat"))
                .andExpect(status().is2xxSuccessful());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ChatRoomDetail chatRoomDetail = objectMapper
                .readValue(jsonResponse, ChatRoomDetail.class);

        assertThat(chatRoomDetail.getItemTitle()).isEqualTo(title);
        assertThat(chatRoomDetail.getPrice()).isEqualTo(price);
        assertThat(chatRoomDetail.getChatList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방 리스트 조회")
    @WithUserDetails(value = "user_0")
    void t005() throws Exception {
        //given
        Member seller = memberService.findByName("user_0");
        Member buyer = memberService.findByName("user_1");

        Item givenItem = itemService.create(Item
                .builder()
                .title("test1")
                .description("desc1")
                .expireAt(LocalDateTime.now())
                .minimumPrice(1000)
                .build());

        chatRoomService.create(new ChatRoomRequest(seller.getId(), buyer.getId(), givenItem.getId()));

        //when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/chat/list"))
                .andDo(print());

        MvcResult mvcResult = resultActions.andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TypeReference<List<ChatRoomResponse>> type = new TypeReference<>() {};
        List<ChatRoomResponse> chatRooms = objectMapper.readValue(jsonResponse, type);

        //then
        assertThat(chatRooms.size()).isEqualTo(1);

    }
}