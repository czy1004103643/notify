package xyz.melodyl.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableTransactionManagement
public class DbConfig {

    @Value("${db.driverClass}")
    private String jdbcDriver;
    @Value("${db.jdbcUrl}")
    private String jdbcUrl;
    @Value("${db.user}")
    private String userName;
    @Value("${db.password}")
    private String password;
    @Value("${db.maxPoolSize}")
    private String maxPoolSize;
    @Value("${db.maxIdleTime}")
    private String maxIdleTime;
    @Value("${db.minPoolSize}")
    private String minPoolSize;
    @Value("${db.initialPoolSize}")
    private String initialPoolSize;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(this.c3p0DataSource());
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return sessionFactory.getObject();
    }

    @Bean
    public DataSource c3p0DataSource() throws PropertyVetoException {
        ComboPooledDataSource c3p0DS = new ComboPooledDataSource();
        c3p0DS.setDriverClass(jdbcDriver);
        c3p0DS.setJdbcUrl(jdbcUrl);
        c3p0DS.setUser(userName);
        c3p0DS.setPassword(password);
        c3p0DS.setMaxPoolSize(Integer.parseInt(maxPoolSize));
        c3p0DS.setMaxIdleTime(Integer.parseInt(maxIdleTime));
        c3p0DS.setMinPoolSize(Integer.parseInt(minPoolSize));
        c3p0DS.setInitialPoolSize(Integer.parseInt(initialPoolSize));
        return c3p0DS;
    }

    @Bean
    public DataSourceTransactionManager txManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(this.c3p0DataSource());
    }
}
