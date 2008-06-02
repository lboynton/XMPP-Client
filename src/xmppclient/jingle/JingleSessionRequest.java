package xmppclient.jingle;

import xmppclient.jingle.packet.Jingle;

/**
 * A wrapper for jingle file transfer session requests. Contains the jingle
 * packet that was sent, and the jingle manager that received the request.
 * @author Lee Boynton (323326)
 */
public class JingleSessionRequest
{
    private final Jingle jingle;
    private final JingleManager manager;

    /**
     * 
     * @param manager
     * @param jingle
     */
    public JingleSessionRequest(JingleManager manager, Jingle jingle)
    {
        this.manager = manager;
        this.jingle = jingle;
    }
    
    /**
     * Gets the session ID
     * @return The session ID
     */
    public String getSid()
    {
        return jingle.getSid();
    }
    
    /**
     * Gets the JID of the user who sent the request. This should be a fully
     * qualified JID.
     * @return The JID
     */
    public String getFrom()
    {
        return jingle.getFrom();
    }
    
    /**
     * Gets the JID of the user who the request was sent to.
     * @return The JID
     */
    public String getTo()
    {
        return jingle.getTo();
    }

    /**
     * Gets the Jingle request packet that was sent
     * @return The Jingle packet
     */
    public Jingle getJingle()
    {
        return jingle;
    }
    
    /**
     * Accepts the session request
     * @return An incoming file transfer session
     */
    public IncomingSession accept()
    {
        return manager.createIncomingSession(this);
    }
    
    /**
     * Not implemented yet
     */
    public void reject()
    {
        
    }
}
