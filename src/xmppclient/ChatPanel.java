/*
 * ChatPanel.java
 *
 * Created on 14 April 2008, 23:30
 */

package xmppclient;

import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import xmppclient.formatter.FormatterUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.VCard;
import xmppclient.emoticons.Emoticon;
import xmppclient.emoticons.Emoticons;
import xmppclient.formatter.Format;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class ChatPanel extends javax.swing.JPanel 
{
    private Chat chat;
    private JFrame frame;
    private Format format = new Format();
    
    public ChatPanel(Chat chat, JFrame frame)
    {
        this.frame = frame;
        this.chat = chat;
        initComponents();
        initTextPane();
        if(!XMPPClientUI.connection.getRoster().getPresence(chat.getParticipant()).isAvailable()) sendFileButton.setEnabled(false);
    }
    
    public Chat getChat()
    {
        return chat;
    }
    
    @Override
    public String getName()
    {
        VCard vCard = new VCard();
        
        if(chat == null) return "Unnamed";
        
        if(getRosterEntry() != null && getRosterEntry().getName() != null && !getRosterEntry().getName().equals(""))
            return getRosterEntry().getName();
        
        try
        {
            vCard.load(XMPPClientUI.connection, chat.getParticipant());
            if(vCard.getNickName() != null) return vCard.getNickName();
        }
        catch (XMPPException ex) {}
        
        return chat.getParticipant();
    }
    
    private void initTextPane()
    {
        StyledDocument doc = messageTextPane.getStyledDocument();
        // create the default style
        Style def = doc.addStyle("default", null);
        
        // create the nickname style
        Style nickname = doc.addStyle("nickname", def);
        StyleConstants.setBold(nickname, true);
        
        // create the icon style
        Style icon = doc.addStyle("icon", def);
        StyleConstants.setAlignment(icon, StyleConstants.ALIGN_CENTER);
        

        messageTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    public void addMessage(Icon avatar, String name, Message message)
    {
        try
        {
            StyledDocument doc = messageTextPane.getStyledDocument();
            
            Format newFormat = (Format) message.getProperty("format");
            
            Style newStyle = doc.addStyle("newStyle", null);
            
            if(newFormat != null)
            {
                StyleConstants.setFontFamily(newStyle, newFormat.getFont().getFamily());
                StyleConstants.setFontSize(newStyle, newFormat.getFont().getSize());
                StyleConstants.setForeground(newStyle, newFormat.getColor());
            }
            
            Style icon = doc.getStyle("icon");
            if(avatar != null) StyleConstants.setIcon(icon, Utils.resizeImage((ImageIcon) avatar, 30));

            doc.insertString(doc.getLength(), name + ": ", doc.getStyle("nickname"));
            int start = doc.getLength();
            doc.insertString(doc.getLength(), message.getBody(), doc.getStyle("newStyle"));
            int end = doc.getLength();
            
            SimpleAttributeSet smi;
            
            for(int i = start; i < end; i++)
            {
                for(Emoticon e: Emoticons.getEmoticons())
                {
                    if((i + e.getSequence().length()) > end) continue;
                    String newString = doc.getText(i, e.getSequence().length());
                
                    if(newString.equals(e.getSequence()))
                    {
                        System.out.println("Emoticon");
                        doc.remove(i, e.getSequence().length());
                        smi=new SimpleAttributeSet();
                        StyleConstants.setIcon(smi, e.getIcon());
                        doc.insertString(i, e.getSequence(), smi);

                        // can't seem to have two icons next to each other
                        // therefore a space will be added after the icon
                        doc.insertString(i+e.getSequence().length(), " ", null);
                        i+=e.getSequence().length() + 1;

                        // increment the end of the document to take into account
                        // the space that was added
                        end++;
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
        return XMPPClientUI.connection.getRoster().getEntry(chat.getParticipant());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contact = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sendFileButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        formatButton = new javax.swing.JButton();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextPane = new javax.swing.JTextPane();
        sendScrollPane = new javax.swing.JScrollPane();
        sendTextPane = new javax.swing.JTextPane();

        contact.setFont(new java.awt.Font("Tahoma", 1, 12));
        contact.setText(chat.getParticipant());

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("To:");

        sendFileButton.setText("Send File");
        sendFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 10));
        statusLabel.setText("(" + Utils.getStatus(XMPPClientUI.connection.getRoster().getPresence(chat.getParticipant())) + ")");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/emoticons/face-smile.png"))); // NOI18N

        formatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/format-text-bold-16x16.png"))); // NOI18N
        formatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatButtonActionPerformed(evt);
            }
        });

        messageTextPane.setEditable(false);
        messageScrollPane.setViewportView(messageTextPane);

        sendTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sendTextPaneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sendTextPaneKeyReleased(evt);
            }
        });
        sendScrollPane.setViewportView(sendTextPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(formatButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                        .addComponent(sendFileButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendFileButton)
                    .addComponent(jLabel1)
                    .addComponent(contact)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(formatButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1)))
                    .addComponent(sendScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    send();
}//GEN-LAST:event_sendButtonActionPerformed
    private void send()
    {
        if(sendTextPane.getText().trim().equals("")) return;
        
        try
        {
            Message message = new Message();
            message.setBody(sendTextPane.getText().trim());
            message.setProperty("format", format);
            chat.sendMessage(message);
            addMessage(Utils.getAvatar(50), "Me", message);
            sendTextPane.setText("");
        }
        catch (XMPPException ex)
        {
            Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
private void sendFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileButtonActionPerformed

    final JFileChooser fileChooser = new JFileChooser();
    fileChooser.setAccessory(new FileTransferChooserAccessory());
    fileChooser.addPropertyChangeListener(new PropertyChangeListener() 
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
            {
                JFileChooser chooser = (JFileChooser)evt.getSource();
                try
                {
                    ((FileTransferChooserAccessory)fileChooser.getAccessory()).setFilename(chooser.getSelectedFile().getName());  
                }
                catch(Exception e) {}
            }
        }
    });
    
    int selection = fileChooser.showDialog(frame, "Send file to " + getName());
    
    if(selection == JFileChooser.APPROVE_OPTION)
    {
        FileTransferManager manager = new FileTransferManager(XMPPClientUI.connection);
        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(XMPPClientUI.connection.getRoster().getPresence(getRosterEntry().getUser()).getFrom());
        
        try
        {
            transfer.sendFile(fileChooser.getSelectedFile(), ((FileTransferChooserAccessory)fileChooser.getAccessory()).getFileDescription());
            new FileTransferUI(transfer);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileTransferChooser.class.getName()).log(Level.SEVERE, null, ex);
        }        
        catch (XMPPException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileTransferChooser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_sendFileButtonActionPerformed

private void formatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatButtonActionPerformed
    FormatterUI formatter = new FormatterUI(null, format.getFont(), format.getColor());
    format = formatter.showDialog();
    sendTextPane.setFont(format.getFont());
    sendTextPane.setForeground(format.getColor());
}//GEN-LAST:event_formatButtonActionPerformed

private void sendTextPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextPaneKeyPressed
    if(evt.getKeyCode() == 10) send();
}//GEN-LAST:event_sendTextPaneKeyPressed

private void sendTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextPaneKeyReleased
    if(evt.getKeyCode() == 10) sendTextPane.setText("");
}//GEN-LAST:event_sendTextPaneKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contact;
    private javax.swing.JButton formatButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextPane messageTextPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton sendFileButton;
    private javax.swing.JScrollPane sendScrollPane;
    private javax.swing.JTextPane sendTextPane;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
