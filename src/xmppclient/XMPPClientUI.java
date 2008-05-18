/*
 * XMPPClientUI.java
 *
 * Created on 10 April 2008, 17:25
 */
package xmppclient;

import xmppclient.multiuserchat.MUCChatUI;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;
import xmppclient.images.Icons;
import xmppclient.images.tango.TangoIcons;
import xmppclient.multiuserchat.InvitationReceivedUI;
import xmppclient.vcard.VCardEditor;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class XMPPClientUI extends javax.swing.JFrame implements FileTransferListener
{
    public static XMPPConnection connection;
    private TrayIcon trayIcon;
    private SystemTray tray;
    private Image appIcon = new ImageIcon(this.getClass().getResource(
            "/xmppclient/images/user.png")).getImage();
    public static ChatUI chatUI;
    private String accountName;
    private final int SORT_BY_STATUS = 0;
    private final int SORT_BY_GROUP = 1;
    private int sortMethod = SORT_BY_STATUS;

    /** Creates new form XMPPClientUI */
    public XMPPClientUI(XMPPConnection connection, String accountName)
    {
        XMPPClientUI.connection = connection;
        this.accountName = accountName;
        MultiUserChat.addInvitationListener(connection, new InvitationReceivedUI(this, true));
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        chatUI = new ChatUI();
        initComponents();
        initSystemTray();
        initStatusComboBox();
        updateContacts();
        contentPanel.setVisible(false);
        connection.getChatManager().addChatListener(chatUI);
        connection.addPacketListener(new SubscriptionRequestListener(), new PacketTypeFilter(Presence.class));
        FileTransferManager manager = new FileTransferManager(connection);
        manager.addFileTransferListener(this);

        // toggle the sign in/out menu items
        signOutMenuItem.setEnabled(true);

        // set the contact list
        connection.getRoster().addRosterListener(new ContactListListener(this));

        nicknameTextField.setText(getUserNickname(connection.getUser()));
        setAvatar();

        // show the content panel
        contentPanel.setVisible(true);
    }

    private void initStatusComboBox()
    {
        statusComboBox.removeAllItems();

        // add default statuses
        statusComboBox.addItem(new xmppclient.Presence(
                Presence.Type.available,
                Presence.Mode.available,
                "Online"));
        statusComboBox.addItem(new xmppclient.Presence(
                Presence.Type.available,
                Presence.Mode.away,
                "Away"));
        statusComboBox.addItem(new xmppclient.Presence(
                Presence.Type.available,
                Presence.Mode.xa,
                "Extended away"));
        statusComboBox.addItem(new xmppclient.Presence(
                Presence.Type.available,
                Presence.Mode.dnd,
                "Busy"));
        statusComboBox.addItem(new xmppclient.Presence(
                Presence.Type.available,
                Presence.Mode.chat,
                "Free to chat"));
        statusComboBox.addItem(new xmppclient.Presence());
        statusComboBox.addItem(new JSeparator());
        statusComboBox.addItem(new CustomStatusDialog(this, true));
        statusComboBox.addItem(new JSeparator());
        statusComboBox.setSelectedIndex(0);
    }

    private void initSystemTray()
    {
        if (SystemTray.isSupported())
        {
            tray = SystemTray.getSystemTray();
            //Image image = Toolkit.getDefaultToolkit().getImage("images/user.png");

            PopupMenu popup = new PopupMenu();

            trayIcon = new TrayIcon(appIcon, "XMPPClient", popup);
            trayIcon.setImageAutoSize(true);
            MenuItem exitMenuItem = new MenuItem("Exit");
            MenuItem showMenuItem = new MenuItem("Show/hide");
            exitMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evt)
                {
                    exit();
                }
            });
            showMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evt)
                {
                    toggleWindowVisibility();
                }
            });

            popup.add(showMenuItem);
            popup.addSeparator();
            popup.add(exitMenuItem);

            trayIcon.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    // check the correct button was pressed
                    if (e.getButton() != MouseEvent.BUTTON1)
                    {
                        return;
                    // toggle only once on double click
                    }
                    if (e.getClickCount() == 2)
                    {
                        return;
                    }
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

    private void sortContactsByGroup()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Contacts");
        DefaultTreeModel model = new DefaultTreeModel(root);

        Collection<RosterGroup> groups = connection.getRoster().getGroups();
        DefaultMutableTreeNode groupNode;

        // get defined groups
        for (RosterGroup group : groups)
        {
            groupNode = new DefaultMutableTreeNode(group.getName());
            root.add(groupNode);

            for (RosterEntry entry : group.getEntries())
            {
                groupNode.add(new DefaultMutableTreeNode(entry));
            }
        }

        // get unfiled contacts
        if (connection.getRoster().getUnfiledEntryCount() > 0)
        {
            DefaultMutableTreeNode unfiled = new DefaultMutableTreeNode("Unfiled");
            root.add(unfiled);
            for (RosterEntry entry : connection.getRoster().getUnfiledEntries())
            {
                unfiled.add(new DefaultMutableTreeNode(entry));
            }
        }

        contactTree.setModel(model);

        int row = 0;
        while (row < contactTree.getRowCount())
        {
            contactTree.expandRow(row++);
        }
    }

    private void sortContactsByStatus()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Contacts");
        DefaultMutableTreeNode online = new DefaultMutableTreeNode("Online");
        DefaultMutableTreeNode offline = new DefaultMutableTreeNode("Offline");

        DefaultTreeModel model = new DefaultTreeModel(root);

        root.add(online);
        root.add(offline);

        Collection<RosterEntry> entries = connection.getRoster().getEntries();

        for (RosterEntry entry : entries)
        {
            if (connection.getRoster().getPresence(entry.getUser()).getType().equals(Presence.Type.available))
            {
                online.add(new DefaultMutableTreeNode(entry));
            }
            else
            {
                offline.add(new DefaultMutableTreeNode(entry));
            }
        }

        contactTree.setModel(model);

        int row = 0;
        while (row < contactTree.getRowCount())
        {
            contactTree.expandRow(row++);
        }
    }

    private void toggleWindowVisibility()
    {
        if (XMPPClientUI.this.isVisible())
        {
            XMPPClientUI.this.setVisible(false);
        }
        else
        {
            XMPPClientUI.this.setVisible(true);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sortContactsButtonGroup = new javax.swing.ButtonGroup();
        contentPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        addContactButton = new javax.swing.JButton();
        vCardButton = new javax.swing.JButton();
        avatarButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        hoverTextLabel = new javax.swing.JLabel();
        nicknameTextField = new javax.swing.JTextField();
        statusComboBox = new javax.swing.JComboBox()
        {
            public void setSelectedIndex(int index)
            {
                if(statusComboBox.getItemAt(index) instanceof JSeparator)
                return;

                super.setSelectedIndex(index);
            }
        };//);
        avatarLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contactTree = new javax.swing.JTree()
        {
            public JToolTip createToolTip()
            {
                JToolTip tip = super.createToolTip();
                String text = tip.getTipText();
                return new JToolTip();
            }

            public String getToolTipText(MouseEvent evt)
            {
                TreePath path = getPathForLocation(evt.getX(), evt.getY());
                if(path == null) return null;
                if(path.getLastPathComponent() instanceof DefaultMutableTreeNode)
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if(node.getUserObject() instanceof RosterEntry)
                    {
                        RosterEntry entry = (RosterEntry) node.getUserObject();
                        return entry.getUser();
                    }
                }

                return null;
            }
        };//);
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        signOutMenuItem = new javax.swing.JMenuItem();
        minimiseMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        contactsMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        toolsMenu = new javax.swing.JMenu();
        createChatRoomMenuItem = new javax.swing.JMenuItem();
        joinChatRoomMenuItem = new javax.swing.JMenuItem();
        sendFileMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("XMPPClient");
        setIconImage(appIcon);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(200, 300));
        setName("XMPPClient"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        addContactButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/user_add.png"))); // NOI18N
        addContactButton.setToolTipText("Add contact");
        addContactButton.setFocusable(false);
        addContactButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addContactButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addContactButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addContactButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addContactButtonMouseExited(evt);
            }
        });
        addContactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContactButtonActionPerformed(evt);
            }
        });
        toolBar.add(addContactButton);

        vCardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/vcard_edit.png"))); // NOI18N
        vCardButton.setToolTipText("Edit VCard");
        vCardButton.setFocusable(false);
        vCardButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        vCardButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        vCardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                vCardButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                vCardButtonMouseExited(evt);
            }
        });
        vCardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vCardButtonActionPerformed(evt);
            }
        });
        toolBar.add(vCardButton);

        avatarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/image.png"))); // NOI18N
        avatarButton.setToolTipText("Change avatar");
        avatarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                avatarButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                avatarButtonMouseExited(evt);
            }
        });
        avatarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avatarButtonActionPerformed(evt);
            }
        });
        toolBar.add(avatarButton);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/cog.png"))); // NOI18N
        jButton1.setToolTipText("Edit preferences");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        toolBar.add(jButton1);

        hoverTextLabel.setFont(new java.awt.Font("Tahoma", 0, 10));
        hoverTextLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        toolBar.add(hoverTextLabel);

        nicknameTextField.setToolTipText("Press enter to set the nickname");
        nicknameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicknameTextFieldActionPerformed(evt);
            }
        });

        statusComboBox.setMaximumRowCount(12);
        statusComboBox.setRenderer(new xmppclient.StatusComboBoxRenderer());
        statusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusComboBoxActionPerformed(evt);
            }
        });

        avatarLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(160, 160, 160), 1, true));

        ToolTipManager.sharedInstance().registerComponent(contactTree);
        contactTree.setCellRenderer(new ContactTreeRenderer());
        contactTree.setModel(null);
        contactTree.setRootVisible(false);
        contactTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactTreeMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                contactTreeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                contactTreeMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(contactTree);

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusComboBox, 0, 192, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nicknameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avatarLabel))
                    .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                .addContainerGap())
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentPanelLayout.createSequentialGroup()
                        .addComponent(nicknameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(avatarLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileMenu.setText("File");

        signOutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/log-out-22x22.png"))); // NOI18N
        signOutMenuItem.setText("Sign out");
        signOutMenuItem.setEnabled(false);
        signOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(signOutMenuItem);

        minimiseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        minimiseMenuItem.setText("Minimise");
        minimiseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimiseMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(minimiseMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/system-shutdown-22x22.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        contactsMenu.setText("Contacts");

        jMenu1.setText("Sort by");

        sortContactsButtonGroup.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("Status");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem1);

        sortContactsButtonGroup.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText("Group");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem2);

        contactsMenu.add(jMenu1);

        menuBar.add(contactsMenu);

        toolsMenu.setText("Tools");
        toolsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolsMenuActionPerformed(evt);
            }
        });

        createChatRoomMenuItem.setText("Create chat room");
        createChatRoomMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createChatRoomMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(createChatRoomMenuItem);

        joinChatRoomMenuItem.setText("Join chat room");
        joinChatRoomMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinChatRoomMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(joinChatRoomMenuItem);

        sendFileMenuItem.setText("Send file...");
        sendFileMenuItem.setEnabled(false);
        toolsMenu.add(sendFileMenuItem);

        menuBar.add(toolsMenu);

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

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        exit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void signOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutMenuItemActionPerformed
        signOut();
}//GEN-LAST:event_signOutMenuItemActionPerformed

    private void nicknameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nicknameTextFieldActionPerformed
        VCard vCard = new VCard();
        try
        {
            // first try to get stored VCard
            vCard.load(connection);
            System.out.println("Loaded nickname");
        }
        catch (XMPPException e)
        {
        } // no vcard

        vCard.setNickName(nicknameTextField.getText());

        if (nicknameTextField.getText().equals(""))
        {
            vCard.setNickName(connection.getUser());
        }
        try
        {
            // send the new nickname
            vCard.save(connection);
            System.out.println("Saved nickname");
        }
        catch (XMPPException e)
        {
            e.printStackTrace();
        }

        connection.sendPacket((Presence) statusComboBox.getSelectedItem());

        if (nicknameTextField.getText().equals(""))
        {
            nicknameTextField.setText(connection.getUser());
        }
    }//GEN-LAST:event_nicknameTextFieldActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (Utils.loadProperty(accountName, "display_minimise_message").equals("false"))
        {
            setVisible(false);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Closing the window minimises to tray. " +
                "\nTo close, right click the tray icon and select exit.",
                "Minimising client",
                JOptionPane.INFORMATION_MESSAGE);

        Utils.saveProperty(accountName, "display_minimise_message", "false");

        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        if (statusComboBox.getSelectedItem() instanceof CustomStatusDialog)
        {
            CustomStatusDialog dialog = (CustomStatusDialog) statusComboBox.getSelectedItem();
            Presence presence = dialog.showDialog();
            if (presence == null)
            {
                return;
            }
            initStatusComboBox();
            statusComboBox.addItem(presence);
            return;
        }
        connection.sendPacket((Presence) statusComboBox.getSelectedItem());
    }//GEN-LAST:event_statusComboBoxActionPerformed

