package org.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Slf4j
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        log.info("Root config classes");
        return new Class[]{
                AppConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        log.info("Servlet config classes");
        return new Class[]{
                WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        log.info("Servlet mappings");
        return new String[]{"/"};
    }
}