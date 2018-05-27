import ex1.protobuf.Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MessageCommunicator {
    private Socket socket;
    private OnReceiver receiver;
    private String host;
    private int port;
    private InputStream is;
    private OutputStream os;

    public MessageCommunicator(Socket socket) {
        this.socket = socket;

    }

    public MessageCommunicator(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        if (socket == null) {
            this.socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int len;
                    byte[] bytes = new byte[128];


                    try {
                        while ((len = socket.getInputStream().read(bytes)) != -1) {
                            String s = new String(bytes, 0, len);
                            System.out.println(s);
                            receiver.onReceive(s);
                        }
                    } catch (Exception e) {
                        disConnect();
                        break;
                    }
                }
            }
        }).start();
    }

    private void removeCommunicator() {
        Server4.communicators.remove(this);
    }

    public void send(String word) throws IOException {
        Chat.Add add = new Chat.Add.Builder().setProto("a,").setWord(word).build();
//        StringBuilder builder = new StringBuilder();
//        builder.append("a," + word);
        String message = add.getProto() + add.getWord();
        System.out.println(message);
        socket.getOutputStream().write(message.getBytes());
    }

    public void removeSend(String word) throws IOException {
        socket.getOutputStream().write(word.toString().getBytes());

    }

    public interface OnReceiver {
        void onReceive(String word);
    }

    public void setOnReceiver(OnReceiver receiver) {
        this.receiver = receiver;
    }

    public void disConnect() {

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
