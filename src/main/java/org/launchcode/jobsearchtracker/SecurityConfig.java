package org.launchcode.jobsearchtracker;

import org.launchcode.jobsearchtracker.models.CustomOAuth2User;
import org.launchcode.jobsearchtracker.models.CustomOAuth2UserService;
import org.launchcode.jobsearchtracker.models.MyUserDetailsService;
import org.launchcode.jobsearchtracker.models.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                    .antMatchers("css/**", "/resources/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/login", "/oauth2/**", "/css/**", "/js/**").permitAll()
                    // allows anyone to access a URL that begins with "/resources"
                    // since this is where my CSS, JavaScript and images are stored
                    // all my static resources are viewable by anyone
//                    .antMatchers("../static/**").permitAll()
                .anyRequest().authenticated()   // every request requires the user to be authenticated
                .and()
                .formLogin()    // form based authentication is supported
                    .permitAll()
                    .loginPage("/login")     // when authentication is required, redirect the browswer to "/login"
                    .loginProcessingUrl("/login")
//                    .defaultSuccessUrl("/dashboard.html", true)
                    .successHandler(
                            new AuthenticationSuccessHandler() {
                                @Override
                                public void onAuthenticationSuccess(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    Authentication authentication)
                                        throws IOException, ServletException {

                                    response.sendRedirect("/dashboard");
                                }
                            }
                    )
                .and()
                .oauth2Login()
                    .permitAll()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oauthUserService)
                    .and()
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                            Authentication authentication) throws IOException, ServletException {

                            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

//                            userService.processOAuthPostLogin(oAuth2User.getEmail()); // getName
                            userService.processOAuthPostLogin(oAuth2User.getName(), oAuth2User.getEmail());

                            response.sendRedirect("/dashboard");
                        }
                    })
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .and()
                .logout().permitAll()
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
