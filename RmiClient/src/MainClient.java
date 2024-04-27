import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

public class MainClient {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        System.out.println("lancement client");
        String url = "rmi://127.0.0.1:9001/chat";
        ChatRemote cr = (ChatRemote) Naming.lookup(url);
        Date d = new Date();
        Message m = new Message("saib","hello",d);

        System.out.println(cr.Getallmsg());
        cr.Setallmsg(m);
        System.out.println(cr.Getallmsg());



    }
}
