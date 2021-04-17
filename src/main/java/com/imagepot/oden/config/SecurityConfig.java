package com.imagepot.oden.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静的リソースへのアクセスにはセキュリティを適用しない
        web.ignoring().antMatchers("/css/**", "/dist/**", "/icomoon/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 直リンクの禁止
        // ログイン不要ページの設定
        // http.authorizeRequests()
        // .antMatchers("/css/**").permitAll() // リソースのアクセス許可
        // .antMatchers("/dist/**").permitAll() // リソースのアクセス許可
        // .antMatchers("/icomoon/**").permitAll() // リソースのアクセス許可
        // .antMatchers("/images/**").permitAll() // リソースのアクセス許可
        // .antMatchers("/").permitAll() // ログインページは直リンクOK
        // .antMatchers("/admin").hasAnyAuthority("ROLE_ADMIN") //
        // ユーザマスタテーブルのroleを指定（Springではロール名の先頭に"ROLE_"をつけるルールがあるのでDB登録時もこのルールび従う）
        // .antMatchers("/signout").permitAll()
        // .anyRequest().authenticated();// それ以外は直リンク禁止
        // ログイン処理
        http.formLogin()
                .loginPage("/top") // ログインページのhtmlファイルを指定（なければSpringSecurityのデフォルトページが表示される）
                .loginProcessingUrl("/signin") // ログイン画面のaction属性
                .failureUrl("/") // ログイン失敗時の遷移先
                .usernameParameter("signinEmail") // ログインページのユーザID（指定するパラメータ名はhtmlから探す）
                .passwordParameter("signinPassword") // ログインページのパスワード
                .defaultSuccessUrl("/home", true); // ログイン成功後の遷移先

        // RESTのみCSRF対策を無効に設定
        http.csrf().disable();
    }
}
