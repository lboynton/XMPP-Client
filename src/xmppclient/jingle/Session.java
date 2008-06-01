package xmppclient.jingle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;
import xmppclient.jingle.packet.Jingle;

/**
 * Base class for Jingle file transfer sessions
 * Based on XEP-0234: Jingle File Transfer - http://www.xmpp.org/extensions/xep-0234.html
 * @author Lee Boynton (323326)
 */
public abstract class Session
{
    protected XMPPConnection connection;
    private String responder;
    private String sid;
    protected boolean complete = false;
    protected boolean connected = false;
    protected String status = "Not started";
    protected int port;

    public Session(XMPPConnection connection, String responder)
    {
        this.connection = connection;
        this.responder = responder;
    }

    public Session(XMPPConnection connection, String responder, String sid)
    {
        this.connection = connection;
        this.responder = responder;
        this.sid = sid;
    }

    public abstract void start();

    public abstract void terminate();
    
    public void sendTerminate()
    {
        Jingle terminate = new Jingle(Jingle.Action.SESSIONTERMINATE);
        terminate.setTo(responder);
        terminate.setFrom(connection.getUser());
        terminate.setSid(sid);
        connection.sendPacket(terminate);
    }

    public boolean isComplete()
    {
        return complete;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public String getStatus()
    {
        return status;
    }

    public String getResponder()
    {
        return responder;
    }

    public String getSid()
    {
        return sid;
    }

    public XMPPConnection getConnection()
    {
        return connection;
    }

    public int getFreePort()
    {
        int freePort = 0;
        
        try
        {
            ServerSocket socket = new ServerSocket(0);
            freePort = socket.getLocalPort();
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(JingleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return freePort;
    }
    
    public String getHostAddress()
    {
        String address = null;
        
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            address = addr.getHostAddress();
        }
        catch (UnknownHostException ex)
        {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return address;
    }
}
