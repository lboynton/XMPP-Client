/*
 * TestReceiver2.java
 *
 * Created on 08 May 2008, 16:12
 */
package xmppclient.audio;

import xmppclient.audio.packet.Audio;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import xmppclient.audio.provider.AudioProvider;

/**
 * A test class for receiving the audio library from another user
 * @author  Lee Boynton (323326)
 */
public class TestReceiver
{
    private XMPPConnection connection;

    /**
     * Logs in and adds support for the audio packets
     */
    public TestReceiver()
    {
        XMPPConnection.DEBUG_ENABLED = true;

        try
        {
            connection = new XMPPConnection("192.168.0.8");
            connection.connect();
            ProviderManager providerManager = ProviderManager.getInstance();
            providerManager.addIQProvider(Audio.ELEMENTNAME, Audio.NAMESPACE, new AudioProvider());
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Audio.NAMESPACE);
            connection.login("bob", "password");
        }
        catch (XMPPException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new TestReceiver();
    }
}
