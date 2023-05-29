package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.bounded_context.users.service.UsersService;

import java.util.stream.IntStream;

@Profile({"dev"})
@Configuration
public class NotProd {
    @Bean
    CommandLineRunner initData(
            UsersService usersService
    ) {
        return args -> {
            IntStream.range(0, 10)
                    .forEach(i -> usersService.join(i + ""));
        };
    }
}
