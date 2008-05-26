/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import javax.swing.Icon;
import xmppclient.images.Icons;

/**
 * Extends the Presence class in Smack to provide extra information such as 
 * the status icon
 * @author Lee Boynton (323326)
 * @see org.jivesoftware.smack.packet.Presence 
 */
public class Presence extends org.jivesoftware.smack.packet.Presence 
{   
    /**
     * Constructor creates a new presence
     * @param type The presence type
     * @param mode The presence mode
     * @param status The presence status
     */
    public Presence(Presence.Type type, Presence.Mode mode, String status)
    {
        super(type);
        setMode(mode);
        setStatus(status);
    }
    
    /**
     * Default presence is offline
     */
    public Presence()
    {
        super(Presence.Type.unavailable);
        setStatus("Offline");
    }

    /**
     * Gets the icon representing this presence depending on the presence type 
     * and presence mode
     * @return
     */
    public Icon getIcon()
    {
        if(getType().equals(Presence.Type.available) && getMode().equals(Presence.Mode.available))
        {
            return Icons.online;
        }
        if(getType().equals(Presence.Type.available) && getMode().equals(Presence.Mode.away))
        {
            return Icons.away;
        }
        if(getType().equals(Presence.Type.available) && getMode().equals(Presence.Mode.xa))
        {
            return Icons.away;
        }
        if(getType().equals(Presence.Type.available) && getMode().equals(Presence.Mode.dnd))
        {
            return Icons.busy;
        }
        if(getType().equals(Presence.Type.available) && getMode().equals(Presence.Mode.chat))
        {
            return Icons.online;
        }
        
        return Icons.offline;
    }
    
    /**
     * The toString() method displays the status message
     * @return The status message of this presence
     */
    @Override
    public String toString()
    {
        return super.getStatus();
    }
}
