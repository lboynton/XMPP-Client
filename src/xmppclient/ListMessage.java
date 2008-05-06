/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import org.jivesoftware.smack.packet.Message;

/**
 *
 * @author Lee Boynton (323326)
 */
public class ListMessage
{
    private String name;
    private Message message;

    public ListMessage(String name, Message message)
    {
        this.name = name;
        this.message = message;
    }

    public String getName()
    {
        return name;
    }
    
    public Message getMessage()
    {
        return message;
    }
    
    @Override
    public String toString()
    {
        return name + ": " + message.getBody();
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
