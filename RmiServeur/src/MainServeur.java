import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServeur {

    private static final Logger LOGGER = Logger.getLogger(MainServeur.class.getName());

    public static void main(String[] args) {
        Connection conn = null;

        try {
            LOGGER.log(Level.INFO, "Lancement serveur");

            ConfigServer con = new ConfigServer();

            // Créer une connexion à la base de données
            conn = MyConnection.getConnection(ConfigServer.URL, ConfigServer.USERNAME, ConfigServer.PASSWORD);

            // Initialiser ChatImplementation avec la connexion
            ChatImplementation chat = new ChatImplementation(conn);

            String url = "rmi://localhost:9001/chat";
            LocateRegistry.createRegistry(9001);
            Naming.rebind(url, chat);
            LOGGER.log(Level.INFO, "Serveur en écoute sur " + url);
        } catch (RemoteException | MalformedURLException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du lancement du serveur", e);
        } finally {
            // Fermer la connexion à la base de données si elle est ouverte
            if (conn != null) {
                try {
                    conn.close();
                    LOGGER.log(Level.INFO, "Connexion à la base de données fermée");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion à la base de données", e);
                }
            }
        }
    }
}
