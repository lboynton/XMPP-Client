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
public class OnlinePresence extends Presence 
{
    public OnlinePresence()
    {
        super(Presence.Type.available);
        setStatus("Online");
    }
    
    @Override
    public String toString()
    {
        return("Online");
    }
}
