//package eu.com.cwsfe.cms.db.configuration;
//
//import org.mockito.Mockito;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Radoslaw Osinski.
// */
//@ComponentScan({"eu.com.cwsfe.cms.rest", "eu.com.cwsfe.cms.db", "eu.com.cwsfe.cms"})
//@Configuration
//@EnableWebMvc
//public class RestTestConfiguration extends WebMvcConfigurationSupport {
//
//    @Bean
//    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ArrayList<MediaType> supportedMediaTypes = new ArrayList<>();
//        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
//        return mappingJackson2HttpMessageConverter;
//    }
//
//    @Bean
//    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
//        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//        ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        messageConverters.add(getMappingJackson2HttpMessageConverter());
//        requestMappingHandlerAdapter.setMessageConverters(messageConverters);
//        return requestMappingHandlerAdapter;
//    }
//
//    @Bean
//    public StringHttpMessageConverter getStringHttpMessageConverter() {
//        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
//    }
//
//    @Override
//    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//    }
//
//    /**
//     * Repository already tested. Mock tests should be fast
//     */
//    @Bean
//    public JdbcTemplate getJdbcTemplate() {
//        return Mockito.mock(org.springframework.jdbc.core.JdbcTemplate.class);
//    }
//}
