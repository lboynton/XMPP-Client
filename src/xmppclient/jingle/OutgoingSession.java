/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.Bytestream;
import xmppclient.jingle.packet.Description;
import xmppclient.jingle.packet.Jingle;

/**
 *
 * @author Lee Boynton (323326)
 */
public class OutgoingSession extends Session implements PacketListener
{
    private File file;
    private InputStream in;
    private DataOutputStream out;
    private Socket connection;
    private ServerSocket serverSocket;
    private byte[] buffer;
    private int port;

    public OutgoingSession(XMPPConnection xmppConnection, String responder, java.io.File file)
    {
        super(xmppConnection, responder);
        this.file = file;
        Jingle jingle = new Jingle();
        jingle.setFrom(xmppConnection.getUser());
        jingle.setTo(responder);
        jingle.setInitiator(xmppConnection.getUser());
        jingle.setResponder(responder);
        jingle.setSid(StringUtils.randomString(5));
        jingle.setAction(Jingle.Action.SESSIONINITIATE);
        jingle.setContent(Jingle.Content.FILEOFFER);
        jingle.setDescription(new Description(
                new xmppclient.jingle.packet.File(file.getName(),
                String.valueOf(file.length()),
                String.valueOf(file.hashCode()))));
        xmppConnection.sendPacket(jingle);

        xmppConnection.addPacketListener(this, new PacketTypeFilter(Jingle.class));
    }

    @Override
    public void start()
    {
        System.out.println("Starting");
        InetSocketAddress addr = new InetSocketAddress(getHostAddress(), port);
        
        try
        {
            buffer = new byte[new Long(file.length()).intValue()];
            serverSocket = new ServerSocket();
            serverSocket.bind(addr);
            System.out.printf("Listening for connections on: %s:%s",
                    serverSocket.getInetAddress().getHostAddress(),
                    serverSocket.getLocalPort());
            connection = serverSocket.accept();
            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedInputStream(new FileInputStream(file));
            in.read(buffer);
            out.write(buffer);
            out.flush();
            out.close();
            in.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(OutgoingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void processPacket(Packet packet)
    {
        Jingle jingle = (Jingle) packet;
        System.out.println("Outgoing session: Jingle packet received");

        if (jingle.getAction() == Jingle.Action.SESSIONACCEPT)
        {
            port = getFreePort();
            Bytestream bytestream = new Bytestream();
            bytestream.addStreamHost(super.getXmppConnection().getUser(), getHostAddress(), port);
            bytestream.setTo(jingle.getFrom());
            bytestream.setFrom(jingle.getTo());
            bytestream.setMode(Bytestream.Mode.tcp);
            bytestream.setType(IQ.Type.SET);
            super.getXmppConnection().sendPacket(bytestream);
            start();
        }
    }
}
