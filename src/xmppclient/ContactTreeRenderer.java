/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Lee Boynton (323326)
 */
public class ContactTreeRenderer extends DefaultTreeCellRenderer
{
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        
        if(node.getUserObject() instanceof RosterEntry)
        {
            final RosterEntry entry = (RosterEntry) node.getUserObject();
            Presence presence = XMPPClientUI.connection.getRoster().getPresence(entry.getUser());
            JLabel lbl = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            lbl.setIcon(Utils.getUserIcon(XMPPClientUI.connection.getRoster().getPresence(entry.getUser())));
            lbl.setText(Utils.getNickname(entry));
            if(presence.getStatus() != null) lbl.setText(lbl.getText() + " (" + presence.getStatus() + ")");
            return lbl;
        }
        
        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }
}
