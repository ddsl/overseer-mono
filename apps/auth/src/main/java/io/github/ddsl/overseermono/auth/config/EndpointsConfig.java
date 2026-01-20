package io.github.ddsl.overseermono.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;

@Configuration
public class EndpointsConfig {
    @Bean
    RequestMatcher authEndpointsMatcher() {
        return RequestMatchers.anyOf(
                PathPatternRequestMatcher.withDefaults().matcher("/auth/signup"),
                PathPatternRequestMatcher.withDefaults().matcher("/auth/signin"),
                PathPatternRequestMatcher.withDefaults().matcher("/auth/logout"),
                PathPatternRequestMatcher.withDefaults().matcher("/error")
        );
    }
}
