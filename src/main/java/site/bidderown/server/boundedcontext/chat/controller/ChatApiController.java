package site.bidderown.server.boundedcontext.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.boundedcontext.chat.controller.dto.ChatResponse;
import site.bidderown.server.boundedcontext.chat.service.ChatService;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "채팅 chat-api-controller", description = "채팅 관련 api 입니다.")
@RestController
public class ChatApiController {
    private final ChatService chatService;

    @Operation(summary = "채팅 내역", description = "채팅방 id를 이용하여 채팅 내역을 가져옵니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/chat/list/{chatRoomId}")
    public List<ChatResponse> chatHistory(@PathVariable Long chatRoomId) {
        return chatService.getChats(chatRoomId);
    }

}
