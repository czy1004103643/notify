package xyz.melodyl.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import xyz.melodyl.filter.SessionInterceptor;
import xyz.melodyl.pojo.User;
import xyz.melodyl.pojo.ticket.GenericTicketParser;
import xyz.melodyl.pojo.ticket.SlashTicketParser;
import xyz.melodyl.pojo.ticket.TicketParser;
import xyz.melodyl.pojo.ticket.URLTicketParser;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource(value = "classpath:db.properties")
@MapperScan("xyz.melodyl.mapper")
@ComponentScan("xyz.melodyl")
public class MvcConfig extends DelegatingWebMvcConfiguration {

    @Value("${ticket.password}")
    private String password;

    @Bean
    SessionInterceptor createSessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    public TicketParser createTicketParser() {
        GenericTicketParser genericTicketParser = new GenericTicketParser(password);
        URLTicketParser urlTicketParser = new URLTicketParser();
        SlashTicketParser slashTicketParser = new SlashTicketParser();

        slashTicketParser.setTicketParser(genericTicketParser);
        urlTicketParser.setTicketParser(slashTicketParser);

        return urlTicketParser;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");//添加静态资源展示路径
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(createSessionInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        converters.add(fastJsonHttpMessageConverter);
        super.configureMessageConverters(converters);
    }
}
