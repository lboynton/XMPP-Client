/*
 * TestSender2.java
 *
 * Created on 08 May 2008, 16:14
 */
package xmppclient.audio;

import xmppclient.audio.packet.Audio;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Test class for sending the audio library to another user
 * @author  Lee Boynton (323326)
 */
public class TestSender
{
    private XMPPConnection connection;
    
    /**
     * Logs the user in, generates the library and sends it to another user
     */
    public TestSender()
    {
        XMPPConnection.DEBUG_ENABLED = true;
        
        try
        {
            connection = new XMPPConnection("192.168.0.8");
            connection.connect();
            connection.login("lee", "password");
            AudioLibrary library = new AudioLibrary("audio");
            library.generateListing();
            List<AudioFile> files = library.getAudioFiles();
            
            Audio packet = new Audio(files);
            packet.setFrom(connection.getUser());
            packet.setTo("bob@192.168.0.8/Smack");
            packet.setAId(StringUtils.randomString(5));
            connection.sendPacket(packet);
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
        new TestSender();
    }
}
