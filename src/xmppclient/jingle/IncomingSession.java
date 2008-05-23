/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.player.Player;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.Bytestream;
import xmppclient.jingle.packet.Jingle;

/**
 *
 * @author Lee Boynton (323326)
 */
public class IncomingSession extends Session implements PacketListener
{
    private DataInputStream in;
    private Socket socket;
    private BufferedInputStream bis;
    private Player player;
    private String host;
    private int port;

    public IncomingSession(XMPPConnection xmppConnection, String responder, String sid)
    {
        super(xmppConnection, responder, sid);
        xmppConnection.addPacketListener(this, new PacketTypeFilter(Jingle.class));
        xmppConnection.addPacketListener(new BytestreamListener(), new PacketTypeFilter(Bytestream.class));
    }

    @Override
    public void start()
    {
        System.out.printf("Attempting to connect to %s:%s\n", host, port);
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 0);
            in = new DataInputStream(socket.getInputStream());
            bis = new BufferedInputStream(in);
            player = new Player(bis);
            player.play();

            in.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(IncomingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void processPacket(Packet packet)
    {
        Jingle jingle = (Jingle) packet;
        System.out.println("Incoming session: Jingle packet received");
    }

    public class BytestreamListener implements PacketListener
    {
        @Override
        public void processPacket(Packet packet)
        {
            Bytestream bytestream = (Bytestream) packet;
            System.out.println("Incoming session: Bytestream packet received");
            host = bytestream.getStreamHost(bytestream.getFrom()).getAddress();
            port = bytestream.getStreamHost(bytestream.getFrom()).getPort();
            start();
        }
    }
}
