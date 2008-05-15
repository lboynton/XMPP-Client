/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import java.util.Collection;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Lee Boynton (323326)
 */
public class ContactListListener implements org.jivesoftware.smack.RosterListener 
{
    private XMPPClientUI clientUI;
    
    public ContactListListener(XMPPClientUI clientUI)
    {
        this.clientUI = clientUI;
    }
    
    public void entriesAdded(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries added");
    }

    public void entriesUpdated(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries updated");
    }

    public void entriesDeleted(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries deleted");
    }

    public void presenceChanged(Presence arg0)
    {
        clientUI.updateContacts();
        System.out.println("Presence changed");
    }

}
