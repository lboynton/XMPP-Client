package xmppclient.jingle;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.util.StringUtils;
import xmppclient.jingle.packet.Description;
import xmppclient.jingle.packet.Jingle;

/**
 *
 * @author Lee Boynton (323326)
 */
public class OutgoingSession extends Session
{
    private File file;
    private InputStream in;
    private DataOutputStream out;
    private Socket connection;
    private ServerSocket serverSocket;
    private byte[] buffer;

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
        catch(SocketException ex)
        {
            System.out.println("Peer closed socket");
            try
            {
                out.close();
                in.close();
            }
            catch (IOException ex1)
            {
                Logger.getLogger(OutgoingSession.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(OutgoingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void terminate()
    {
        try
        {
            in.close();
            out.close();
            connection.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(OutgoingSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
