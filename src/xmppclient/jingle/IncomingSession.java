package xmppclient.jingle;

import java.io.BufferedInputStream;
import java.io.IOException;
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

/**
 * An incoming file transfer jingle session
 * @author Lee Boynton (323326)
 */
public class IncomingSession extends Session
{
    private Socket socket;
    private BufferedInputStream bis;
    private String host;
    private int received = 0;
    private File file;
    private BasicPlayer player;
    private BasicController control;
    private BytestreamListener listener;

    public BasicController getControl()
    {
        return control;
    }

    public BasicPlayer getPlayer()
    {
        return player;
    }

    public IncomingSession(XMPPConnection connection, String responder, String sid)
    {
        super(connection, responder, sid);
        listener = new BytestreamListener();
        connection.addPacketListener(listener, new PacketTypeFilter(Bytestream.class));
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

        System.out.printf("Attempting to connect to %s:%s\n", host, super.port);
        try
        {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, super.port), 0);
            super.status = "Connected";
            super.connected = true;
            bis = new BufferedInputStream(socket.getInputStream());
            control.open(bis);
            control.play();
        }
        catch (Exception ex)
        {
            Logger.getLogger(IncomingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class BytestreamListener implements PacketListener
    {
        @Override
        public void processPacket(Packet packet)
        {
            Bytestream bytestream = (Bytestream) packet;
            System.out.println("Incoming session: Bytestream packet received");
            host = bytestream.getStreamHost(bytestream.getFrom()).getAddress();
            IncomingSession.super.port = bytestream.getStreamHost(bytestream.getFrom()).getPort();
            start();
        }
    }

    @Override
    public void terminate()
    {
        super.connection.removePacketListener(listener);
        
        try
        {
            bis.close();
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(IncomingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
