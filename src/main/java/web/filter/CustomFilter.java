package web.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper =
            new ContentCachingRequestWrapper((HttpServletRequest)servletRequest);

        ContentCachingResponseWrapper responseWrapper =
            new ContentCachingResponseWrapper((HttpServletResponse)servletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {}

    @Override
    public void destroy() {}
}
