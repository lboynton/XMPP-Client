/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Lee Boynton (323326)
 */
public class BusyPresence extends Presence
{
    public BusyPresence()
    {
        super(Presence.Type.available);
        setMode(Presence.Mode.dnd);
        setStatus("Busy");
    }
    
    @Override
    public String toString()
    {
        return("Busy");
    } 
}
