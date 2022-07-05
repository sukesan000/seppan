package com.example.seppan.security;

import com.example.seppan.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //フォームの値と比較するDBから取得したパスワードは暗号化されているのでフォームの値も暗号化するために利用
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    //静的ファイル(css,javascript)を利用する際のリクエストまで弾いてしまわないための設定
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(false).ignoring().antMatchers(
                "/css/**",
                "/js/**"
        );
    }

    //認証・認可の処理の中でhttpリクエスト関連の部分についての設定を記述するためのメソッド
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //「login.html」はログイン不要でアクセス可能に設定
                .antMatchers("/login", "/error").permitAll()
                //上記以外は直リンク禁止
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //ログイン処理のパス
                .loginProcessingUrl("/sign_in")
                //ログインページ
                .loginPage("/login")
                //ログインエラー時の遷移先 ※パラメーターに「error」を付与
                .failureUrl("/login?error")
                //ログイン成功時の遷移先
                .defaultSuccessUrl("/seppan/top")
                //ログイン時のキー：名前
                .usernameParameter("username")
                //ログイン時のパスワード
                .passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutUrl("/logout") //ログアウトのURL
                .invalidateHttpSession(true)
                //ログアウト時の遷移先 POSTでアクセス
                .logoutSuccessUrl("/login?logout");
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