private void avatarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avatarButtonActionPerformed
    new AvatarChooser(this, false);
}//GEN-LAST:event_avatarButtonActionPerformed

private void toolsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsMenuActionPerformed
}//GEN-LAST:event_toolsMenuActionPerformed

private void avatarButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avatarButtonMouseEntered
    setHoverText(evt);
}//GEN-LAST:event_avatarButtonMouseEntered

private void addContactButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addContactButtonMouseEntered
    setHoverText(evt);
}//GEN-LAST:event_addContactButtonMouseEntered

private void vCardButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vCardButtonMouseEntered
    setHoverText(evt);
}//GEN-LAST:event_vCardButtonMouseEntered

private void avatarButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avatarButtonMouseExited
    clearHoverText();
}//GEN-LAST:event_avatarButtonMouseExited

private void addContactButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addContactButtonMouseExited
    clearHoverText();
}//GEN-LAST:event_addContactButtonMouseExited

private void vCardButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vCardButtonMouseExited
    clearHoverText();
}//GEN-LAST:event_vCardButtonMouseExited

private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
    setHoverText(evt);
}//GEN-LAST:event_jButton1MouseEntered

private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
    clearHoverText();
}//GEN-LAST:event_jButton1MouseExited

private void vCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vCardButtonActionPerformed
    VCard vCard = new VCard();
    try
    {
        vCard.load(connection);
    }
    catch (XMPPException ex)
    {
    }
    new VCardEditor(vCard, true).setVisible(true);
}//GEN-LAST:event_vCardButtonActionPerformed

