package site.bidderown.server.bounded_context.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.users.controller.dto.UsersResponse;
import site.bidderown.server.bounded_context.users.entity.Users;
import site.bidderown.server.bounded_context.users.repository.UsersRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public Users join(String username) {
        Assert.hasText(username, "Username must be provided");
        return findOpByUsername(username)
                .orElseGet(() -> usersRepository.save(Users.of(username)));
    }

    public UsersResponse findByUsername(String username) {
        Users user = findOpByUsername(username)
                .orElseThrow(() -> new NotFoundException("Not Found -> " + username));
        return UsersResponse.from(user);
    }

    private Optional<Users> findOpByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Transactional
    public Users loginAsSocial(String username) {
        return findOpByUsername(username)
                .orElseGet(() -> join(username));
    }
}
