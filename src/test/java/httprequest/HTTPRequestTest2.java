package httprequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.dslztx.assist.util.ClassPathResourceAssist;

@Slf4j
public class HTTPRequestTest2 {

    @Test
    public void test0() {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/uploadChunked");

            httpPost.addHeader("connection", "Keep-Alive");
            httpPost.addHeader("Content-Type", "message/rfc822");
            httpPost.addHeader("X-Product", "out");

            // 当前HTTPClient客户端实现不支持
            // httpPost.addHeader("Trailer", "Expires");

            RedoSpamInputStreamEntity inputStreamEntity = new RedoSpamInputStreamEntity();

            // 需要设置的，否则不是Chunked协议，服务端不能正确接收解析，而返回0字节
            inputStreamEntity.setChunked(true);

            httpPost.setEntity(inputStreamEntity);

            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    httpClient.execute(httpPost);
                }
            }).start();

            byte[] bb = IOUtils.toByteArray(ClassPathResourceAssist.locateInputStream("1.eml"));
            System.out.println(bb.length);
            inputStreamEntity.write(bb, 0, bb.length);

            inputStreamEntity.setFinish();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
