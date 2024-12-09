package web.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import web.filter.CustomFilter;

public class JettyServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);

    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";

    public static void startJettyServer(int port, WebApplicationContext webApplicationContext) throws Exception {
        logger.info("starting jetty server listening on port {}", port);

        Server server = new Server(port);

        server.setHandler(buildServletContextHandler(webApplicationContext));

        server.start();

        logger.info("the jetty server has started listening on port {}", port);

        // 这个join()操作不是为了让Jetty Server彻底起来，网上很多人理解错了，而是：Jetty Server起来后，挂起当前线程，直到Jetty Server彻底关闭后，所以join()
        // 调用之后的逻辑应该是关闭Jetty Server后的一些清理后置操作
        // 如果不需要可以先不要这个join()调用
        // server.join();
    }

    public static void startJettyServerWithFilter(int port, WebApplicationContext webApplicationContext)
        throws Exception {

        logger.info("starting jetty server listening on port {}", port);

        Server server = new Server(port);

        server.setHandler(buildServletContextHandlerWithFilter(webApplicationContext));

        server.start();

        logger.info("the jetty server has started listening on port {}", port);

        // 这个join()操作不是为了让Jetty Server彻底起来，网上很多人理解错了，而是：Jetty Server起来后，挂起当前线程，直到Jetty Server彻底关闭后，所以join()
        // 调用之后的逻辑应该是关闭Jetty Server后的一些清理后置操作
        // 如果不需要可以先不要这个join()调用
        // server.join();
    }

    private static ServletContextHandler buildServletContextHandler(WebApplicationContext webApplicationContext) {
        ServletContextHandler handler = new ServletContextHandler();

        handler.setContextPath(CONTEXT_PATH);
        handler.addServlet(new ServletHolder(new DispatcherServlet(webApplicationContext)), MAPPING_URL);
        handler.addEventListener(new ContextLoaderListener(webApplicationContext));

        return handler;
    }

    private static ServletContextHandler
        buildServletContextHandlerWithFilter(WebApplicationContext webApplicationContext) {

        ServletContextHandler handler = buildServletContextHandler(webApplicationContext);

        handler.addFilter(CustomFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        return handler;
    }

}
