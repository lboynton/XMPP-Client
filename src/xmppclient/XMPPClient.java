/*
 * XMPPClient.java
 *
 * Created on 04 May 2008, 17:49
 */

package xmppclient;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Contains the main method for starting the application.
 * Shows the main login JFrame.
 * @author  Lee Boynton (323326)
 */
public class XMPPClient extends javax.swing.JFrame 
{
    private XMPPConnection XMPPConnection;
    private ImageIcon icon = new ImageIcon(getClass().getResource("images/bg.jpg"));
    
    /** Creates new form XMPPClient */
    public XMPPClient() 
    {
        XMPPConnection.DEBUG_ENABLED = false;
        
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.out.println("Unable to use system look and feel");
        }
        
        initComponents();
        
        Connection defaultConnection = new Connection();
        storedConnectionComboBox.addItem(defaultConnection);
        storedConnectionComboBox.setSelectedItem(defaultConnection);
        usernameTextField.requestFocus();
        validateInputs();
        getRootPane().setDefaultButton(signInButton);
    }

    /**
     * Gets the XMPPConnection instance
     * @return The XMPPConnection
     */
    public XMPPConnection getXMPPConnection()
    {
        return XMPPConnection;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerPanel = new javax.swing.JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                //  Dispaly image at at full size
                //g.drawImage(icon.getImage(), 0, 0, null);

                //  Scale image to size of component
                //Dimension d = getSize();
                //g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);

                //  Fix the image position in the scroll pane
                //Point p = scrollPane.getViewport().getViewPosition();
                //g.drawImage(icon.getImage(), p.x, p.y, null);

                super.paintComponent(g);
            }
        };//);
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        storedConnectionComboBox = new javax.swing.JComboBox(Utils.getConnections());
        usernameTextField = new javax.swing.JTextField();
        resourceTextField = new javax.swing.JTextField();
        hostTextField = new javax.swing.JTextField();
        storeConnectionCheckBox = new javax.swing.JCheckBox();
        passwordTextField = new javax.swing.JPasswordField();
        portTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        signInButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("XMPPClient");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setName("XMPPClientFrame"); // NOI18N
        setResizable(false);

        containerPanel.setOpaque(false);

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        jLabel4.setText("Resource");

        jLabel5.setText("Host");

        storedConnectionComboBox.setFont(storedConnectionComboBox.getFont());
        storedConnectionComboBox.setToolTipText("Select a previously stored connection, or select new connection to create a new connection");
        storedConnectionComboBox.setBorder(null);
        storedConnectionComboBox.setFocusable(false);
        storedConnectionComboBox.setOpaque(false);
        storedConnectionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storedConnectionComboBoxActionPerformed(evt);
            }
        });

        usernameTextField.setToolTipText("<html>\n<p>Enter your username for the XMPP connection</p>\n</html>");
        usernameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usernameTextFieldKeyReleased(evt);
            }
        });

        resourceTextField.setText("Home");
        resourceTextField.setToolTipText("Enter the resource, or leave as default");

        hostTextField.setToolTipText("Enter the server IP address or host name");
        hostTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hostTextFieldKeyReleased(evt);
            }
        });

        storeConnectionCheckBox.setFont(new java.awt.Font("Tahoma", 0, 10));
        storeConnectionCheckBox.setText("Save connection");
        storeConnectionCheckBox.setToolTipText("Tick this box to store this connection for future use");
        storeConnectionCheckBox.setBorder(null);
        storeConnectionCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        storeConnectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeConnectionCheckBoxActionPerformed(evt);
            }
        });

        passwordTextField.setText("password");
        passwordTextField.setToolTipText("Enter your password for the XMPP connection");
        passwordTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordTextFieldKeyReleased(evt);
            }
        });

        portTextField.setText("5222");
        portTextField.setToolTipText("Set the port number or leave as default");
        portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                portTextFieldKeyReleased(evt);
            }
        });

        jLabel7.setText("Port");

        jLabel8.setText("Name:");

        nameTextField.setToolTipText("<html>\n<p>Give a name to the connection. This name will be used in the connection drop down box.</p>\n</html>");
        nameTextField.setEnabled(false);

        jLabel1.setText("Connection");

        signInButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/go-next.png"))); // NOI18N
        signInButton.setText("Sign in");
        signInButton.setEnabled(false);
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addComponent(hostTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(resourceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(storedConnectionComboBox, 0, 185, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                .addContainerGap(170, Short.MAX_VALUE)
                .addComponent(signInButton)
                .addGap(10, 10, 10))
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(storeConnectionCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        containerPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5});

        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(storedConnectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(resourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(hostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signInButton)
                .addGap(70, 70, 70)
                .addComponent(storeConnectionCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        containerPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, nameTextField, storeConnectionCheckBox});

        jMenuBar1.setBorderPainted(false);

        jMenu1.setText("File");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Register");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/system-shutdown-22x22.png"))); // NOI18N
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/network-server-22x22.png"))); // NOI18N
        jMenuItem2.setText("Account manager");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void storedConnectionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storedConnectionComboBoxActionPerformed
    Connection connection = (Connection) storedConnectionComboBox.getSelectedItem();
    
    usernameTextField.setText(connection.getUsername());
    resourceTextField.setText(connection.getResource());
    hostTextField.setText(connection.getHost());
    portTextField.setText(connection.getPort());
    nameTextField.setText(connection.getName());
    validateInputs();
}//GEN-LAST:event_storedConnectionComboBoxActionPerformed

