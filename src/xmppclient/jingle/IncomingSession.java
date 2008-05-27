package xmppclient.jingle;

import java.io.BufferedInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.Bytestream;
import xmppclient.jingle.packet.File;
import xmppclient.jingle.packet.Jingle;

/**
 * An incoming file transfer jingle session
 * @author Lee Boynton (323326)
 */
public class IncomingSession extends Session implements PacketListener
{
    private Socket socket;
    private BufferedInputStream bis;
    private String host;
    private int port;
    private int received = 0;
    private File file;
    private BasicPlayer player;
    private BasicController control;

    public BasicController getControl()
    {
        return control;
    }

    public BasicPlayer getPlayer()
    {
        return player;
    }

    public IncomingSession(XMPPConnection xmppConnection, String responder, String sid)
    {
        super(xmppConnection, responder, sid);
        xmppConnection.addPacketListener(this, new PacketTypeFilter(Jingle.class));
        xmppConnection.addPacketListener(new BytestreamListener(), new PacketTypeFilter(Bytestream.class));
        player = new BasicPlayer();
        control = (BasicController) player;
    }

    public File getFile()
    {
        return file;
    }

    @Override
    public void start()
    {
        super.status = "Connecting...";

        System.out.printf("Attempting to connect to %s:%s\n", host, port);
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 0);
            super.status = "Connected";
            super.connected = true;
            bis = new BufferedInputStream(socket.getInputStream());
            control.open(bis);
            control.play();
            socket.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(IncomingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void processPacket(Packet packet)
    {
        System.out.println("Incoming session: Jingle packet received");
        Jingle jingle = (Jingle) packet;
        file = (File) jingle.getDescription().getType();
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
