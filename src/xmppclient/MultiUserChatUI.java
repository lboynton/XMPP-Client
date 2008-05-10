/*
 * MultiUserChatUI.java
 *
 * Created on 09 May 2008, 15:22
 */
package xmppclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class MultiUserChatUI extends javax.swing.JFrame implements PacketListener
{
    private MultiUserChat muc;
    
    /** Creates new form MultiUserChatUI */
    public MultiUserChatUI(String room)
    {
        muc = new MultiUserChat(XMPPClientUI.connection, room);  
        try
        {
            muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
        }
        catch (XMPPException ex)
        {
            Logger.getLogger(MultiUserChatUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        muc.addMessageListener(this);
        initComponents();
    }
    
    public void create(String nickname) throws XMPPException
    {
        muc.create(nickname);
    }
    
    public void join(String nickname) throws XMPPException
    {
        muc.join(nickname);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);
        jScrollPane1.setViewportView(messageTextArea);

        sendTextArea.setColumns(20);
        sendTextArea.setRows(5);
        jScrollPane2.setViewportView(sendTextArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sendButton)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    try
    {//GEN-LAST:event_sendButtonActionPerformed
        muc.sendMessage(sendTextArea.getText());
    }
    catch (XMPPException ex)
    {
        Logger.getLogger(MultiUserChatUI.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @Override
    public
    void processPacket(Packet packet)
    {
        Message message = (Message) packet;
        messageTextArea.append(message.getFrom() + ": " + message.getBody() + "\n");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea sendTextArea;
    // End of variables declaration//GEN-END:variables
}