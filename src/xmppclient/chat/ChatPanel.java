/*
 * ChatPanel.java
 *
 * Created on 14 April 2008, 23:30
 */
package xmppclient.chat;

import xmppclient.*;
import java.awt.Cursor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import xmppclient.formatter.FormatterUI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.VCard;
import xmppclient.emoticons.Emoticon;
import xmppclient.emoticons.Emoticons;
import xmppclient.formatter.Format;

/**
 * A JPanel which is displayed in the tabs in the chat window. Represents a 
 * one-to-one chat.
 * @author  Lee Boynton (323326)
 */
public class ChatPanel extends javax.swing.JPanel
{
    private Chat chat;
    private JFrame parent;
    private Format format = new Format();

    /**
     * Creates a new JPanel for conducting the chat
     * @param chat The chat this panel should be associated with
     * @param frame The chat JFrame
     */
    public ChatPanel(Chat chat, JFrame parent)
    {
        this.parent = parent;
        this.chat = chat;
        initComponents();
        initTextPane();
        if (!ContactListUI.connection.getRoster().getPresence(chat.getParticipant()).isAvailable())
        {
            sendFileButton.setEnabled(false);
        }
    }

    /**
     * Gets the chat instance associated with this chat panel
     * @return The chat
     */
    public Chat getChat()
    {
        return chat;
    }

    /**
     * Gets the name of the user. If the local user has given the contact a name
     * then that name is used. If not, the contact's nickname is returned. If there
     * is no nickname then the user's JID is returned.
     * @return The name of the user
     */
    @Override
    public String getName()
    {
        VCard vCard = new VCard();

        if (chat == null)
        {
            return "Unnamed";
        }
        if (getRosterEntry() != null && getRosterEntry().getName() != null && !getRosterEntry().getName().equals(""))
        {
            return getRosterEntry().getName();
        }
        try
        {
            vCard.load(ContactListUI.connection, chat.getParticipant());
            if (vCard.getNickName() != null)
            {
                return vCard.getNickName();
            }
        }
        catch (XMPPException ex)
        {
        }

        return chat.getParticipant();
    }

