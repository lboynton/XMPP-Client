/*
 * ChatUI.java
 *
 * Created on 14 April 2008, 23:40
 */

package xmppclient;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class ChatUI extends javax.swing.JFrame implements MessageListener, ChatManagerListener
{
    
    /** Creates new form ChatUI */
    public ChatUI() 
    {
        initComponents();
    }
    
    public void addChat(Chat chat)
    {
        if(getTabIndex(chat) == -1) 
        {
            chat.addMessageListener(this);
            tabs.add( new ChatPanel(chat, this) );
        }
    }
    
    public int getTabIndex(Chat chat)
    {     
        for(int i = 0; i < tabs.getTabCount(); i++)
        {
            ChatPanel tab = (ChatPanel)tabs.getComponentAt(i);
            
            // return the tab that contains the JID without resource
            if(StringUtils.parseBareAddress(tab.getChat().getParticipant())
                    .equals(StringUtils.parseBareAddress(chat.getParticipant()))) return i;
        }
        
        // return -1 if tab is not present
        return -1;
    }
    
    public ChatPanel getChat(Chat chat)
    {
        return (ChatPanel) tabs.getComponentAt(getTabIndex(chat));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(350, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        ChatPanel chatPanel = (ChatPanel) tabs.getSelectedComponent();
        
        tabs.remove(chatPanel);
        
        if(tabs.getTabCount() == 0) setVisible(false);
    }//GEN-LAST:event_formWindowClosing
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables

    @Override
    public void chatCreated(Chat chat, boolean createdLocally)
    {
        chat.addMessageListener(this);
        
        if(createdLocally) 
        {
            // add the chat
            addChat(chat);
            
            // show the window
            setVisible(true);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message)
    {
        if(message.getType() == Message.Type.chat)
        {
            // a message has been sent, show the window
            setVisible(true);
            addChat(chat);
            getChat(chat).addMessage(Utils.getAvatar(chat.getParticipant(), 50), Utils.getNickname(chat.getParticipant()), message);
        }
    }
    
}
