package site.bidderown.server.boundedcontext.chatroom.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatRoomControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ChatRoomService chatRoomService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void clear() {
//        chatRoomService.clear();
//    }
//
//    @Test
//    @DisplayName("채팅방 리스트 페이지로 이동(파라미터가 있을 때)")
//    void t001() throws Exception {
//        ResultActions resultActions = mvc
//                .perform(get("/chat/list")
//                        .param("id", "1"))
//                .andDo(print());
//        resultActions
//                .andExpect(handler().handlerType(ChatRoomController.class))
//                .andExpect(handler().methodName("showChatRoomList"))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    @DisplayName("채팅방 리스트 페이지로 이동(파라미터가 없을 때)")
//    void t002() throws Exception {
//        ResultActions resultActions = mvc
//                .perform(get("/chat/list"))
//                .andDo(print());
//        resultActions
//                .andExpect(handler().handlerType(ChatRoomController.class))
//                .andExpect(handler().methodName("showChatRoomList"))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    @DisplayName("채팅방이 없으면 생성하고 그 방으로 리다이렉팅")
//    void t003() throws Exception {
//        //given
//        Member seller = memberService.getMember("user_0");
//        Member buyer = memberService.getMember("user_1");
//
//        Item givenItem = itemRepository.save(
//                Item.builder()
//                        .title("title1")
//                        .description("desc1")
//                        .minimumPrice(10000)
//                        .member(seller)
//                        .build()
//        );
//
//        String jsonString = new ObjectMapper().writeValueAsString(
//                ChatRoomRequest.of(
//                        seller.getId(),
//                        buyer.getId(),
//                        givenItem.getId())
//        );
//
//        //when
//        ResultActions resultActions = mvc
//                .perform(post("/api/v1/chat-room")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonString))
//                .andDo(print());
//
//        MvcResult mvcResult = resultActions.andReturn();
//        String url = mvcResult.getResponse().getRedirectedUrl();
//
//        //then
//        resultActions
//                .andExpect(handler().handlerType(ChatRoomController.class))
//                .andExpect(handler().methodName("handleChatRoom"))
//                .andExpect(status().is3xxRedirection());
//
//        assertThat(url).contains("?id=" + givenItem.getId());
//    }
//
//    @Test
//    @DisplayName("채팅방 아이템 정보, 채팅 내역 가져오는 테스트")
//    void t004() throws Exception {
//        //given
//        Member seller = memberService.getMember("user_0");
//        Member buyer = memberService.getMember("user_1");
//
//        String title = "title1";
//        String desc = "desc1";
//        int price = 10000;
//
//        Item givenItem = itemRepository.save(
//                Item.builder()
//                        .title(title)
//                        .description(desc)
//                        .minimumPrice(price)
//                        .member(seller)
//                        .build()
//        );
//
//        ChatRoom savedChatRoom = chatRoomService.create(
//                ChatRoomRequest.of(
//                        seller.getId(),
//                        buyer.getId(),
//                        givenItem.getId()
//                )
//        );
//
//        //when
//        ResultActions resultActions = mvc
//                .perform(get("/api/v1/chat/" + savedChatRoom.getId()))
//                .andDo(print());
//
//        MvcResult mvcResult = resultActions.andReturn();
//
//        //then
//        resultActions
//                .andExpect(handler().handlerType(ChatRoomController.class))
//                .andExpect(handler().methodName("joinChat"))
//                .andExpect(status().is2xxSuccessful());
//
//        String jsonResponse = mvcResult.getResponse().getContentAsString();
//        ChatRoomDetail chatRoomDetail = objectMapper
//                .readValue(jsonResponse, ChatRoomDetail.class);
//
//        assertThat(chatRoomDetail.getItemTitle()).isEqualTo(title);
//        assertThat(chatRoomDetail.getPrice()).isEqualTo(price);
//        assertThat(chatRoomDetail.getChatList().size()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("채팅방 리스트 조회")
//    @WithUserDetails(value = "user_0")
//    void t005() throws Exception {
//        //given
//        Member seller = memberService.getMember("user_0");
//        Member buyer = memberService.getMember("user_1");
//
//        Item givenItem = itemRepository.save(
//                Item.builder()
//                        .title("title")
//                        .description("desc")
//                        .minimumPrice(1000)
//                        .member(seller)
//                        .build()
//        );
//
//        chatRoomService.create(new ChatRoomRequest(seller.getId(), buyer.getId(), givenItem.getId()));
//
//        //when
//        ResultActions resultActions = mvc
//                .perform(get("/api/v1/chat/list"))
//                .andDo(print());
//
//        MvcResult mvcResult = resultActions.andReturn();
//
//        String jsonResponse = mvcResult.getResponse().getContentAsString();
//        TypeReference<List<ChatRoomResponse>> type = new TypeReference<>() {};
//        List<ChatRoomResponse> chatRooms = objectMapper.readValue(jsonResponse, type);
//
//        //then
//        assertThat(chatRooms.size()).isEqualTo(1);
//
//    }
}