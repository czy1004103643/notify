package xyz.melodyl.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import xyz.melodyl.filter.DomainFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class ProjectInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext webApplicationContext = createWebApplicationContext(servletContext);
        addServlet(servletContext, webApplicationContext);
        addFilter(servletContext);
    }

    private WebApplicationContext createWebApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(MvcConfig.class);
        webApplicationContext.setServletContext(servletContext);

        return webApplicationContext;
    }

    private void addServlet(ServletContext servletContext, WebApplicationContext webApplicationContext) {
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(webApplicationContext));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }

    // nginx 解决跨域，所以不添加
    private void addFilter(ServletContext servletContext) {
//        FilterRegistration.Dynamic filter = servletContext.addFilter(DomainFilter.class.getSimpleName(), DomainFilter.class);
//        filter.addMappingForUrlPatterns(null, false, "/*");
    }
}
