package site.bidderown.server.base.data;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"dev"})
@RequiredArgsConstructor
@Configuration
public class NotProd {

    @Bean
    CommandLineRunner initData(
            DummyData dummyData
    ) {
        return args -> dummyData.init();
    }
}
