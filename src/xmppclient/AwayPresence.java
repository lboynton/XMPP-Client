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
public class AwayPresence extends Presence
{
    public AwayPresence()
    {
        super(Presence.Type.unavailable);
        setStatus("Away");
    }
    
    @Override
    public String toString()
    {
        return("Away");
    } 
}
