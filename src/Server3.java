import java.net.ServerSocket;
import java.net.Socket;

public class Server3 {
    private static String[] words = new String[]{"hello", "hi", "yun",
            "jeong", "kikat", "wow", "ming", "do", "kyung", "soo",
            "foxtail", "the", "lab", "gugugu", "smile", "picture"};

    public static int i;
    public static StringBuilder inputWord = new StringBuilder();


    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(1000);
        new Thread(() -> {
            while (true) {
                i = (i + 1) % words.length;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        while (true) {
            System.out.println("1");
            Socket socket = serverSocket.accept();
            System.out.println(socket.getRemoteSocketAddress());
            MessageCommunicator messageCommunicator = new MessageCommunicator(socket);
            messageCommunicator.connect();
            System.out.println("2");
        }
    }
}

//read return -1이면 연결이 끊긴다.

