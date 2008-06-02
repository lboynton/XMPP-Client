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
    /**
     * The XMPP connection that will be used to send control informtion, such as
     * requests and responses
     */
    protected XMPPConnection connection;
    private String responder;
    private String sid;
    /**
     * This should be set by subclasses to indicate if the session has completed
     * transmission
     */
    protected boolean complete = false;
    /**
     * This should be set by subclasses to indicate if the session is connected
     * to a remote user
     */
    protected boolean connected = false;
    /**
     * This should be set by subclasses to indicate the status of the session
     * in textual form
     */
    protected String status = "Not started";
    /**
     * This should be set by the outgoing session subclass. It is the port the
     * outgoing session listens on.
     */
    protected int port;

    /**
     * Sets the session parameters
     * @param connection The XMPP connection the session will take place over
     * @param responder The remote user who should respond to sessions
     */
    public Session(XMPPConnection connection, String responder)
    {
        this.connection = connection;
        this.responder = responder;
    }

    /**
     * Sets the session parameters
     * @param connection The XMPP connection the session will take place over
     * @param responder The remote user who should respond to sessions
     * @param sid The id identifying this session
     */
    public Session(XMPPConnection connection, String responder, String sid)
    {
        this.connection = connection;
        this.responder = responder;
        this.sid = sid;
    }

    /**
     * Called when the session should start connecting to the remote user
     */
    public abstract void start();

    /**
     * Called when the session should terminate the connection to the remote user
     */
    public abstract void terminate();
    
    /**
     * Sends a session terminate to the remote user. Not actually used currently.
     */
    public void sendTerminate()
    {
        Jingle terminate = new Jingle(Jingle.Action.SESSIONTERMINATE);
        terminate.setTo(responder);
        terminate.setFrom(connection.getUser());
        terminate.setSid(sid);
        connection.sendPacket(terminate);
    }

    /**
     * Used to determine if the session has completed transferring
     * @return True if completed transferring, false otherwise
     */
    public boolean isComplete()
    {
        return complete;
    }

    /**
     * Used to determine if the session has connected to the remote user
     * @return True if connected to remote user, false otherwise
     */
    public boolean isConnected()
    {
        return connected;
    }

    /**
     * Gets the textual message indicating the status of the file transfer
     * @return The status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Gets the JID of the remote user
     * @return The JID
     */
    public String getResponder()
    {
        return responder;
    }

    /**
     * Gets the ID of this session
     * @return The ID
     */
    public String getSid()
    {
        return sid;
    }

    /**
     * Gets the XMPP connection being used in this session
     * @return The XMPP connection
     */
    public XMPPConnection getConnection()
    {
        return connection;
    }

    /**
     * Gets a free port by creating a new server socket using any free port, 
     * getting the port from the socket and closing the socket.
     * @return The free port
     */
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
    
    /**
     * Gets the host address of this computer. If this is running on Linux, this
     * may return the localhost address if the /etc/hosts file is not set up correctly.
     * Java resolves the IP address of the hostname, therefore the first entry
     * for the hostname in the hosts file must be a public address.
     * @return The host address
     */
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
