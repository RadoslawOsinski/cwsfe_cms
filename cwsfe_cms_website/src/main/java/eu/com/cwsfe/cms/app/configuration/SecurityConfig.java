package eu.com.cwsfe.cms.app.configuration;

import eu.com.cwsfe.cms.web.login.CmsAuthProvider;
import eu.com.cwsfe.cms.web.login.CmsAuthenticationProcessingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@EnableWebSecurity
@Import(BeforeSecurityConfiguration.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String NEWS_IMAGES = "/newsImages/";
    public static final String BLOG_POST_IMAGES = "/blogPostImages/";

    @Bean
    public CmsAuthProvider getCmsAuthProvider() {
        return new CmsAuthProvider();
    }

    @Bean
    public CmsAuthenticationProcessingFilter getCmsAuthenticationProcessingFilter() {
        CmsAuthenticationProcessingFilter cmsAuthenticationProcessingFilter = new CmsAuthenticationProcessingFilter();
        cmsAuthenticationProcessingFilter.setAuthenticationManager(getAuthenticationManager());
        cmsAuthenticationProcessingFilter.setFilterProcessesUrl("/cwsfe_cms_security_check");
        cmsAuthenticationProcessingFilter.setAuthenticationFailureHandler(getCmsLoginFailureHandler());
        cmsAuthenticationProcessingFilter.setAuthenticationSuccessHandler(getCmsLoginSuccessHandler());
        return cmsAuthenticationProcessingFilter;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(getCmsAuthProvider());
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler getCmsLoginSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl("/Main");
        savedRequestAwareAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
        return savedRequestAwareAuthenticationSuccessHandler;
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler getCmsLoginFailureHandler() {
        SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
        simpleUrlAuthenticationFailureHandler.setDefaultFailureUrl("/loginFailed");
        return simpleUrlAuthenticationFailureHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getCmsAuthProvider()).parentAuthenticationManager(getAuthenticationManager());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(
                "/loginPage", "/loginFailed", "/configuration/initialConfiguration",
                "favicon.ico", "/rest/**", "/resources-cwsfe-cms/**",
                NEWS_IMAGES + "**", BLOG_POST_IMAGES + "**",
                "/swagger-ui.html", "/webjars/**", "/swagger-resources", "/swagger-resources/**",
                "/v2", "/v2/**"
            ).permitAll()
            .antMatchers("/**").hasRole("CWSFE_CMS_ADMIN")
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/loginPage").defaultSuccessUrl("/loginPage").failureUrl("/loginFailed").permitAll()
            .and().logout().logoutSuccessUrl("/loginPage").invalidateHttpSession(true).logoutUrl("/logout").permitAll()
            .and().addFilterAfter(getCmsAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            .authenticationProvider(getCmsAuthProvider());
    }

}

