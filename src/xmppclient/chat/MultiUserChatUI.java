/*
 * MultiUserChatUI.java
 *
 * Created on 09 May 2008, 15:22
 */
package xmppclient.chat;

import xmppclient.*;
import xmppclient.chat.MultiUserChatInviteUI;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.muc.UserStatusListener;
import xmppclient.formatter.Format;
import xmppclient.formatter.FormatterUI;

/**
 * A JFrame for conducting conferences in
 * @author  Lee Boynton (323326)
 */
public class MultiUserChatUI extends javax.swing.JFrame implements PacketListener
{
    private MultiUserChat muc;
    private ChatTextPaneStyledDocument doc;
    private Format format = new Format();

    /**
     * Creates a new JFrame for conducting a conference in.
     * @param room The name of the conference to create/join
     */
    public MultiUserChatUI(String room)
    {
        muc = new MultiUserChat(ContactListUI.connection, room + "@conference.192.168.0.8");
        initComponents();
        doc = (ChatTextPaneStyledDocument) messageTextPane.getStyledDocument();
    }

    /**
     * Creates a conference and joins the user with the given nickname
     * @param nickname The nickname to use in the conference
     * @throws org.jivesoftware.smack.XMPPException If the room cannot be created
     * 
     * NOTE: The nickname may be in use
     * 
     */
    public void create(String nickname) throws XMPPException
    {
        muc.create(nickname);
        muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
        initialise();
    }

    /**
     * Joins the conference using the nickname
     * @param nickname The nickname to use in the conference
     * @throws org.jivesoftware.smack.XMPPException If the room cannot be joined
     * 
     * NOTE: The nickname may be in use
     * 
     */
    public void join(String nickname) throws XMPPException
    {
        muc.join(nickname);
        initialise();
    }

    /**
     * When the JFrame is made visible this sets the split pane dividers to
     * the default position
     * @param b If the JFrame should be made visible or not
     */
    @Override
    public void setVisible(boolean b)
    {
        super.setVisible(b);
        pack();
        horizontalSplitPane.setDividerLocation(0.82);
        verticalSplitPane.setDividerLocation(0.75);
    }

    private void initialise()
    {
        updateOccupantList();
        muc.addMessageListener(this);
        muc.addParticipantListener(new PacketListener()
        {
            @Override
            public void processPacket(Packet packet)
            {
                updateOccupantList();
            }
        });
        muc.addUserStatusListener(new UserStatusListener()
        {
            @Override
            public void kicked(String actor, String reason)
            {
                doc.insertInfo(actor + " was kicked for: " + reason);
            }

            @Override
            public void voiceGranted()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void voiceRevoked()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void banned(String actor, String reason)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void membershipGranted()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void membershipRevoked()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void moderatorGranted()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void moderatorRevoked()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void ownershipGranted()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void ownershipRevoked()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void adminGranted()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void adminRevoked()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        muc.addParticipantStatusListener(new ParticipantStatusListener()
        {
            @Override
            public void joined(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " joined");
                updateOccupantList();
            }

            @Override
            public void left(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " left");
                updateOccupantList();
            }

            @Override
            public void kicked(String participant, String actor, String reason)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was kicked out by " + actor + "for: " + reason);
                updateOccupantList();
            }

            @Override
            public void voiceGranted(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was granted voice");
            }

            @Override
            public void voiceRevoked(String participant)
            {
                doc.insertInfo("Voice was revoked from " + StringUtils.parseResource(participant));
            }

            @Override
            public void banned(String participant, String actor, String reason)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was banned by " + actor + " for: " + reason);
            }

            @Override
            public void membershipGranted(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was granted membership");
            }

            @Override
            public void membershipRevoked(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " has had their membership revoked");
            }

            @Override
            public void moderatorGranted(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was granted moderator role");
            }

