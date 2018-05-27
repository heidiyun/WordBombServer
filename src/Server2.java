import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server2 {
    private static String[] words = new String[]{"hello", "hi", "yun",
            "jeong", "kikat", "wow", "ming", "do", "kyung", "soo",
            "foxtail", "the", "lab", "gugugu", "smile", "picture"};
    public static StringBuilder builder = new StringBuilder();
    public static Thread thread;

    public static  int i;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1000);
        new Thread(() -> {
            while (true) {
                i = (i + 1) % words.length;
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    break;
//                }
            }
        }).start();

        while (true) {

            Socket socket = serverSocket.accept();
            System.out.println(socket.getRemoteSocketAddress());

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            Thread connectionThread = new Thread(new ConnectionThread(socket, words));
//            Thread readThread = new Thread(new ConnectionReadingThread(socket));
//            Thread sendThread = new Thread(new ConnectionSendThread(socket));
            connectionThread.start();
//            readThread.start();
//            sendThread.start();
        }
    }
}

class ConnectionSendThread implements Runnable {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    ConnectionSendThread (Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();

    }

    @Override
    public void run() {
        while(true) {
            try {
                os.write(Server2.builder.toString().getBytes());
                Server2.builder.setLength(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class ConnectionReadingThread implements Runnable {

    private Socket socket;
    private InputStream is;
    private OutputStream os;

    ConnectionReadingThread(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    @Override
    public void run() {
        int len;
        byte[] bytes = new byte[128];

        try {

            while ((len = is.read(bytes)) != -1) {
                if (len != 0) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("r");
                    builder.append(",");
                    builder.append(new String(bytes, 0, len));
                    Server2.builder.append(builder);
                    builder.setLength(0);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class ConnectionThread implements Runnable{

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String[] words;



    ConnectionThread(Socket socket, String[] words) throws IOException {
        this.socket = socket;
        this.words = words;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }


    @Override
    public void run() {

        while (true)
        {
            // for (int i = 0; i < words.length; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append("a");
            builder.append(",");
            builder.append(words[Server2.i]);

            try {
                os.write(builder.toString().getBytes());
                builder.setLength(0);
                Thread.sleep(1000);
            } catch (IOException  | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}

