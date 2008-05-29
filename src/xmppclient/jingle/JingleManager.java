/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import xmppclient.jingle.packet.Jingle;
import xmppclient.jingle.provider.JingleProvider;
import xmppclient.jingle.packet.File;
import xmppclient.jingle.packet.Description;
import xmppclient.jingle.provider.DescriptionProvider;
import xmppclient.jingle.provider.FileProvider;

/**
 *
 * @author Lee Boynton (323326)
 */
public class JingleManager
{
    private XMPPConnection connection;
    private List<JingleSessionRequestListener> sessionRequestListeners;

    public JingleManager(XMPPConnection connection)
    {
        this.connection = connection;
    }
    

    static
    {
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addIQProvider(Jingle.ELEMENTNAME, Jingle.NAMESPACE, new JingleProvider());
        providerManager.addExtensionProvider(Description.ELEMENTNAME, Description.NAMESPACE, new DescriptionProvider());
        providerManager.addExtensionProvider(File.ELEMENTNAME, File.NAMESPACE, new FileProvider());
        providerManager.addIQProvider(Bytestream.StreamHost.ELEMENTNAME, Bytestream.StreamHost.NAMESPACE, new BytestreamsProvider());

        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
        {
            @Override
            public void connectionCreated(XMPPConnection connection)
            {
                JingleManager.setServiceEnabled(connection, true);
            }
        });
    }

    public synchronized static void setServiceEnabled(XMPPConnection connection, boolean enabled)
    {
        if (isServiceEnabled(connection) == enabled)
        {
            return;
        }

        if (enabled)
        {
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Jingle.NAMESPACE);
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Bytestream.StreamHost.NAMESPACE);
        }
        else
        {
            ServiceDiscoveryManager.getInstanceFor(connection).removeFeature(Jingle.NAMESPACE);
            ServiceDiscoveryManager.getInstanceFor(connection).removeFeature(Bytestream.StreamHost.NAMESPACE);
        }
    }

    public XMPPConnection getConnection()
    {
        return connection;
    }

    public static boolean isServiceEnabled(XMPPConnection connection)
    {
        return ServiceDiscoveryManager.getInstanceFor(connection).includesFeature(
                Jingle.NAMESPACE);
    }

    public void addSessionRequestListener(JingleSessionRequestListener sessionRequestListener)
    {
        if (sessionRequestListener != null)
        {
            if (sessionRequestListeners == null)
            {
                initSessionRequestListeners();
            }
            synchronized (sessionRequestListeners)
            {
                sessionRequestListeners.add(sessionRequestListener);
            }
        }
    }

    private void triggerSessionCreated(JingleSessionRequest request)
    {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void triggerSessionRequested(Jingle initJin)
    {
        JingleSessionRequestListener[] sessionRequestListeners = null;

        // Make a synchronized copy of the listenerJingles
        synchronized (this.sessionRequestListeners)
        {
            sessionRequestListeners = new JingleSessionRequestListener[this.sessionRequestListeners.size()];
            this.sessionRequestListeners.toArray(sessionRequestListeners);
        }

        // ... and let them know of the event
        JingleSessionRequest request = new JingleSessionRequest(this, initJin);
        for (int i = 0; i < sessionRequestListeners.length; i++)
        {
            sessionRequestListeners[i].sessionRequested(request);
        }
    }

    private void initSessionRequestListeners()
    {
        PacketFilter initRequestFilter = new PacketFilter()
        {
            // Return true if we accept this packet
            @Override
            public boolean accept(Packet packet)
            {
                if (packet instanceof IQ)
                {
                    IQ iq = (IQ) packet;
                    if (iq.getType().equals(IQ.Type.SET))
                    {
                        if (iq instanceof Jingle)
                        {
                            Jingle jin = (Jingle) packet;
                            if (jin.getAction().equals(Jingle.Action.SESSIONINITIATE))
                            {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };

        sessionRequestListeners = new ArrayList<JingleSessionRequestListener>();

        // Start a packet listener for session initiation requests
        connection.addPacketListener(new PacketListener()
        {
            @Override
            public void processPacket(Packet packet)
            {
                triggerSessionRequested((Jingle) packet);
            }
        }, initRequestFilter);
    }

    public IncomingSession createIncomingSession(JingleSessionRequest request)
    {
        triggerSessionCreated(request);

        // send acceptance to contact
        Jingle ack = request.getJingle();
        ack.setTo(request.getFrom());
        ack.setFrom(connection.getUser());
        ack.setAction(Jingle.Action.SESSIONACCEPT);
        connection.sendPacket(ack);

        // create incoming session
        return new IncomingSession(connection, request.getFrom(), request.getSid());
    }

    public void rejectIncomingSession(JingleSessionRequest request)
    {
        /*Jingle initiation = request.getJingle();
        
        IQ rejection = JingleSession.createError(initiation.getPacketID(), initiation
        .getFrom(), initiation.getTo(), 403, "Declined");
        connection.sendPacket(rejection);*/
    }

    public OutgoingSession createOutgoingSession(String responder, String filePath) throws XMPPException, FileNotFoundException
    {
        if (StringUtils.parseResource(responder).equals(""))
        {
            throw new XMPPException("Responder JID was not fully qualified");
        }
        
        java.io.File file = new java.io.File(filePath);
        
        if(!file.exists())
        {
            throw new FileNotFoundException("The audio file could not be found at the specified location");
        }

        return new OutgoingSession(connection, responder, file);
    }
}
