/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;

/**
 *
 * @author Lee Boynton (323326)
 */
public class TestSender
{
    public TestSender()
    {
        try
        {
            XMPPConnection.DEBUG_ENABLED = true;
            XMPPConnection connection = new XMPPConnection("192.168.0.8");
            connection.connect();
            connection.login("lee", "password", "home");
            JingleManager jingleManager = new JingleManager(connection);
            jingleManager.createOutgoingSession("bob@192.168.0.8/home", "file.mp3");
        }
        catch (Exception ex)
        {
            Logger.getLogger(TestSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[])
    {
        new TestSender();
    }
}