    /**
     * Creates some initial text styles
     * Sets the cursor of the text pane
     */
    private void initTextPane()
    {
        messageTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    /**
     * Adds a message to the messages text pane. Where available is uses the formatting
     * specified in the message.
     * @param avatar The avatar of the user
     * @param name The name of the user who sent the message
     * @param message The message sent
     */
    public void addMessage(Icon avatar, String name, Message message)
    {
        try
        {
            ChatTextPaneStyledDocument doc = (ChatTextPaneStyledDocument) messageTextPane.getStyledDocument();

            // get the formatted text
            Format newFormat = (Format) message.getProperty("format");

            // create a new style for the formatted text
            Style newStyle = doc.addStyle("newStyle", null);

            // the format property will only be set by this client
            if (newFormat != null)
            {
                StyleConstants.setFontFamily(newStyle, newFormat.getFont().getFamily());
                StyleConstants.setFontSize(newStyle, newFormat.getFont().getSize());
                StyleConstants.setForeground(newStyle, newFormat.getColor());
                StyleConstants.setItalic(newStyle, newFormat.getFont().isItalic());
                StyleConstants.setBold(newStyle, newFormat.getFont().isBold());
            }

            doc.insertUser(name);
            int start = doc.getLength();
            doc.insertString(doc.getLength(), message.getBody(), doc.getStyle("newStyle"));

            List<Emoticon> emoticons = Emoticons.getEmoticons();

            if (message.getProperty("emoticons") != null &&
                    message.getProperty("emoticons") instanceof List)
            {       
                List tempList = (List) message.getProperty("emoticons");
                for(Object o:tempList)
                {
                    if(o instanceof Emoticon) emoticons.add((Emoticon) o);
                }
            }
            SimpleAttributeSet smi;

            for (int i = start; i < doc.getLength(); i++)
            {
                for (Emoticon e : Emoticons.getEmoticons())
                {
                    if ((i + e.getSequence().length()) > doc.getLength())
                    {
                        continue;
                    }
                    String newString = doc.getText(i, e.getSequence().length());

                    if (newString.equals(e.getSequence()))
                    {
                        doc.remove(i, e.getSequence().length());
                        smi = new SimpleAttributeSet();
                        StyleConstants.setIcon(smi, e.getIcon());
                        doc.insertString(i, e.getSequence(), smi);

                        i += e.getSequence().length() - 1;
                        break;
                    }
                }
            }

            doc.insertString(doc.getLength(), "\n", doc.getStyle("default"));

            messageTextPane.setCaretPosition(doc.getLength());
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveChat()
    {
        ContactListUI.accountManager.logConversation(messageTextPane, chat.getParticipant());
    }

    /**
     * Gets the roster entry of the chat participant from the contact list.
     * @return The roster entry, or null if not in roster
     */
    private RosterEntry getRosterEntry()
    {
        /*
         * NOTE:
         * chat.getParticipant() returns the JID with the resource
         * The roster JID does not have the resource
         */
        return ContactListUI.connection.getRoster().getEntry(chat.getParticipant());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contactLabel = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        toLabel = new javax.swing.JLabel();
        sendFileButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        emoticonsButton = new javax.swing.JButton();
        formatButton = new javax.swing.JButton();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextPane = new javax.swing.JTextPane();
        sendScrollPane = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();

        contactLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        contactLabel.setText(chat.getParticipant());

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        toLabel.setText("To:");

        sendFileButton.setText("Send File");
        sendFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 10));
        statusLabel.setText("(" + Utils.getStatus(ContactListUI.connection.getRoster().getPresence(chat.getParticipant())) + ")");

        emoticonsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/emoticons/face-smile.png"))); // NOI18N
        emoticonsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emoticonsButtonActionPerformed(evt);
            }
        });

        formatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/format-text-bold-16x16.png"))); // NOI18N
        formatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatButtonActionPerformed(evt);
            }
        });

        messageScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        messageTextPane.setEditable(false);
        messageTextPane.setStyledDocument(new ChatTextPaneStyledDocument());
        messageScrollPane.setViewportView(messageTextPane);

        sendTextArea.setColumns(20);
        sendTextArea.setFont(sendTextArea.getFont());
        sendTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sendTextAreaKeyReleased(evt);
            }
        });
        sendScrollPane.setViewportView(sendTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(formatButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(emoticonsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(toLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contactLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                        .addComponent(sendFileButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendFileButton)
                    .addComponent(toLabel)
                    .addComponent(contactLabel)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendScrollPane, 0, 0, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(formatButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emoticonsButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        send();
}//GEN-LAST:event_sendButtonActionPerformed
    private void send()
    {
        if (sendTextArea.getText().trim().equals(""))
        {
            return;
        }
        try
        {
            Message message = new Message();
            message.setBody(sendTextArea.getText().trim());
            message.setProperty("format", format);
            message.setProperty("emoticons", getCustomEmoticons());
            chat.sendMessage(message);
            addMessage(Utils.getAvatar(50), "Me", message);
            sendTextArea.setText("");
        }
        catch (XMPPException ex)
        {
            Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Emoticon> getCustomEmoticons()
    {
        List<Emoticon> emoticons = new ArrayList<Emoticon>();

        try
        {
            for (int i = 0; i < sendTextArea.getText().length(); i++)
            {
                for (Emoticon e : Emoticons.getEmoticons())
                {
                    if ((i + e.getSequence().length()) > sendTextArea.getText().length())
                    {
                        continue;
                    }
                    String newString = sendTextArea.getText(i, e.getSequence().length());

                    if (newString.equals(e.getSequence()))
                    {
                        if (!emoticons.contains(e))
                        {
                            emoticons.add(e);
                        }
                    }
                }
            }
        }
        catch (BadLocationException ex)
        {
            ex.printStackTrace();
        }

        return emoticons;
    }

private void sendFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileButtonActionPerformed
    new FileTransferChooser(parent, true, getRosterEntry()).setVisible(true);
}//GEN-LAST:event_sendFileButtonActionPerformed

private void formatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatButtonActionPerformed
    FormatterUI formatter = new FormatterUI(null, format.getFont(), format.getColor());
    format = formatter.showDialog();
    sendTextArea.setFont(format.getFont());
    sendTextArea.setForeground(format.getColor());
}//GEN-LAST:event_formatButtonActionPerformed

private void emoticonsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emoticonsButtonActionPerformed
    Point point = emoticonsButton.getMousePosition();
    SwingUtilities.convertPointToScreen(point, emoticonsButton);
    new EmoticonsUI(null, sendTextArea, point).setVisible(true);
}//GEN-LAST:event_emoticonsButtonActionPerformed

private void sendTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextAreaKeyReleased
    if (evt.getKeyCode() == 10 && evt.getModifiersEx() == 64)
    {
        sendTextArea.append("\n");
        return;
    }
    if (evt.getKeyCode() == 10)
    {
        send();
    }
}//GEN-LAST:event_sendTextAreaKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contactLabel;
    private javax.swing.JButton emoticonsButton;
    private javax.swing.JButton formatButton;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextPane messageTextPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton sendFileButton;
    private javax.swing.JScrollPane sendScrollPane;
    private javax.swing.JTextArea sendTextArea;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables
}
