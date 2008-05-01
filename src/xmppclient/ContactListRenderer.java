/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Lee
 */
public class ContactListRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list, 
            Object object, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus)
    {       
        if(XMPPClientUI.connection == null || !XMPPClientUI.connection.isConnected()) return null;
        
        RosterEntry rosterEntry = (RosterEntry)object;
        Presence presence = XMPPClientUI.connection.getRoster().getPresence(rosterEntry.getUser());
        
        if(list.getSelectedIndex() == index) return new SelectedContactListItem(rosterEntry, presence);
        
        return new ContactListItem(rosterEntry, presence);
    }
}

