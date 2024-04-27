import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatClientUI extends JFrame {

    private JFrame frame;
    private JTextArea chatArea1;
    private JTextField messageField1;
    private JButton sendButton1;
    private JTextArea chatArea2;
    private JTextField messageField2;
    private JButton sendButton2;
    private JButton deleteConversationButton;
    private ChatRemote chatRemote;
    private String pseudo1;
    private String pseudo2;
    public  JPanel messagePanel;
    public ChatClientUI(String pseudo1, String pseudo2) {
        this.pseudo1 = pseudo1;
        this.pseudo2 = pseudo2;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Chat App");
        frame.setSize(800, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("ChatApp", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.RED);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Client 1
        JPanel client1Panel = new JPanel();
        client1Panel.setLayout(new BorderLayout());
        JLabel labelClient1 = new JLabel("Client 1: " + pseudo1);
        labelClient1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        client1Panel.add(labelClient1, BorderLayout.NORTH);
        chatArea1 = new JTextArea();
        chatArea1.setPreferredSize(new Dimension(100, 380));
        chatArea1.setEditable(false);
        chatArea1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        JScrollPane scrollPane1 = new JScrollPane(chatArea1);
        client1Panel.add(scrollPane1, BorderLayout.CENTER);
        JPanel messagePanel1 = new JPanel(new BorderLayout());
        messageField1 = new JTextField();
        messageField1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        messagePanel1.add(messageField1, BorderLayout.CENTER);
        sendButton1 = new JButton("Send");
        sendButton1.addActionListener(e -> sendMessage(pseudo1, messageField1));
        sendButton1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        messagePanel1.add(sendButton1, BorderLayout.EAST);
        client1Panel.add(messagePanel1, BorderLayout.SOUTH);
        topPanel.add(client1Panel);

        // Client 2
        JPanel client2Panel = new JPanel();
        client2Panel.setLayout(new BorderLayout());
        JLabel labelClient2 = new JLabel("Client 2: " + pseudo2);
        labelClient2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        client2Panel.add(labelClient2, BorderLayout.NORTH);
        chatArea2 = new JTextArea();
        chatArea2.setPreferredSize(new Dimension(100, 380));
        chatArea2.setEditable(false);
        chatArea2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        JScrollPane scrollPane2 = new JScrollPane(chatArea2);
        client2Panel.add(scrollPane2, BorderLayout.CENTER);
        JPanel messagePanel2 = new JPanel(new BorderLayout());
        messageField2 = new JTextField();
        messageField2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        messagePanel2.add(messageField2, BorderLayout.CENTER);
        sendButton2 = new JButton("Send");
        sendButton2.addActionListener(e -> sendMessage(pseudo2, messageField2));
        sendButton2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        messagePanel2.add(sendButton2, BorderLayout.EAST);
        client2Panel.add(messagePanel2, BorderLayout.SOUTH);
        topPanel.add(client2Panel);

        frame.add(topPanel, BorderLayout.NORTH);

        deleteConversationButton = new JButton("Delete Conversation");
        deleteConversationButton.addActionListener(e -> deleteConversation());
        deleteConversationButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        frame.add(deleteConversationButton, BorderLayout.SOUTH);

        // Fond coloré pour les zones de chat
        chatArea1.setBackground(new Color(0x33C7FF)); // Fond bleu pour chatArea1
        sendButton1.setBackground(new Color(0x33C7FF)); // Fond bleu pour le bouton sendButton1
        // Fond bleu pour le label labelClient1

        chatArea2.setBackground(new Color(0x33FF5E)); // Fond vert pour chatArea2
        sendButton2.setBackground(new Color(0x33FF5E)); // Fond vert pour le bouton sendButton2
      // Fond vert pour le label labelClient2

        // Fond rouge pour chatArea2

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMessage(String pseudo, JTextField messageField) {
        try {
            if (chatRemote != null) {
                String messageText = messageField.getText();
                Date time = new Date();
                Message message = new Message(pseudo, messageText, time);
                chatRemote.Setallmsg(message);
                messageField.setText("");

                // Mettre à jour la zone de chat respective pour afficher le message envoyé
                ArrayList<Message> allMessages = chatRemote.Getallmsg();
                if (pseudo.equals(pseudo1)) {
                    updateChatArea(allMessages, true);
                } else if (pseudo.equals(pseudo2)) {
                    updateChatArea(allMessages, false);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void deleteConversation() {
        try {
            if (chatRemote != null) {
                chatRemote.deleteConversation();

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateChatArea(ArrayList<Message> messages, boolean isClient1) {
        JTextArea chatAreaToUpdate = isClient1 ? chatArea1 : chatArea2;
        chatAreaToUpdate.setText("");

        for (Message message : messages) {
            // Formatage de la date pour afficher uniquement l'heure, les minutes et les secondes
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeFormatted = sdf.format(message.getTime());

            // Ajout du pseudo, du message et du temps à la zone de chat
            chatAreaToUpdate.append("\t\t"+timeFormatted +"\n"+message.getPseudo() + ": " + message.getMessage() +"\n");
        }


    }



    public void setChatRemote(ChatRemote chatRemote) {
        this.chatRemote = chatRemote;
    }

    public static void main(String[] args) {
        try {
            String url = "rmi://127.0.0.1:9001/chat";
            ChatRemote cr = (ChatRemote) Naming.lookup(url);

            // Custom dialog for entering pseudonyms
            JPanel panel = new JPanel(new BorderLayout(10000, 20));

            JLabel titleLabel = new JLabel("Enter Pseudonyms", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            panel.add(titleLabel, BorderLayout.NORTH);

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 30, 20));
            JLabel label1 = new JLabel("Client 1:", SwingConstants.RIGHT);
            JTextField pseudo1Field = new JTextField();
            JLabel label2 = new JLabel("Client 2:", SwingConstants.RIGHT);
            JTextField pseudo2Field = new JTextField();

            label1.setFont(new Font("Arial", Font.PLAIN, 18));
            pseudo1Field.setFont(new Font("Arial", Font.PLAIN, 18));
            label2.setFont(new Font("Arial", Font.PLAIN, 18));
            pseudo2Field.setFont(new Font("Arial", Font.PLAIN, 18));

            inputPanel.add(label1);
            inputPanel.add(pseudo1Field);
            inputPanel.add(label2);
            inputPanel.add(pseudo2Field);

            panel.add(inputPanel, BorderLayout.CENTER);

            int option = JOptionPane.showConfirmDialog(null, panel, "Enter Pseudonyms",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String pseudo1 = pseudo1Field.getText();
                String pseudo2 = pseudo2Field.getText();
                ChatClientUI clientUI = new ChatClientUI(pseudo1, pseudo2);
                clientUI.setChatRemote(cr);

                clientUI.updateChatArea(cr.Getallmsg(), true);
                clientUI.updateChatArea(cr.Getallmsg(), false);

                Thread updateThread = new Thread(() -> {
                    while (true) {
                        try {
                            ArrayList<Message> messages1 = cr.Getallmsg();
                            clientUI.updateChatArea(messages1, true);
                            ArrayList<Message> messages2 = cr.Getallmsg();
                            clientUI.updateChatArea(messages2, false);
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                updateThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
