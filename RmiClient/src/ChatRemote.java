import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatRemote extends Remote {
    public ArrayList<Message> Getallmsg() throws RemoteException;
    public void Setallmsg(Message c) throws RemoteException;
    public void deleteConversation() throws RemoteException; // Méthode pour supprimer la conversation
}
