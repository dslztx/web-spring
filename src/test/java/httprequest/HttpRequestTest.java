package httprequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import me.dslztx.assist.util.ArrayAssist;
import me.dslztx.assist.util.ClassPathResourceAssist;
import me.dslztx.assist.util.CloseableAssist;
import me.dslztx.assist.util.ObjectAssist;
import web.domain.User;

/**
 * 先手动开启Jetty
 */
public class HttpRequestTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestTest.class);

    @Test
    public void getTest0() {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;

        try {
            httpClient = HttpClients.createDefault();

            HttpGet request = new HttpGet("http://127.0.0.1:8080/hello/way");

            // add request headers
            request.addHeader("custom-key", "mkyong");
            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            response = httpClient.execute(request);

            StringBuilder sb = new StringBuilder();

            sb.append(response.getStatusLine().toString()).append("\n");

            Header[] headers = response.getAllHeaders();
            if (ArrayAssist.isNotEmpty(headers)) {
                for (Header header : headers) {
                    sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
                }
            }

            sb.append("\n");

            if (ObjectAssist.isNotNull(response.getEntity())) {
                sb.append(EntityUtils.toString(response.getEntity()));
            }

            System.out.println(sb.toString());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("get request fail");
            }
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        } finally {
            CloseableAssist.closeQuietly(response);
            CloseableAssist.closeQuietly(httpClient);
        }

    }

    @Test
    public void postTest0() {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;

        try {
            httpClient = HttpClients.createDefault();

            HttpPost post = new HttpPost("http://127.0.0.1:8080/addUser");

            User user = new User("post user", 10);

            String s = JSON.toJSONString(user);

            StringEntity stringEntity = new StringEntity(s, ContentType.APPLICATION_JSON);

            post.setEntity(stringEntity);

            response = httpClient.execute(post);

            StringBuilder sb = new StringBuilder();

            sb.append(response.getStatusLine().toString()).append("\n");

            Header[] headers = response.getAllHeaders();
            if (ArrayAssist.isNotEmpty(headers)) {
                for (Header header : headers) {
                    sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
                }
            }

            sb.append("\n");

            if (ObjectAssist.isNotNull(response.getEntity())) {
                sb.append(EntityUtils.toString(response.getEntity()));
            }

            System.out.println(sb.toString());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("get request fail");
            }
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        } finally {
            CloseableAssist.closeQuietly(response);
            CloseableAssist.closeQuietly(httpClient);
        }

    }

    @Test
    public void postDataStreamTest1() {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;

        try {
            httpClient = HttpClients.createDefault();

            HttpPost post = new HttpPost("http://127.0.0.1:8080/upload/datastream");

            User user = new User("post user", 10);

            String s = JSON.toJSONString(user);
            ByteArrayEntity entity = new ByteArrayEntity(s.getBytes(), ContentType.APPLICATION_OCTET_STREAM);
            post.setEntity(entity);

            response = httpClient.execute(post);

            StringBuilder sb = new StringBuilder();

            sb.append(response.getStatusLine().toString()).append("\n");

            Header[] headers = response.getAllHeaders();
            if (ArrayAssist.isNotEmpty(headers)) {
                for (Header header : headers) {
                    sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
                }
            }

            sb.append("\n");

            if (ObjectAssist.isNotNull(response.getEntity())) {
                sb.append(EntityUtils.toString(response.getEntity()));
            }

            System.out.println(sb.toString());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("get request fail");
            }

        } catch (SocketTimeoutException ee) {
            logger.error("", ee);
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        } finally {
            CloseableAssist.closeQuietly(response);
            CloseableAssist.closeQuietly(httpClient);
        }

    }

    @Test
    public void postTimeoutTest1() {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;

        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000).setSocketTimeout(5000).build();

            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

            HttpPost post = new HttpPost("http://127.0.0.1:8080/timeout");

            User user = new User("post user", 10);

            String s = JSON.toJSONString(user);
            ByteArrayEntity entity = new ByteArrayEntity(s.getBytes(), ContentType.APPLICATION_OCTET_STREAM);
            post.setEntity(entity);

            response = httpClient.execute(post);

            StringBuilder sb = new StringBuilder();

            sb.append(response.getStatusLine().toString()).append("\n");

            Header[] headers = response.getAllHeaders();
            if (ArrayAssist.isNotEmpty(headers)) {
                for (Header header : headers) {
                    sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
                }
            }

            sb.append("\n");

            if (ObjectAssist.isNotNull(response.getEntity())) {
                sb.append(EntityUtils.toString(response.getEntity()));
            }

            System.out.println(sb.toString());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("get request fail");
            }

            Assert.fail();
        } catch (SocketTimeoutException expected) {
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        } finally {
            CloseableAssist.closeQuietly(response);
            CloseableAssist.closeQuietly(httpClient);
        }

    }

    /**
     * 如果是HTTP 1.1，默认开启HTTP长连接；如果是HTTP 1.0，默认开启HTTP短连接
     * <p>
     * HTTP长连接需要HTTP协议参与，在使用HTTP长连接的情形中： <br/>
     * - 如果使用HttpClient客户端没有任何问题 <br/>
     * - 如果直接使用Socket，会出现响应头立即收到，响应正文很久才收到现象，所以：1）使用HTTP 1 .0不使用长连接；2）加上"Connection: close"头不使用长连接
     */
    @Test
    public void getUseSocketStraightlyTest1() {
        try {
            StringBuilder httpHeader = new StringBuilder();
            httpHeader.append("GET /hello HTTP/1.1\r\n");
            httpHeader.append("Accept-Language: en-us\r\n");
            httpHeader.append("Accept: */*\r\n");
            httpHeader.append("HOST: 127.0.0.1:8080\r\n");
            httpHeader.append("User-Agent: Googlebot\r\n");
            httpHeader.append("Connection: close\r\n");
            httpHeader.append("\r\n");

            Socket socket = new Socket("127.0.0.1", 8080);
            socket.setSoTimeout(5000);

            OutputStream out = socket.getOutputStream();

            IOUtils.write(httpHeader.toString().getBytes(), out);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String res = null;
            while ((res = in.readLine()) != null) {
                System.out.println(res);
            }
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        }
    }

    @Test
    public void postUseSocketStraightlyTest1() {
        try {
            StringBuilder postBodyHeader = new StringBuilder();

            postBodyHeader.append("X-CTCH-Pver: 0000001\r\n");
            postBodyHeader.append("X-CTCH-SenderIP: 127.0.0.1\r\n");
            postBodyHeader.append("X-CTCH-MailFrom: not_exist_email@gmail.com\r\n");
            postBodyHeader.append("X-CTCH-MidRcpt: 1tbiBRaghFl2uRkwXgAAsW\r\n");
            postBodyHeader.append("X-CTCH-SUBJECT: testsubject\r\n");
            postBodyHeader.append("X-CTCH-TRUNCATE: false\r\n");
            postBodyHeader.append("\r\n");

            byte[] dd = FileUtils.readFileToByteArray(ClassPathResourceAssist.locateFileNotInJar("1.eml"));

            StringBuilder httpHeader = new StringBuilder();
            httpHeader.append("POST /upload/datastream HTTP/1.1\r\n");
            httpHeader.append("Accept-Language: en-us\r\n");
            httpHeader.append("Accept: */*\r\n");
            httpHeader.append("HOST: 127.0.0.1:8080\r\n");
            httpHeader.append("User-Agent: Commtouch HTTP Client\r\n");
            httpHeader.append("Connection: close\r\n");
            httpHeader.append("Content-Type: application/octet-stream\r\n");

            int requestBodyLength = postBodyHeader.toString().getBytes().length + dd.length;

            httpHeader.append("Content-Length: " + requestBodyLength).append("\r\n");
            httpHeader.append("\r\n");

            Socket socket = new Socket("127.0.0.1", 8080);
            socket.setSoTimeout(5000);

            OutputStream out = socket.getOutputStream();

            IOUtils.write(httpHeader.toString().getBytes(), out);
            IOUtils.write(postBodyHeader.toString().getBytes(), out);
            IOUtils.write(dd, out);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String res = null;
            while ((res = in.readLine()) != null) {
                System.out.println(res);
            }
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail();
        }
    }

    @Test
    public void testMailIdentify() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://localhost:8080/mail/identify");

        byte[] mailData = IOUtils.toByteArray(ClassPathResourceAssist.locateInputStream("test.eml"));

        // 邮件完全加载进内存了，可以让httpclient自动设置Content-Length
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "message/rfc822");
        httpPost.addHeader(HttpHeaders.CONTENT_MD5, DigestUtils.md5Hex(mailData));
        httpPost.addHeader("X-SMTP-Remote-Address", "1.2.3.4");
        httpPost.addHeader("X-SMTP-Helo", "test.com");
        httpPost.addHeader("X-SMTP-Sender", "a@test.com");
        httpPost.addHeader("X-SMTP-Recipients", String.join(",", "b@example.com", "c@example.com"));
        httpPost.addHeader("X-ANTISPAM-Message-Id", UUID.randomUUID().toString());
        httpPost.addHeader("X-ANTISPAM-Message-Size", String.valueOf(mailData.length));
        httpPost.addHeader("X-ANTISPAM-Message-Truncated", String.valueOf(false));
        httpPost.addHeader("X-ANTISPAM-Product", "in");
        httpPost.addHeader("X-ANTISPAM-Client", "127.0.0.1);");
        httpPost.addHeader("X-ANTISPAM-Extras",
            Base64.encodeBase64String("{\"mType\":\"NORMAL\"}".getBytes(StandardCharsets.UTF_8)));
        // 这里没有考虑截断
        httpPost.setEntity(new ByteArrayEntity(mailData));

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            String content = EntityUtils.toString(httpResponse.getEntity());

            System.out.println(content);
        }
    }

}
