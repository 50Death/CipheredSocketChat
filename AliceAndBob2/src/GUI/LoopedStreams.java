package GUI;

import java.io.*;

public class LoopedStreams {


    private PipedOutputStream pipedOS =
            new PipedOutputStream();
    private boolean keepRunning = true;
    private ByteArrayOutputStream byteArrayOS =
            new ByteArrayOutputStream(1024) {
                public void close() {
                    keepRunning = false;
                    try {
                        super.close();
                        pipedOS.close();
                    } catch (IOException e) {
                        System.exit(1);
                    }
                }
            };

    private PipedInputStream pipedIS = new PipedInputStream() {
        public void close() {
            keepRunning = false;
            try {
                super.close();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    };

    public LoopedStreams() throws IOException {
        pipedIS.connect(pipedOS);
        startByteArrayReaderThread();
    }

    public InputStream getInputStream() {
        return pipedIS;
    }

    public OutputStream getOutputStream() {
        return byteArrayOS;
    }

    private void startByteArrayReaderThread() {
        new Thread(new Runnable() {
            public void run() {
                while (keepRunning) {
                    if (byteArrayOS.size() > 0) {
                        byte[] buffer = null;
                        synchronized (byteArrayOS) {
                            buffer = byteArrayOS.toByteArray();
                            byteArrayOS.reset(); // 清除缓冲区
                        }
                        try {
                            pipedOS.write(buffer, 0, buffer.length);
                        } catch (IOException e) {
                            System.exit(1);
                        }

                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
// TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}