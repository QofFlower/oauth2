# Spring Security OAuth

## 简介

oAuth协议为用户资源的授权提供了一个安全的、开放而又简易的标准。与以往的授权方式不同之处是oAuth的授权不会使第三饭触及到用户的账号和密码，即第三方无需使用用户的用户和密码就可以申请获得该用户资源的授权，因此oAuth是安全的。



## 应用

### 授权码模式认证

1.导入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
	<version>2.2.4.RELEASE</version>
</dependency>
```



2.配置授权客户端配置类

```java
/**
 * @author :花のQ
 * @since 2020/10/16 21:16
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 配置客户端信息，注意这里不是登录用户信息，而是可以访问系统的客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用内存设置
                .inMemory()
                // 客户端
                .withClient("client")
                // 客户端密码
                .secret(passwordEncoder.encode("secret"))
                // 授权类型
                .authorizedGrantTypes("authorization_code")
                // 授权范围
                .scopes("app")
                // 注册回调地址
                .redirectUris("https://bilibili.com");
    }
	
	// 允许表单请求
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()// 开启表单认证，主要是让/oauth/token支持client_id和client_secret作登录认证
                .tokenKeyAccess("permitAll()")// 开启/oauth/token_key验证端口无权限访问
                .checkTokenAccess("permitAll");// 开启/oauth/check_token验证端口认证无权限访问
    }
}
```



3.配置用户信息配置类

```java
/**
 * @author :花のQ
 * @since 2020/10/16 21:23
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 密码加密
     * @return password encoder util class
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置登录用户信息，密码需要加密处理
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                // 用户名
                .withUser("admin")
                // 密码(加密处理)
                .password(passwordEncoder().encode("123456"))
                // 用户角色
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("123456")).roles("USER");
    }
}
```



4.[访问路径](localhost:8888/oauth/authorize?client_id=client&scope=app&response_type=code)

并选择授权![image-20201016225044425](C:\Users\27135\AppData\Roaming\Typora\typora-user-images\image-20201016225044425.png)

将会跳转到定义的页面(bilibili)，并携带授权码

![image-20201016225131880](C:\Users\27135\AppData\Roaming\Typora\typora-user-images\image-20201016225131880.png)





5.在postman里对 [路径](localhost:8888/oauth/token) 请求并使用表单请求，将返回授权token

![image-20201016225443517](C:\Users\27135\AppData\Roaming\Typora\typora-user-images\image-20201016225443517.png)





---

### 密码模式认证

要支持密码认证，需要添加password模式：

- 在WebSecurityConfiguration类中添加：

```java
/**
* 密码模式认证
* @return
* @throws Exception
*/
@Bean
@Override
public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManagerBean();
}
```



- 在AuthenticationServerConfiguration中添加：

```java
@Autowired
private AuthenticationManager authenticationManager;

/**
* 密码认证模式
* @param endpoints
* @throws Exception
*/
@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager);
}
```



- 在客户端配置的授权类型中添加password密码模式

```java
/**
     * 配置客户端信息，注意这里不是登录用户信息，而是可以访问系统的客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用内存设置
                .inMemory()
                // 客户端
                .withClient("client")
                // 客户端密码
                .secret(passwordEncoder.encode("secret"))
                // 授权类型
                .authorizedGrantTypes("authorization_code","password")
                // 授权范围
                .scopes("app")
                // 注册回调地址
                .redirectUris("https://bilibili.com");
    }
```



- 使用postman请求接口localhost:8888/oauth/token，获取token

---

### 客户端凭证模式

- 在客户端配置中的授权类型添加client_credentials

```java
/**
     * 配置客户端信息，注意这里不是登录用户信息，而是可以访问系统的客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用内存设置
                .inMemory()
                // 客户端
                .withClient("client")
                // 客户端密码
                .secret(passwordEncoder.encode("secret"))
                // 授权类型
                .authorizedGrantTypes("authorization_code","password","client_credentials")
                // 授权范围
                .scopes("app")
                // 注册回调地址
                .redirectUris("https://bilibili.com");
    }
```

