package com.imagepot.xyztk.security;

import com.imagepot.xyztk.service.LoginUserService;
import com.imagepot.xyztk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginUserService loginUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(LoginUserService loginUserService, PasswordEncoder passwordEncoder) {
        this.loginUserService = loginUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // SpringSecurity除外設定
        http.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/icomoon/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers("/admin").hasAnyAuthority("ROLE_ADMIN") // adminのみ許可
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated();// それ以外は直リンク禁止

        // ログイン処理
        http.formLogin()
                .loginPage("/").permitAll() // ログインページのhtmlファイルを指定
                .loginProcessingUrl("/login") // ログイン画面のaction属性
                .failureUrl("/") // ログイン失敗時の遷移先
                .usernameParameter("email") // ログインに必要なパラメータ
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true); // ログイン成功後の遷移先

        // ログアウト処理
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");

        http
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.jdbcAuthentication().dataSource(dataSource)
        // .usersByUsernameQuery(
        // "SELECT user_email as username, user_password, true FROM mywork.pot_user
        // WHERE user_email = ?") // serviceで実装したい
        // .authoritiesByUsernameQuery(
        // "SELECT user_email, user_role FROM mywork.pot_user WHERE user_email = ?")
        // .passwordEncoder(passwordEncoder()); // 復号
        auth.userDetailsService(loginUserService)
                .passwordEncoder(passwordEncoder);
    }
}
