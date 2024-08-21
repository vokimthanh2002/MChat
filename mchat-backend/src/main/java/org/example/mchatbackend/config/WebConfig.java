package org.example.mchatbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //Cau hinh CORS duoc them vao day
        registry.addMapping("/**")                                      //Ap dung cau hinh CORS cho tat ca endpoint trong ung dung.
                .allowedOrigins("http://localhost:5173")                           //Chi cho phep yeu cau tu 'http://localhost:5173'.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") //Cho phep chi dinh vao cac phuong thu HTTP.
                .allowedHeaders("*")                                               //Cho phep chi dinh vao cac tieu de HTTP headers duoc phep trong yeu cau CORS.
                .allowCredentials(true);                                           //Cho phep cac thong tin xac thuc (credentials) gui cac yeu cau CORS.
    }
}
