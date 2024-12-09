package web.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 在ApplicationBooter中会显式加载本配置类，然后会发生：<br/>
 * 1、基于@Configuration注解：本类作为主配置类<br/>
 * 2、基于@ComponentScan注解：配置了包扫描路径<br/>
 * 3、基于@Import注解：加载另外一个配置类MvcConfiguration<br/>
 */
@Configuration
@ComponentScan(basePackages = {"web"})
@Import({MvcConfiguration.class})
public class AppConfiguration {

}