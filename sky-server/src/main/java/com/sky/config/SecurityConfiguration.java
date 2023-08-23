//package com.sky.config;
//import com.sky.utils.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.parameters.P;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//
//@Configuration
//public class SpringSecurityConfig {
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    //获取AuthenticationManager（认证管理器），登录时认证使用
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    //配置过滤
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)//关闭csrf
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭session
//                .and()
//                .authorizeRequests(auth -> auth
//                                .requestMatchers("/api/auth/**", "/error").permitAll()
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(conf -> conf
//                        .loginProcessingUrl("/api/auth/**")
//                        .failureHandler(this::handleProcess)
//                        .successHandler(this::handleProcess)
//                        .permitAll()
//                )
//                .logout(conf -> conf
//                        .logoutUrl("/api/auth/logout")
//                        .logoutSuccessHandler(this::onLogoutSuccess)
//                )
//                .exceptionHandling(conf -> conf
//                        .accessDeniedHandler(this::handleProcess)
//                        .authenticationEntryPoint(this::handleProcess)
//                )
//                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
//                .userDetailsService(userDetailsService).build();
//    }
//
//    //配置加密方式
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //配置跨源访问(CORS)
//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
//    /**
//     * 将多种类型的Handler整合到同一个方法中，包含：
//     * - 登录成功
//     * - 登录失败
//     * - 未登录拦截/无权限拦截
//     * @param request 请求
//     * @param response 响应
//     * @param exceptionOrAuthentication 异常或是验证实体
//     * @throws IOException 可能的异常
//     */
//    private void handleProcess(HttpServletRequest request,
//                               HttpServletResponse response,
//                               Object exceptionOrAuthentication) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
//            writer.write(RestBean
//                    .forbidden(exception.getMessage()).asJsonString());
//        } else if(exceptionOrAuthentication instanceof Exception exception) {
//            writer.write(RestBean
//                    .unauthorized(exception.getMessage()).asJsonString());
//        } else if(exceptionOrAuthentication instanceof Authentication authentication){
//            User user = (User) authentication.getPrincipal();
//            Account account = service.findAccountByNameOrEmail(user.getUsername());
//            String jwt = utils.createJwt(user, account.getUsername(), account.getId());
//            if(jwt == null) {
//                writer.write(RestBean.forbidden("登录验证频繁，请稍后再试").asJsonString());
//            } else {
//                AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, o -> o.setToken(jwt));
//                vo.setExpire(utils.expireTime());
//                writer.write(RestBean.success(vo).asJsonString());
//            }
//        }
//    }
//
//    /**
//     * 退出登录处理，将对应的Jwt令牌列入黑名单不再使用
//     * @param request 请求
//     * @param response 响应
//     * @param authentication 验证实体
//     * @throws IOException 可能的异常
//     */
//    private void onLogoutSuccess(HttpServletRequest request,
//                                 HttpServletResponse response,
//                                 Authentication authentication) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        String authorization = request.getHeader("Authorization");
//        if(utils.invalidateJwt(authorization)) {
//            writer.write(RestBean.success("退出登录成功").asJsonString());
//            return;
//        }
//        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
//    }
//}
