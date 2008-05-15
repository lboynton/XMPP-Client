/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import javax.swing.Icon;
import xmppclient.images.Icons;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Presence extends org.jivesoftware.smack.packet.Presence 
{   
    public Presence(Presence.Type type, Presence.Mode mode, String status)
    {
        super(type);
        setMode(mode);
        setStatus(status);
    }
    
    public Presence()
    {
        super(Presence.Type.unavailable);
        setStatus("Offline");
    }

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
    
    @Override
    public String toString()
    {
        return super.getStatus();
    }
}
