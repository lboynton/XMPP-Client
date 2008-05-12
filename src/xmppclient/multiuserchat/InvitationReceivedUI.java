/*
 * InvitationReceivedUI.java
 *
 * Created on 12 May 2008, 13:31
 */
package xmppclient.multiuserchat;

import javax.swing.JOptionPane;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import xmppclient.multiuserchat.MUCChatUI;
import xmppclient.Utils;
import xmppclient.XMPPClientUI;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class InvitationReceivedUI extends javax.swing.JDialog implements InvitationListener
{
    private String room;
    private String password;
    private String inviter;

    /** Creates new form InvitationReceivedUI */
    public InvitationReceivedUI(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        acceptRadioButton = new javax.swing.JRadioButton();
        declineRadioButton = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        declineReasonTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        roomTextField = new javax.swing.JTextField();
        inviterTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        inviteReasonTextArea = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Conference Invitation");
        setResizable(false);

        buttonGroup.add(acceptRadioButton);
        acceptRadioButton.setSelected(true);
        acceptRadioButton.setText("Accept");
        acceptRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        acceptRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        acceptRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(declineRadioButton);
        declineRadioButton.setText("Decline");
        declineRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineRadioButtonActionPerformed(evt);
            }
        });

        declineReasonTextArea.setColumns(20);
        declineReasonTextArea.setEditable(false);
        declineReasonTextArea.setRows(5);
        jScrollPane1.setViewportView(declineReasonTextArea);

        jLabel1.setText("Reason");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Room");

        jLabel3.setText("Inviter");

        jLabel4.setText("Reason");

        roomTextField.setEditable(false);

        inviterTextField.setEditable(false);

        inviteReasonTextArea.setColumns(20);
        inviteReasonTextArea.setEditable(false);
        inviteReasonTextArea.setRows(5);
        jScrollPane2.setViewportView(inviteReasonTextArea);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Conference Invitation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roomTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inviterTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(261, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(acceptRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(declineRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {acceptRadioButton, declineRadioButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(roomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(inviterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptRadioButton)
                    .addComponent(declineRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    if(acceptRadioButton.isSelected())
    {
        System.out.println(room);
        MUCChatUI mucui = new MUCChatUI(StringUtils.parseName(room));
        try
        {
            System.out.println("Using nick: " + Utils.getNickname());
            mucui.join(Utils.getNickname());
        }
        catch (XMPPException ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        mucui.setVisible(true);
    }
    
    MultiUserChat.decline(XMPPClientUI.connection, room, inviter, declineReasonTextArea.getText());
    dispose();
}//GEN-LAST:event_okButtonActionPerformed

private void declineRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineRadioButtonActionPerformed
    declineReasonTextArea.setEditable(true);
    declineReasonTextArea.requestFocus();
}//GEN-LAST:event_declineRadioButtonActionPerformed

private void acceptRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptRadioButtonActionPerformed
    declineReasonTextArea.setEditable(false);
}//GEN-LAST:event_acceptRadioButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {
                InvitationReceivedUI dialog = new InvitationReceivedUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {

                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton acceptRadioButton;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JRadioButton declineRadioButton;
    private javax.swing.JTextArea declineReasonTextArea;
    private javax.swing.JTextArea inviteReasonTextArea;
    private javax.swing.JTextField inviterTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField roomTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void invitationReceived(XMPPConnection conn, String room, String inviter, String reason, String password, Message message)
    {
        this.room = room;
        this.password = password;
        this.inviter = inviter;
        
        inviterTextField.setText(inviter);
        roomTextField.setText(room);
        inviteReasonTextArea.setText(reason);
        
        setVisible(true);
    }
}
