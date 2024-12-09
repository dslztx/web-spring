package web.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import me.dslztx.assist.util.ArrayAssist;
import me.dslztx.assist.util.CollectionAssist;
import me.dslztx.assist.util.ObjectAssist;

@Component
public class CustomInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request1, HttpServletResponse response, Object handler) {

        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(request1);

        Request request0 = new Request();

        request0.setMethod(request.getMethod());
        request0.setUri(request.getRequestURI());
        request0.setProtocol(request.getProtocol());

        Enumeration<String> headerNames = request.getHeaderNames();

        List<Header> headers = new ArrayList<>();

        String name;
        while (headerNames.hasMoreElements()) {
            name = headerNames.nextElement();
            headers.add(new Header(name, request.getHeader(name)));
        }

        request0.setHeaders(headers);

        byte[] dd = request.getContentAsByteArray();
        if (ArrayAssist.isNotEmpty(dd)) {
            request0.setRequestBody(new String(dd));
        }

        logger.info("intercepted result request {}", request0.toString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws IOException {

        ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper)(response);

        Response response0 = new Response();

        response0.setStatusCode(wrappedResponse.getStatus());

        Collection<String> headerNames = wrappedResponse.getHeaderNames();
        if (CollectionAssist.isNotEmpty(headerNames)) {
            List<Header> headers = new ArrayList<>();
            for (String headerName : headerNames) {
                headers.add(new Header(headerName, wrappedResponse.getHeader(headerName)));
            }
            response0.setHeaders(headers);
        }

        byte[] dd = wrappedResponse.getContentAsByteArray();
        if (ArrayAssist.isNotEmpty(dd)) {
            response0.setResponseBody(new String(dd));
        }
        wrappedResponse.copyBodyToResponse();

        logger.info("intercepted result response {}", response0.toString());
    }
}

class Request {
    private String method;

    private String uri;

    private String protocol;

    private List<Header> headers;

    /**
     * 以字符串形式表示返回内容
     */
    private String requestBody;

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        sb.append(method).append(" ").append(uri).append(" ").append(protocol).append("\n");

        if (CollectionAssist.isNotEmpty(headers)) {
            for (Header header : headers) {
                sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
        }

        sb.append("\n");

        if (ObjectAssist.isNotNull(requestBody)) {
            sb.append(requestBody);
        }

        return sb.toString();
    }
}

class Response {

    private String protocol;

    private int statusCode;

    private String statusText;

    private List<Header> headers;

    /**
     * 以字符串形式表示返回内容
     */
    private String responseBody;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        sb.append(protocol).append(" ").append(statusCode).append(" ").append(statusText).append("\n");

        if (CollectionAssist.isNotEmpty(headers)) {
            for (Header header : headers) {
                sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
        }

        sb.append("\n");

        if (ObjectAssist.isNotNull(responseBody)) {
            sb.append(responseBody);
        }

        return sb.toString();
    }
}

class Header {
    String name;

    String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}