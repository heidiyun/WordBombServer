import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Sender{
    InputStream sis;
    OutputStream sos;

    private String host;
    private int port;

    public Sender(String host, int port){
        this.host = host;
        this.port = port;
    }

    private Receiver receiver = new Receiver() {
        @Override
        public void OnReceiver(String message) {

        }
    };

    interface Receiver{
        void OnReceiver(String message);
    }

    public void send(String message){
        try {
            System.out.println("Send Message : " + message);
            sos.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            Socket socket = new Socket(host, port);
            sos = socket.getOutputStream();
            sis = socket.getInputStream();

            new Thread(()->{
                byte[] buf = new byte[128];
                int len;

                try {
                    while ((len = sis.read(buf)) != -1) {

                        receiver.OnReceiver(new String(buf, 0, len));
                        System.out.println("Receive : " + new String(buf));

                    }
                    sis.close();
                    sos.close();
                } catch(Exception e){
                    try {
                        sis.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        sos.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
    }



}