private void addContactButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addContactButtonActionPerformed
    showAddContactDialog();
}//GEN-LAST:event_addContactButtonActionPerformed

private void joinChatRoomMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinChatRoomMenuItemActionPerformed
    String room = JOptionPane.showInputDialog(this, "Enter room name");
    MUCChatUI mucui = new MUCChatUI(room);
    try
    {
        mucui.join(Utils.getNickname());
    }
    catch (XMPPException ex)
    {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    mucui.setVisible(true);

}//GEN-LAST:event_joinChatRoomMenuItemActionPerformed

private void createChatRoomMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createChatRoomMenuItemActionPerformed
    String room = JOptionPane.showInputDialog(this, "Enter room name");
    MUCChatUI mucui = new MUCChatUI(room);

    try
    {
        mucui.create(Utils.getNickname());
    }
    catch (XMPPException ex)
    {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    mucui.setVisible(true);

}//GEN-LAST:event_createChatRoomMenuItemActionPerformed

private void minimiseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimiseMenuItemActionPerformed
    setVisible(false);
}//GEN-LAST:event_minimiseMenuItemActionPerformed

private void contactTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactTreeMouseClicked

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) contactTree.getLastSelectedPathComponent();
    if (node == null)
    {
        return;
    }

    if (node.getUserObject() instanceof RosterEntry)
    {
        if (evt.isPopupTrigger())
        {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem("Fef"));
            popup.show(evt.getComponent(), evt.getX(), evt.getY());
            return;
        }
        if (evt.getClickCount() == 2)
        {
            RosterEntry rosterEntry = (RosterEntry) node.getUserObject();
            connection.getChatManager().createChat(rosterEntry.getUser(), chatUI);
        }
    }
}//GEN-LAST:event_contactTreeMouseClicked

