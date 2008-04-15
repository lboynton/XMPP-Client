/*
 * XMPPClientUI.java
 *
 * Created on 10 April 2008, 17:25
 */

package xmppclient;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

/**
 *
 * @author  lee
 */
public class XMPPClientUI extends javax.swing.JFrame
{
    private XMPPConnection connection;
    private Roster roster;
    private TrayIcon trayIcon;
    private Image appIcon = new ImageIcon(this.getClass().getResource(
                "/xmppclient/images/user.png")).getImage();
    
    /** Creates new form XMPPClientUI */
    public XMPPClientUI() 
    {
        XMPPConnection.DEBUG_ENABLED = false;
        try
        {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.out.println("Unable to use system look and feel");
        }
        
        initComponents();
        initSystemTray();
        initStatusComboBox();
        this.setLocationRelativeTo(null);
        contentPanel.setVisible(false);
        
    }
    
    private void initStatusComboBox()
    {
        statusComboBox.removeAllItems();
        statusComboBox.addItem(new OnlinePresence());
        statusComboBox.addItem(new AwayPresence());
        statusComboBox.setSelectedIndex(0);
    }
    
    private void initSystemTray()
    {
        if (SystemTray.isSupported()) 
        {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");
            
            PopupMenu popup = new PopupMenu();
            
            trayIcon = new TrayIcon(image, "XMPPClient", popup);
            trayIcon.setImageAutoSize(true);
            MenuItem exitMenuItem = new MenuItem("Exit");
            MenuItem showMenuItem = new MenuItem("Show/Hide");
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt)
                {
                    exit();
                }
            });
            showMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt)
                {
                    toggleWindowVisibility();
                }
            });
            
            popup.add(showMenuItem);
            popup.addSeparator();
            popup.add(exitMenuItem);
            
            trayIcon.addMouseListener(new MouseAdapter () 
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    // check the correct button was pressed
                    if(e.getButton() != MouseEvent.BUTTON1) return;
                    
                    // toggle only once on double click
                    if(e.getClickCount() == 2) return;

                    toggleWindowVisibility();
                }
            });
            
            try 
            {
                tray.add(trayIcon);
            } 
            catch (AWTException e) 
            {
                System.err.println("TrayIcon could not be added.");
            }
        }
    }
    
    public void toggleWindowVisibility()
    {
        if(XMPPClientUI.this.isVisible())
        {
            XMPPClientUI.this.setVisible(false);
        }
        else
        {
            XMPPClientUI.this.setVisible(true);
        }
    }
    
    public void setConnection(XMPPConnection connection)
    {
        this.connection = connection;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPanel = new javax.swing.JPanel();
        contactListScrollPane = new javax.swing.JScrollPane();
        contactList = new javax.swing.JList();
        nicknameTextField = new javax.swing.JTextField();
        statusComboBox = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        signInMenuItem = new javax.swing.JMenuItem();
        signOutMenuItem = new javax.swing.JMenuItem();
        fileMenuSeparator = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("XMPPClient");
        setIconImage(appIcon);
        setMinimumSize(new java.awt.Dimension(200, 300));
        setName("XMPPClient"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        contactList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactList.setCellRenderer(new ContactListRenderer());
        contactList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactListMouseClicked(evt);
            }
        });
        contactList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                contactListValueChanged(evt);
            }
        });
        contactListScrollPane.setViewportView(contactList);

        nicknameTextField.setToolTipText("Press enter to set the nickname");
        nicknameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicknameTextFieldActionPerformed(evt);
            }
        });

        statusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup()
                        .addComponent(nicknameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(contactListScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addContainerGap())
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nicknameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileMenu.setText("File");

        signInMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        signInMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/connect.png"))); // NOI18N
        signInMenuItem.setText("Sign in...");
        signInMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(signInMenuItem);

        signOutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/disconnect.png"))); // NOI18N
        signOutMenuItem.setText("Sign out");
        signOutMenuItem.setEnabled(false);
        signOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(signOutMenuItem);
        fileMenu.add(fileMenuSeparator);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/cross.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void signInMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInMenuItemActionPerformed
        signIn();
    }//GEN-LAST:event_signInMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        exit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void signOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutMenuItemActionPerformed
        signOut();
}//GEN-LAST:event_signOutMenuItemActionPerformed

    private void contactListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_contactListValueChanged
    }//GEN-LAST:event_contactListValueChanged

    private void contactListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactListMouseClicked
        if(evt.getClickCount() == 2)
        {
            System.out.println("Double click");
            
            System.out.println(contactList.getSelectedValue().toString());
        }
    }//GEN-LAST:event_contactListMouseClicked

    private void nicknameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nicknameTextFieldActionPerformed
        VCard vCard = new VCard();

        try
        {
            // first try to get stored VCard
            vCard.load(connection);
            
        }
        catch(XMPPException e) { } // no vcard
        
        vCard.setNickName(nicknameTextField.getText());
        
        try
        {
            // send the new nickname
            vCard.save(connection);
        }
        catch(XMPPException e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_nicknameTextFieldActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        // check the connection has been made
        if(connection != null && connection.isConnected())
            connection.sendPacket((Presence)statusComboBox.getSelectedItem());
    }//GEN-LAST:event_statusComboBoxActionPerformed
 
    private void exit()
    {
        // if the user is signed in then sign them out before exiting
        if(connection != null) 
        {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Do you wish to sign out and close XMPPClient?",
                    "Close XMPPClient",
                    JOptionPane.OK_CANCEL_OPTION);
            
            if(confirm == JOptionPane.OK_OPTION) signOut();
            else return;
        }
        
        System.exit(0);
    }
    
    private void signIn()
    {
        new XMPPClientSignInUI(this).setVisible(true);
        
        // check if user clicked cancel
        if(connection == null) return;
        
        // toggle the sign in/out menu items
        signOutMenuItem.setEnabled(true);
        signInMenuItem.setEnabled(false);
        
        // get the roster
        roster = connection.getRoster();
        roster.addRosterListener(new ContactListListener(this));
        
        // set the contact list
        contactList.setListData(roster.getEntries().toArray());
        
        // show the content panel
        contentPanel.setVisible(true);
    }
    
    public XMPPConnection getConnection()
    {
        return connection;
    }
    
    public void updateContactList()
    {
        contactList.setListData(roster.getEntries().toArray());
    }
    
    private void signOut()
    {
        contactList.setModel(new DefaultListModel());
        connection.disconnect();
        connection = null;
        signInMenuItem.setEnabled(true);
        signOutMenuItem.setEnabled(false);
        contentPanel.setVisible(false);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new XMPPClientUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList contactList;
    private javax.swing.JScrollPane contactListScrollPane;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator fileMenuSeparator;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextField nicknameTextField;
    private javax.swing.JMenuItem signInMenuItem;
    private javax.swing.JMenuItem signOutMenuItem;
    private javax.swing.JComboBox statusComboBox;
    // End of variables declaration//GEN-END:variables
    
}
