package vn.techres.order.online.security;// package vn.techres.order.temporary.security;

// import java.util.Arrays;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @Configuration
// @EnableWebSecurity
// public class WebSecurityConfig {
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//                 .authorizeHttpRequests((requests) -> requests
//                         // .requestMatchers("/api/v1/public/**").permitAll()
//                         // .requestMatchers("/**").permitAll()
//                         .anyRequest().permitAll());

//         return http.build();

//     }

//     @Bean
//     CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.setAllowedOrigins(Arrays.asList("*"));
//         configuration.setAllowedMethods(Arrays.asList("*"));
//         configuration.setAllowedHeaders(Arrays.asList("*"));
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }
// }
