/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle;

import xmppclient.jingle.packet.Jingle;

/**
 *
 * @author Lee Boynton (323326)
 */
public class JingleSessionRequest
{
    private final Jingle jingle;
    private final JingleManager manager;

    public JingleSessionRequest(JingleManager manager, Jingle jingle)
    {
        this.manager = manager;
        this.jingle = jingle;
    }
    
    public String getSid()
    {
        return jingle.getSid();
    }
    
    public String getFrom()
    {
        return jingle.getFrom();
    }
    
    public String getTo()
    {
        return jingle.getTo();
    }

    public Jingle getJingle()
    {
        return jingle;
    }
    
    public IncomingSession accept()
    {
        return manager.createIncomingSession(this);
    }
    
    public void reject()
    {
        
    }
}
