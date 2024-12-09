package web;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import lombok.extern.slf4j.Slf4j;
import web.configuration.AppConfiguration;
import web.jetty.JettyServer;

@Slf4j
public class ApplicationBooter {

    public static void main(String[] args) throws Exception {
        int port = -1;

        try {
            port = Integer.valueOf(System.getenv("PORT"));
        } catch (Exception e) {
            log.error("", e);
        }

        if (port == -1) {
            throw new RuntimeException("no assigned port");
        }

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfiguration.class);

        JettyServer.startJettyServer(port, context);
    }

}