            @Override
            public void moderatorRevoked(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " had their moderator role revoked");
            }

            @Override
            public void ownershipGranted(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " was granted ownership of the conference");
            }

            @Override
            public void ownershipRevoked(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " is no longer owner");
            }

            @Override
            public void adminGranted(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " is now admin");
            }

            @Override
            public void adminRevoked(String participant)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " is no longer admin");
            }

            @Override
            public void nicknameChanged(String participant, String newNickname)
            {
                doc.insertInfo(StringUtils.parseResource(participant) + " changed their nickname to " + newNickname);
            }
        });
        muc.addInvitationRejectionListener(new InvitationRejectionListener()
        {
            @Override
            public void invitationDeclined(String invitee, String reason)
            {
                JOptionPane.showMessageDialog(MultiUserChatUI.this,
                        invitee + " rejected your invitation.\nReason: " + reason,
                        "Invitation rejected",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        muc.addSubjectUpdatedListener(new SubjectUpdatedListener()
        {
            @Override
            public void subjectUpdated(String subject, String from)
            {
                ChatTextPaneStyledDocument doc = (ChatTextPaneStyledDocument) messageTextPane.getStyledDocument();
                doc.insertInfo(StringUtils.parseResource(from) + 
                        " changed the subject to: " +
                        subject + ".");
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        inviteButton = new javax.swing.JButton();
        subjectButton = new javax.swing.JButton();
        banButton = new javax.swing.JButton();
        kickButton = new javax.swing.JButton();
        verticalSplitPane = new javax.swing.JSplitPane();
        horizontalSplitPane = new javax.swing.JSplitPane();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextPane = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        memberList = new javax.swing.JList()
        {
            public String getToolTipText(MouseEvent evt)
            {
                // Get item index
                int index = locationToIndex(evt.getPoint());

                // Get item
                String user = (String) getModel().getElementAt(index);

                StringBuilder text = new StringBuilder();
                text.append("<html>");
                text.append(muc.getOccupant(user).getJid());
                text.append("<br>");
                text.append("<strong>Affiliation: </strong>");
                text.append(muc.getOccupant(user).getAffiliation());
                text.append("<br>");
                text.append("<strong>Role: </strong>");
                text.append(muc.getOccupant(user).getRole());
                text.append("</html>");

                // Return the tool tip text
                return text.toString();
            }
        };//);
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        emoticonsButton = new javax.swing.JButton();
        formatButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(muc.getRoom() + " - Conference");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        inviteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/user_add.png"))); // NOI18N
        inviteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(inviteButton);

        subjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/page_white_text_width.png"))); // NOI18N
        subjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(subjectButton);

        banButton.setText("Ban user");
        banButton.setFocusable(false);
        banButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        banButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        banButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(banButton);

        kickButton.setText("Kick user");
        kickButton.setFocusable(false);
        kickButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kickButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        kickButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kickButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(kickButton);

        Utils.flattenSplitPane(verticalSplitPane);
        verticalSplitPane.setBorder(null);
        verticalSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        Utils.flattenSplitPane(horizontalSplitPane);
        horizontalSplitPane.setBorder(null);
        horizontalSplitPane.setDividerSize(7);
        horizontalSplitPane.setOneTouchExpandable(true);

        messageScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        messageTextPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        messageTextPane.setBackground(new java.awt.Color(255, 255, 255));
        messageTextPane.setEditable(false);
        messageTextPane.setStyledDocument(new ChatTextPaneStyledDocument());
        messageScrollPane.setViewportView(messageTextPane);

        horizontalSplitPane.setLeftComponent(messageScrollPane);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        memberList.setBackground(new java.awt.Color(255, 255, 255));
        memberList.setCellRenderer(new MemberListRenderer());
        memberList.setMinimumSize(new java.awt.Dimension(85, 0));
        jScrollPane3.setViewportView(memberList);

        horizontalSplitPane.setRightComponent(jScrollPane3);

        verticalSplitPane.setLeftComponent(horizontalSplitPane);

        jPanel1.setMinimumSize(new java.awt.Dimension(0, 30));

        sendTextArea.setColumns(20);
        sendTextArea.setFont(sendTextArea.getFont());
        sendTextArea.setLineWrap(true);
        sendTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sendTextAreaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(sendTextArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emoticonsButton, 0, 0, Short.MAX_VALUE)
                    .addComponent(formatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(formatButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emoticonsButton))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );

        verticalSplitPane.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verticalSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verticalSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void send()
    {
        String text = sendTextArea.getText().trim();

        // dont send anything if there's no text to send
        if (text.length() == 0)
        {
            return;
        }

        try
        {
            Message message = new Message(muc.getRoom(), Message.Type.groupchat);
            message.setBody(text);
            message.setProperty("format", format);
            muc.sendMessage(message);
            sendTextArea.setText("");
        }
        catch (XMPPException ex)
        {
            Logger.getLogger(MultiUserChatUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
    send();
}//GEN-LAST:event_sendButtonActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    int option = JOptionPane.showConfirmDialog(this,
            "Do you wish to leave the conference?",
            "Leaving Conference",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (option == JOptionPane.YES_OPTION)
    {
        ContactListUI.accountManager.logConversation(messageTextPane, muc.getRoom());
        muc.leave();
        dispose();
    }
}//GEN-LAST:event_formWindowClosing

private void inviteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviteButtonActionPerformed

    MultiUserChatInviteUI invite = new MultiUserChatInviteUI(this, true);
    String values[] = invite.showDialog();

    if (values[0] == null)
    {
        return;
    }
    muc.invite(values[0], values[1]);
}//GEN-LAST:event_inviteButtonActionPerformed

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

private void subjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectButtonActionPerformed
        String subject = JOptionPane.showInputDialog(this, "Enter new subject");

        if (subject == null)
        {
            return;
        }
        if (subject.equals(""))
        {
            return;
        }
        try
        {
            muc.changeSubject(subject);
        }
        catch (XMPPException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "You are not allowed to change the subject",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
//GEN-LAST:event_subjectButtonActionPerformed
private void banButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_banButtonActionPerformed
    String reason = JOptionPane.showInputDialog(this, "Reason for ban");
    String member = (String) memberList.getSelectedValue();
    try
    {//GEN-LAST:event_banButtonActionPerformed
            muc.banUser(member, reason);
        }
        catch (XMPPException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Could not ban user " + member + "\n" +
                    ex.getMessage(),
                    "Could not ban user",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
private void emoticonsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emoticonsButtonActionPerformed
    Point point = emoticonsButton.getMousePosition();
    SwingUtilities.convertPointToScreen(point, emoticonsButton);
    new EmoticonsUI(this, sendTextArea, point).setVisible(true);
}//GEN-LAST:event_emoticonsButtonActionPerformed

private void formatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatButtonActionPerformed
    FormatterUI formatter = new FormatterUI(null, format.getFont(), format.getColor());
    format = formatter.showDialog();
    sendTextArea.setFont(format.getFont());
    sendTextArea.setForeground(format.getColor());
}//GEN-LAST:event_formatButtonActionPerformed

private void kickButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kickButtonActionPerformed
    String member = (String) memberList.getSelectedValue();
    String reason = JOptionPane.showInputDialog(this, "Reason for kick");
    try
    {//GEN-LAST:event_kickButtonActionPerformed
        muc.kickParticipant(member, reason);
    }
    catch (XMPPException ex)
    {
        JOptionPane.showMessageDialog(this,
                "Could not kick user " + member + "\n" +
                ex.getMessage(),
                "Could not kick user",
                JOptionPane.ERROR_MESSAGE);
    }
}

    @Override
    public void processPacket(Packet packet)
    {
        Message message = (Message) packet;
        doc.insertUser(StringUtils.parseResource(message.getFrom()));
        // see if the message has a formatting property
        if (message.getProperty("format") != null)
        {
            Format newFormat = (Format) message.getProperty("format");
            doc.insertMessage(message.getBody(), newFormat);
        }
        // insert message body with defualt style
        else
        {
            doc.insertMessage(message.getBody());
        }
        messageTextPane.setCaretPosition(doc.getLength());
    }

    private void updateOccupantList()
    {
        DefaultListModel model = null;

        model = new DefaultListModel();

        Iterator<String> occupants = muc.getOccupants();

        while (occupants.hasNext())
        {
            model.addElement(occupants.next());
        }

        memberList.setModel(model);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton banButton;
    private javax.swing.JButton emoticonsButton;
    private javax.swing.JButton formatButton;
    private javax.swing.JSplitPane horizontalSplitPane;
    private javax.swing.JButton inviteButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton kickButton;
    private javax.swing.JList memberList;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextPane messageTextPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea sendTextArea;
    private javax.swing.JButton subjectButton;
    private javax.swing.JSplitPane verticalSplitPane;
    // End of variables declaration//GEN-END:variables

    private class MemberListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            lbl.setText(StringUtils.parseResource((String) value));
            return lbl;
        }
    }
}

