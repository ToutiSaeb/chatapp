import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatImplementation extends UnicastRemoteObject implements ChatRemote {

    private static final Logger LOGGER = Logger.getLogger(ChatImplementation.class.getName());
    private Connection conn;

    public ChatImplementation(Connection conn) throws RemoteException, SQLException {
        this.conn = MyConnection.getConnection(ConfigServer.URL, ConfigServer.USERNAME, ConfigServer.PASSWORD);
    }

    @Override
    public ArrayList<Message> Getallmsg() throws RemoteException {
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "SELECT pseudo, message, time FROM messages";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String pseudo = rs.getString("pseudo");
                String message = rs.getString("message");
                Timestamp time = rs.getTimestamp("time");
                messages.add(new Message(pseudo, message, new java.util.Date(time.getTime())));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving messages from DB", e);
            throw new RemoteException("Failed to retrieve messages from database", e);
        }
        return messages;
    }

    @Override
    public void Setallmsg(Message message) throws RemoteException {
        insertMessageToDB(message);
    }


    @Override
    public void deleteConversation() throws RemoteException {
        try {
            String sql = "DELETE FROM messages";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "All messages deleted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting messages from DB", e);
            throw new RemoteException("Failed to delete messages from database", e);
        }
    }

    private void insertMessageToDB(Message message) {
        String sql = "INSERT INTO messages (pseudo, message, time) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getPseudo());
            pstmt.setString(2, message.getMessage());
            pstmt.setTimestamp(3, new Timestamp(message.getTime().getTime()));
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Message inserted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting message to DB", e);
            throw new RuntimeException("Failed to insert message into database", e);
        }
    }
}
