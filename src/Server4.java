import ex1.protobuf.Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server4 {
    private static String[] words;
    public static List<MessageCommunicator> communicators = new ArrayList<>();
    private static List<MessageCommunicator> preSockets = new ArrayList<>();
    private static boolean isBreak;
    private static int i;

    public static void main(String[] args) throws Exception {
        String s = TextFileManager.readFile("./data/Text.txt");
        words = s.split("\n");
        new Thread(() -> {
            System.out.print("start");
            while (true) {
                addList();
                for (MessageCommunicator communicator : communicators) {
                    System.out.print("Add");
                    try {
                        communicator.send(words[i]);
                        Thread.sleep(1000);
                    } catch (IOException e) {
//                        communicators.remove(communicator);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    System.out.println(1);
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("dlfkjsdlfkjs");
                i = (i + 1) % words.length;
                if (isBreak)
                    break;
            }

        }).start();


        ServerSocket serverSocket = new ServerSocket(5000);
        while (true) {
            Socket socket = serverSocket.accept();
            MessageCommunicator communicator = new MessageCommunicator(socket);
            System.out.println(socket.getRemoteSocketAddress());
            System.out.println("add");
            addPreList(communicator);
            MessageCommunicator messageCommunicator = new MessageCommunicator(socket);
            messageCommunicator.setOnReceiver((String word) -> {
                Chat.Remove remove = new Chat.Remove.Builder().setProto("r,").setWord(word).build();
//                                inputWord.append("r");
//                                inputWord.append(",");
//                                inputWord.append(word);
                String message = remove.getProto() + remove.getWord();
                broadCastWord(message);
//                                inputWord.setLength(0);

            });
            messageCommunicator.connect();
        }
    }

    private static void addPreList(MessageCommunicator communicator) {
        preSockets.add(communicator);
    }

    private static void addList() {
        communicators.addAll(preSockets);
        preSockets.clear();
    }

    private static void broadCastWord(String word) {
        System.out.println("word : " + word);
        for (MessageCommunicator communicator : communicators) {
            try {
                communicator.removeSend(word);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}