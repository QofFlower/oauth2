package com.hananoq.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author :花のQ
 * @since 2020/11/23 16:36
 * 令牌管理类
 **/
@Configuration
public class TokenConfig {

    // 该密钥在资源服务和授权服务必须一致
    // The key must be identical between the resource server and the authorization server
    private String SIGNING_KEY = "hananoq";

    /*@Bean
    public TokenStore tokenStore(){
        // 暂时使用内存存储令牌
        return new InMemoryTokenStore();
    }*/

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
        return converter;
    }

}
