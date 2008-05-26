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
    private ContactListUI clientUI;
    
    /**
     * Listens for changes to the roster and updates the contact list accordingly
     * @param clientUI
     */
    public ContactListListener(ContactListUI clientUI)
    {
        this.clientUI = clientUI;
    }
    
    @Override
    public void entriesAdded(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries added");
    }

    @Override
    public void entriesUpdated(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries updated");
    }

    @Override
    public void entriesDeleted(Collection<String> arg0)
    {
        clientUI.updateContacts();
        System.out.println("Entries deleted");
    }

    @Override
    public void presenceChanged(Presence arg0)
    {
        clientUI.updateContacts();
        System.out.println("Presence changed");
    }

}
