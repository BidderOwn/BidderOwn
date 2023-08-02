package site.bidderown.server.bounded_context.heart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartResponse;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartStatus;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.heart.repository.HeartRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartService {
    private final HeartRepository heartRepository;
    private final ItemService itemService;
    private final MemberService memberService;

    public HeartResponse handleHeart(Long itemId, String username) {
        Item item = itemService.getItem(itemId);
        Member member = memberService.getMember(username);

        Optional<Heart> opHeart = heartRepository.findByItemIdAndMemberId(itemId, member.getId());

        if (opHeart.isPresent()) {
            Heart heart = opHeart.get();
            heartRepository.delete(heart);
            return HeartResponse.of(heart, false);
        }

        Heart heart = Heart.of(member, item);
        heartRepository.save(heart);
        return HeartResponse.of(heart, true);
    }

    public HeartStatus getLikeStatus(Long itemId, String username) {
        Member member = memberService.getMember(username);
        Optional<Heart> heart = heartRepository.findByItemIdAndMemberId(itemId, member.getId());

        if (heart.isPresent()) {
            return HeartStatus.of(true);
        }

        return HeartStatus.of(false);
    }

    public List<Long> getLikeIdList(String username) {
        Member member = memberService.getMember(username);
        return heartRepository.findByMemberId(member.getId())
                .stream()
                .map(heart -> heart.getItem().getId())
                .toList();
    }
}
