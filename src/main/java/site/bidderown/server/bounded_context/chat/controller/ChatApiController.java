package site.bidderown.server.bounded_context.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat.service.ChatService;

import java.util.List;

@Tag(name = "chat", description = "채팅 API")
@RequiredArgsConstructor
@RestController
public class ChatApiController {
    private final ChatService chatService;

    @Operation(summary = "채팅 리스트 조회", description = "chatRoomId를 이용하여 모든 chat을 조회합니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/chat/list/{chatRoomId}")
    public List<ChatResponse> chatHistory(@PathVariable Long chatRoomId) {
        return chatService.getChats(chatRoomId);
    }

}
