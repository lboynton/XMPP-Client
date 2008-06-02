/*
 * PrivacyListUI.java
 *
 * Created on 24 May 2008, 14:22
 */
package xmppclient;

import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Incomplete dialog for adding entries to a privacy list.
 * @author  Lee Boynton (323326)
 */
public class PrivacyListUI extends javax.swing.JDialog
{
    private XMPPConnection connection;
    
    /**
     * Creates new form PrivacyListUI
     * @param parent The parent JFrame
     * @param connection The XMPP connection to the server which the privacy
     * lists will be retrieved from
     */
    public PrivacyListUI(JFrame parent, XMPPConnection connection)
    {
        super(parent, "New Privacy List", true);
        this.connection = connection;
        initComponents();
    }
    
    private void updateDomains()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        model.addElement("<any domain>");
        
        Collection<RosterEntry> entries = connection.getRoster().getEntries();
        
        for(RosterEntry entry:entries)
        {
            String domain = StringUtils.parseServer(entry.getUser());
            
            System.out.println(model.getIndexOf(domain));
            if(model.getIndexOf(domain) < 0) model.addElement(domain);
        }
        
        domainComboBox.setModel(model);
    }
    
    private void updateUsernames()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        model.addElement("<any username>");
        
        Collection<RosterEntry> entries = connection.getRoster().getEntries();
        
        for(RosterEntry entry:entries)
        {
            String username = StringUtils.parseName(entry.getUser());
            
            System.out.println(model.getIndexOf(username));
            if(model.getIndexOf(username) < 0) model.addElement(username);
        }
        
        usernameComboBox.setModel(model);
    }
    
    private void updateResources()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        model.addElement("<any resource>");
        
        Collection<RosterEntry> entries = connection.getRoster().getEntries();
        
        for(RosterEntry entry:entries)
        {
            String resource = StringUtils.parseResource(entry.getUser());
            
            System.out.println(model.getIndexOf(resource));
            if(model.getIndexOf(resource) < 0) model.addElement(resource);
        }
        
        resourceComboBox.setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        domainComboBox = new javax.swing.JComboBox();
        usernameComboBox = new javax.swing.JComboBox();
        resourceComboBox = new javax.swing.JComboBox();

        updateDomains();
        domainComboBox.setEditable(true);

        updateUsernames();
        usernameComboBox.setEditable(true);

        updateResources();
        resourceComboBox.setEditable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(domainComboBox, 0, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameComboBox, 0, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resourceComboBox, 0, 119, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(domainComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(146, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox domainComboBox;
    private javax.swing.JComboBox resourceComboBox;
    private javax.swing.JComboBox usernameComboBox;
    // End of variables declaration//GEN-END:variables
}
