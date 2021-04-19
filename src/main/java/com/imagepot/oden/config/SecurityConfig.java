package com.imagepot.oden.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("UserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静的リソースへのアクセスにはセキュリティを適用しない
        web.ignoring().antMatchers("/css/**", "/dist/**", "/icomoon/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // SpringSecurity除外設定
        http.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/icomoon/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers("/admin").hasAnyAuthority("ROLE_ADMIN") // adminのみ許可
                .antMatchers("/signout").permitAll()
                .anyRequest().authenticated();// それ以外は直リンク禁止

        // ログイン処理
        http.formLogin()
                .loginPage("/").permitAll() // ログインページのhtmlファイルを指定
                .loginProcessingUrl("/signin") // ログイン画面のaction属性
                .failureUrl("/") // ログイン失敗時の遷移先
                .usernameParameter("signinEmail") // ログインに必要なパラメータ
                .passwordParameter("signinPassword")
                .defaultSuccessUrl("/home", true); // ログイン成功後の遷移先

        // ログアウト処理
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/signout"))
                .logoutSuccessUrl("/");
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
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
