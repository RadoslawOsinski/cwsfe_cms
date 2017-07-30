package eu.com.cwsfe.cms.web.configuration;

import eu.com.cwsfe.cms.db.configuration.*;
import eu.com.cwsfe.cms.web.login.SecurityConfig;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Created by Radosław Osiński
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
//@Import(value = {RestConfiguration.class})
@ComponentScan(value = "eu.com.cwsfe.cms", excludeFilters = @ComponentScan.Filter(
    value = {
        CwsfeCmsApplicationConfig.class, RestConfiguration.class, HazelcastIntegrationInstance.class,
        DataSourceConfiguration.class, SecurityConfig.class
    }, type = FilterType.ASSIGNABLE_TYPE
))
public class CwsfeCmsWebContext extends WebMvcConfigurerAdapter {


    private LocaleChangeInterceptor getLocaleChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("cmsLanguage");
        return localeChangeInterceptor;
    }

    @Bean
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(2000000000);
        return commonsMultipartResolver;
    }

    @Bean
    public BeanNameViewResolver getBeanNameViewResolver() {
        return new BeanNameViewResolver();
    }

    @Bean
    public CookieLocaleResolver getCookieLocaleResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/pages/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    /**
     * spring sucks in defining different id than messageSource :(
     * @return message source
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:cwsfe_cms_i18n");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        reloadableResourceBundleMessageSource.setCacheSeconds(10);
        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setSearchSystemEnvironment(true);
        return propertyPlaceholderConfigurer;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLocaleChangeInterceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        allowed-origins="${cors.allowed.origins}"
        registry.addMapping("/rest/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*").allowCredentials(false);
        super.addCorsMappings(registry);
    }

    /**
     * Handles HTTP GET requests for /resources-cwsfe-cms/** by efficiently serving up static resources in the ${webappRoot}/resources-cwsfe-cms/ directory
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources-cwsfe-cms/**").addResourceLocations("/resources-cwsfe-cms/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public Docket privilegesApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("CWSFE CMS Rest API")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).build();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        super.configureMessageConverters(converters);
    }

}
