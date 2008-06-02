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

/**
 * An incoming file transfer jingle session.
 * Based on XEP-0234: Jingle File Transfer - http://www.xmpp.org/extensions/xep-0234.html
 * @author Lee Boynton (323326)
 */
public class IncomingSession extends Session
{
    private Socket socket;
    private BufferedInputStream bis;
    private String host;
    private int received = 0;
    private BasicPlayer player;
    private BasicController control;
    private BytestreamListener listener;

    /**
     * Return the control for the audio player. This can play, pause, stop, etc the
     * playback
     * @return The controller
     */
    public BasicController getControl()
    {
        return control;
    }

    /**
     * Get the audio player. This can be used for adding status listeners to the
     * player, getting the status of the player, or changing the volume of the player
     * @return The audio player
     */
    public BasicPlayer getPlayer()
    {
        return player;
    }

    /**
     * Creates a new incoming Jingle file transfer session. Sets the parameters,
     * adds a bytestream listener, and initialises the audio player.
     * @param connection The XMPP connection to use
     * @param responder The user who will be sending
     * @param sid The ID identifying this session
     */
    public IncomingSession(XMPPConnection connection, String responder, String sid)
    {
        super(connection, responder, sid);
        listener = new BytestreamListener();
        connection.addPacketListener(listener, new PacketTypeFilter(Bytestream.class));
        player = new BasicPlayer();
        control = (BasicController) player;
    }

    /**
     * Attempts to connect to the remtoe user using the host address and port
     * received in the bytestream packet.
     */
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

    /**
     * Listens for bytestream packets, which inform the receiver of the address
     * and port to connect to. When receiving a bytestream packet this will call
     * #start()
     */
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

    /**
     * Removes the bytestream listener and closes the socket connection and
     * input stream
     */
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
