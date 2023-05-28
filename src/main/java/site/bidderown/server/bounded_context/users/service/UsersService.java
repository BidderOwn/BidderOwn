package site.bidderown.server.bounded_context.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Optional<Users> opUser = findByUsername(username);
        return opUser.orElseGet(() -> usersRepository.save(Users.of(username)));
    }

    private Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Transactional
    public Users loginAsSocial(String username) {
        Optional<Users> opUser = findByUsername(username);
        return opUser.orElseGet(() -> join(username));
    }
}