private void usernameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameTextFieldKeyReleased
    validateInputs();
}//GEN-LAST:event_usernameTextFieldKeyReleased

private void hostTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostTextFieldKeyReleased
    validateInputs();
}//GEN-LAST:event_hostTextFieldKeyReleased

private void storeConnectionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeConnectionCheckBoxActionPerformed
    if(storeConnectionCheckBox.isSelected()) 
    {
        nameTextField.setEnabled(true);
        nameTextField.requestFocus();
        nameTextField.selectAll();
    }
    else nameTextField.setEnabled(false);
}//GEN-LAST:event_storeConnectionCheckBoxActionPerformed

private void passwordTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordTextFieldKeyReleased
    validateInputs();
}//GEN-LAST:event_passwordTextFieldKeyReleased

private void portTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portTextFieldKeyReleased
    validateInputs();
}//GEN-LAST:event_portTextFieldKeyReleased

private void signInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInButtonActionPerformed
    enableComponents(false);
    (new ConnectSwingWorker()).execute();
}//GEN-LAST:event_signInButtonActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    new AccountManagerUI().setVisible(true);
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    dispose();
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    new RegistrationUI(this).setVisible(true);
}//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new XMPPClient().setVisible(true);
            }
        });
    }
    
    private void storeConnection(boolean store) throws Exception
    {
        if(!store) return;
        
        Utils.saveConnection(usernameTextField.getText(),
                resourceTextField.getText(),
                hostTextField.getText(),
                portTextField.getText(),
                nameTextField.getText());
    }
    
    private void validateInputs()
    {
        signInButton.setEnabled(false);
        
        // validate lengths
        if(usernameTextField.getText().equals("")) return;
        if(passwordTextField.getPassword().length == 0) return;
        if(portTextField.getText().equals("")) return;
        if(hostTextField.getText().equals("")) return;
        
        try
        {
            Integer.parseInt(portTextField.getText());
        }
        catch(NumberFormatException ex)
        {
            return;
        }
        
        signInButton.setEnabled(true);
    }
    
    private void enableComponents(boolean enable)
    {
        storedConnectionComboBox.setEnabled(enable);
        usernameTextField.setEnabled(enable);
        resourceTextField.setEnabled(enable);
        portTextField.setEnabled(enable);
        hostTextField.setEnabled(enable);
        storeConnectionCheckBox.setEnabled(enable);
        passwordTextField.setEnabled(enable);
        signInButton.setEnabled(enable);
        
        if(enable) signInButton.setText("Sign in");
        else signInButton.setText("Connecting");
    }
    
    private class ConnectSwingWorker extends SwingWorker<XMPPConnection, Object>
    {
        @Override
        protected XMPPConnection doInBackground() throws Exception
        {
            ConnectionConfiguration config = new ConnectionConfiguration(
                hostTextField.getText(), 
                Integer.parseInt(portTextField.getText()));
            
            XMPPConnection connection = new XMPPConnection(config);
            
            connection.connect();
            connection.login(usernameTextField.getText(),
                    new String(passwordTextField.getPassword()),
                    resourceTextField.getText());
            
            storeConnection(storeConnectionCheckBox.isSelected());
            
            return connection;
        }

        @Override
        protected void done()
        {
            try
            {
                new XMPPClientUI(get(), nameTextField.getText()).setVisible(true);
                XMPPClient.this.dispose();
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(XMPPClient.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                enableComponents(true);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel containerPanel;
    private javax.swing.JTextField hostTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPasswordField passwordTextField;
    private javax.swing.JTextField portTextField;
    private javax.swing.JTextField resourceTextField;
    private javax.swing.JButton signInButton;
    private javax.swing.JCheckBox storeConnectionCheckBox;
    private javax.swing.JComboBox storedConnectionComboBox;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
