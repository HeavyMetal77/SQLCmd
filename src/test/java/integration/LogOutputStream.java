package integration;

import java.io.IOException;
import java.io.OutputStream;

public class LogOutputStream extends OutputStream {
    private String log;

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char) b);
    }

    public String getData() {
        return log;
    }
}
