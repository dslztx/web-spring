package httprequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.entity.AbstractHttpEntity;

public class RedoSpamInputStreamEntity extends AbstractHttpEntity {

    private ArrayBlockingQueue<byte[]> bufferQueue = new ArrayBlockingQueue<byte[]>(1);

    private volatile boolean isFinish = false;

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return null;
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        while (!isFinish || !bufferQueue.isEmpty()) {
            try {
                outStream.write(bufferQueue.take());
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    public void setFinish() {
        isFinish = true;
    }

    public void write(byte[] chunkBytes, int offset, int length) throws InterruptedException {
        byte[] content = new byte[length];
        System.arraycopy(chunkBytes, offset, content, 0, length);
        bufferQueue.put(content);
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
}