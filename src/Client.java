public class Client{

    public static void main(String[] args){

        Sender s = new Sender("localhost",5000);

        s.connect();
        s.send("WT??");
        s.setReceiver((String message)->{
            System.out.println("Receive Message in Main !!");
        });

        new Thread(()->{for(;;){}}).start();
    }

}

