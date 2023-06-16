package site.bidderown.server.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll()
                .and()
//                    .csrf().ignoringAntMatchers("/h2-console/**") TODO csrf 설정
                    .csrf().ignoringAntMatchers("/**")
                .and()
                    .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                    .formLogin(
                            formLogin -> formLogin
                                    .loginPage("/login")
                                    .loginProcessingUrl("/loginProc")
                                    .defaultSuccessUrl("/home")
                    )
                    .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                                .defaultSuccessUrl("/home")
                    )
                    .logout(
                            logout -> logout
                                    .logoutUrl("/logout")
                                    .logoutSuccessUrl("/login") // TODO logout url 변경해야됨
                    );

        return http.build();
    }
}
