package server;

public class ServerUI {
    
    final public static int DEFAULT_PORT = 5555;

    public static void main(String[] args) {
        EchoServer sv = new EchoServer(DEFAULT_PORT);
        try {
            sv.listen(); 
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}