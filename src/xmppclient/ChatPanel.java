/*
 * ChatPanel.java
 *
 * Created on 14 April 2008, 23:30
 */

package xmppclient;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import xmppclient.formatter.FormatterUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.VCard;
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
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        doc.addStyle("default", def);
        doc.addStyle("nickname", def);
        doc.addStyle("icon", def);
        messageTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    public void addMessage(Icon avatar, String name, Message message)
    {
        try
        {
            StyledDocument doc = messageTextPane.getStyledDocument();
            Format newFormat = (Format) message.getProperty("format");
            
            Style newStyle = doc.addStyle("newStyle", null);
            StyleConstants.setFontFamily(newStyle, newFormat.getFont().getFamily());
            StyleConstants.setFontSize(newStyle, newFormat.getFont().getSize());
            StyleConstants.setForeground(newStyle, newFormat.getColor());
            
            Style icon = doc.getStyle("icon");
            if(avatar != null) StyleConstants.setIcon(icon, Utils.resizeImage((ImageIcon) avatar, 40));
            
            doc.insertString(doc.getLength(), "", icon);
            doc.insertString(doc.getLength(), name + ": ", doc.getStyle("default"));
            doc.insertString(doc.getLength(), message.getBody(), doc.getStyle("newStyle"));
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
        sendScrollPane = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sendFileButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        formatButton = new javax.swing.JButton();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextPane = new javax.swing.JTextPane();

        contact.setFont(new java.awt.Font("Tahoma", 1, 12));
        contact.setText(chat.getParticipant());

        sendTextArea.setColumns(20);
        sendTextArea.setFont(new java.awt.Font("Tahoma", 0, 10));
        sendTextArea.setLineWrap(true);
        sendTextArea.setRows(1);
        sendTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sendTextAreaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sendTextAreaKeyReleased(evt);
            }
        });
        sendScrollPane.setViewportView(sendTextArea);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sendScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(formatButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    send();
}//GEN-LAST:event_sendButtonActionPerformed
    private void send()
    {
        if(sendTextArea.getText().trim().equals("")) return;
        
        try
        {
            Message message = new Message();
            message.setBody(sendTextArea.getText().trim());
            message.setProperty("format", format);
            chat.sendMessage(message);
            addMessage(Utils.getAvatar(50), "Me", message);
            sendTextArea.setText("");
        }
        catch (XMPPException ex)
        {
            Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextAreaKeyPressed
       if(evt.getKeyCode() == 10) send();
    }//GEN-LAST:event_sendTextAreaKeyPressed

    private void sendTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextAreaKeyReleased
        if(evt.getKeyCode() == 10) sendTextArea.setText("");
    }//GEN-LAST:event_sendTextAreaKeyReleased

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
    FormatterUI formatter = new FormatterUI(null);
    format = formatter.showDialog();
    sendTextArea.setFont(format.getFont());
    sendTextArea.setForeground(format.getColor());
}//GEN-LAST:event_formatButtonActionPerformed
    
    
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
    private javax.swing.JTextArea sendTextArea;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    private class MessageListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, 
                Object object, 
                int index, 
                boolean isSelected, 
                boolean cellHasFocus)
        {       
            ListMessage message = (ListMessage) object;
            
            return new ListMessageRenderer(message);
        }
    }
        
}
