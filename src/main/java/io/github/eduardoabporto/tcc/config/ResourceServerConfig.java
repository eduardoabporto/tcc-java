package io.github.eduardoabporto.tcc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/usuario/**").permitAll()
                .antMatchers(
                        "/api/empresa/**",
                        "/api/clientes/**",
                        "/api/ordem-servico/**",
                        "/api/recurso/**",
                        "/api/projeto/**"
                        ).authenticated()
                .anyRequest().denyAll();

        ;
    }
}