private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
    sortMethod = SORT_BY_GROUP;
    updateContacts();
}//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
    sortMethod = SORT_BY_STATUS;
    updateContacts();
}//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

private void contactTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactTreeMouseReleased
    showContactPopup(evt);
}//GEN-LAST:event_contactTreeMouseReleased

private void contactTreeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactTreeMousePressed
    showContactPopup(evt);
}//GEN-LAST:event_contactTreeMousePressed

    private void showContactPopup(java.awt.event.MouseEvent evt)
    {
        if (!evt.isPopupTrigger())
        {
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) contactTree.getClosestPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();

        if (node.getUserObject() instanceof RosterEntry)
        {
            final RosterEntry entry = (RosterEntry) node.getUserObject();
            final JPopupMenu menu = new JPopupMenu();
            JMenuItem nickname = new JMenuItem(Utils.getNickname(entry));
            nickname.setEnabled(false);
            menu.add(nickname);
            JMenuItem chat = new JMenuItem("Open chat", Icons.userComment);
            menu.add(chat);
            chat.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    connection.getChatManager().createChat(entry.getUser(), chatUI);
                }
            });
            JMenuItem vcard = new JMenuItem("View VCard", Icons.vcard);
            vcard.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    VCard vCard = new VCard();
                    try
                    {
                        vCard.load(connection, entry.getUser());
                    }
                    catch (XMPPException ex)
                    {
                    }
                    new VCardEditor(vCard, false).setVisible(true);
                }
            });
            menu.add(vcard);
            JMenuItem groupMenuItem = new JMenuItem("Change group");
            groupMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String groups =
                            JOptionPane.showInputDialog(XMPPClientUI.this,
                            "Insert group names, separated by commas",
                            Utils.getGroupsCSV(entry));
                    if (groups == null)
                    {
                        return;
                    // remove user from all groups
                    }
                    for (RosterGroup r : entry.getGroups())
                    {
                        try
                        {
                            r.removeEntry(entry);
                        }
                        catch (XMPPException ex)
                        {
                            JOptionPane.showMessageDialog(XMPPClientUI.this,
                                    "Could not remove user from group" +
                                    ex.getMessage(),
                                    "Error removing user from group",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    if (groups.equals(""))
                    {
                        return;
                    }
                    String groupsArray[] = groups.split(",");

                    RosterGroup rosterGroup;
                    for (String group : groupsArray)
                    {
                        if (connection.getRoster().getGroup(group.trim()) == null)
                        {
                            rosterGroup = connection.getRoster().createGroup(group.trim());
                        }
                        else
                        {
                            rosterGroup = connection.getRoster().getGroup(group.trim());
                        }
                        try
                        {
                            rosterGroup.addEntry(entry);
                        }
                        catch (XMPPException ex)
                        {
                            JOptionPane.showMessageDialog(XMPPClientUI.this,
                                    "Could not add user to group" +
                                    ex.getMessage(),
                                    "Error adding user to group",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                        updateContacts();
                    }
                }
            });
            menu.add(groupMenuItem);
            JMenuItem sendFileMenuItem = new JMenuItem("Send file");
            sendFileMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    new FileTransferChooser(XMPPClientUI.this, true, entry).setVisible(true);
                }
            });
            menu.add(sendFileMenuItem);
            JMenuItem setNameMenuItem = new JMenuItem("Set name");
            setNameMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String name = JOptionPane.showInputDialog(XMPPClientUI.this, 
                            "Enter a name for this user, or leave blank to remove");
                    if(name==null) return;
                    entry.setName(name);
                    updateContacts();
                }
            });
            menu.add(setNameMenuItem);
            menu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    private void setHoverText(java.awt.event.MouseEvent evt)
    {
        hoverTextLabel.setText(((JButton) evt.getSource()).getToolTipText());
    }

    private void clearHoverText()
    {
        hoverTextLabel.setText("");
    }

    private void showAddContactDialog()
    {
        String contact = (String) JOptionPane.showInputDialog(this, "Enter the JID of the contact you wish to add", "Add user", JOptionPane.PLAIN_MESSAGE, TangoIcons.users32x32, null, null);
        contact = StringUtils.parseBareAddress(contact);
        Presence request = new Presence(Presence.Type.subscribe);
        request.setTo(contact);
        connection.sendPacket(request);
    }

    private void exit()
    {
        requestFocus();

        // if the user is signed in then sign them out before exiting
        if (connection != null)
        {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Do you wish to sign out and close XMPPClient?",
                    "Close XMPPClient",
                    JOptionPane.OK_CANCEL_OPTION);

            if (confirm == JOptionPane.OK_OPTION)
            {
                signOut();
            }
            else
            {
                return;
            }
        }

        System.exit(0);
    }

    public void setAvatar()
    {
        avatarLabel.setIcon(Utils.getAvatar(85));
        if (avatarLabel.getIcon() == null)
        {
            avatarLabel.setVisible(false);
        }
    }

    public void setAvatar(ImageIcon icon)
    {
        avatarLabel.setIcon(Utils.resizeImage(icon, 85));
        if (avatarLabel.getIcon() == null)
        {
            avatarLabel.setVisible(false);
        }
    }

    public String getUserNickname(String user)
    {
        VCard VCard = new VCard();

        try
        {
            VCard.load(connection);
            user = VCard.getNickName();
        }
        catch (XMPPException e)
        {
        }

        return user;
    }

    public XMPPConnection getConnection()
    {
        return connection;
    }

    public void updateContacts()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (sortMethod == SORT_BY_STATUS)
                    {
                        sortContactsByStatus();
                    }
                    else
                    {
                        sortContactsByGroup();
                    }
                }
                catch (Exception e)
                {
                }
            }
        });
    }

    private void signOut()
    {
        connection.disconnect();
        connection = null;
        signOutMenuItem.setEnabled(false);
        contentPanel.setVisible(false);
        new XMPPClient().setVisible(true);
        if (tray != null)
        {
            tray.remove(trayIcon);
        }
        this.dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addContactButton;
    private javax.swing.JButton avatarButton;
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JTree contactTree;
    private javax.swing.JMenu contactsMenu;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JMenuItem createChatRoomMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel hoverTextLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem joinChatRoomMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem minimiseMenuItem;
    private javax.swing.JTextField nicknameTextField;
    private javax.swing.JMenuItem sendFileMenuItem;
    private javax.swing.JMenuItem signOutMenuItem;
    private javax.swing.ButtonGroup sortContactsButtonGroup;
    private javax.swing.JComboBox statusComboBox;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JButton vCardButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void fileTransferRequest(FileTransferRequest request)
    {
        Object[] options =
        {
            "Accept", "Reject"
        };
        String desc = request.getDescription();
        if (desc == null)
        {
            desc = "None entered";
        }
        int option = JOptionPane.showOptionDialog(this,
                "File transfer request received from " + request.getRequestor() + "\nFilename: " + request.getFileName() + "\nDescription: " + request.getDescription() + "\nWould you like to accept or reject it?",
                "File Transfer Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (option == JOptionPane.YES_OPTION)
        {
            IncomingFileTransfer transfer = request.accept();
            (new File("received")).mkdir();

            try
            {
                transfer.recieveFile(new File("received/" + request.getFileName()));
                new FileTransferUI(transfer);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            catch (XMPPException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            request.reject();
        }
    }

    private class SubscriptionRequestListener implements PacketListener
    {
        @Override
        public void processPacket(Packet packet)
        {
            Presence presence = (Presence) packet;

            if (presence.getType() == Presence.Type.subscribe)
            {
                Object options[] =
                {
                    "Accept", "Reject"
                };

                int selection = JOptionPane.showOptionDialog(XMPPClientUI.this,
                        "The user " + presence.getFrom() + " wishes to subscribe" +
                        "\nto your presence information. Do you accept or reject?",
                        "Subscription Request",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        null);

                Presence auth;

                if (selection == JOptionPane.YES_OPTION)
                {
                    // accept
                    auth = new Presence(Presence.Type.subscribe);
                }
                else
                {
                    // reject
                    auth = new Presence(Presence.Type.unsubscribe);
                }

                auth.setTo(presence.getFrom());
                connection.sendPacket(auth);
            }
        }
    }
}
