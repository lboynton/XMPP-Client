package xmppclient.jingle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.Bytestream;
import xmppclient.jingle.packet.Jingle;

/**
 *
 * @author Lee Boynton (323326)
 */
public abstract class Session implements PacketListener
{
    private XMPPConnection xmppConnection;
    private String responder;
    private String sid;
    protected boolean complete = false;
    protected boolean connected = false;
    protected String status = "Not started";
    protected int port;

    public Session(XMPPConnection xmppConnection, String responder)
    {
        this.xmppConnection = xmppConnection;
        this.responder = responder;
        xmppConnection.addPacketListener(this, new PacketTypeFilter(Jingle.class));
    }

    public Session(XMPPConnection xmppConnection, String responder, String sid)
    {
        this.xmppConnection = xmppConnection;
        this.responder = responder;
        this.sid = sid;
        xmppConnection.addPacketListener(this, new PacketTypeFilter(Jingle.class));
    }

    public abstract void start();

    public abstract void terminate();
    
    public void sendTerminate()
    {
        Jingle terminate = new Jingle(Jingle.Action.SESSIONTERMINATE);
        terminate.setTo(responder);
        terminate.setFrom(xmppConnection.getUser());
        terminate.setSid(sid);
        xmppConnection.sendPacket(terminate);
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

    public XMPPConnection getXmppConnection()
    {
        return xmppConnection;
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
    
    @Override
    public void processPacket(Packet packet)
    {
        Jingle jingle = (Jingle) packet;
        System.out.printf("%s: Jingle packet received\n", this.getClass().getName());

        // sending a session accept triggers this listener on the sender for some reason
        if (jingle.getAction() == Jingle.Action.SESSIONACCEPT && !jingle.getFrom().equals(xmppConnection.getUser()))
        {
            port = getFreePort();
            Bytestream bytestream = new Bytestream();
            bytestream.addStreamHost(xmppConnection.getUser(), getHostAddress(), port);
            bytestream.setTo(jingle.getFrom());
            bytestream.setFrom(jingle.getTo());
            bytestream.setMode(Bytestream.Mode.tcp);
            bytestream.setType(IQ.Type.SET);
            xmppConnection.sendPacket(bytestream);
            start();
        }
        
        if(jingle.getAction() == Jingle.Action.SESSIONTERMINATE)
        {
            terminate();
        }
    }
}
