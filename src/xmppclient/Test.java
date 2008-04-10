package xmppclient;

import org.jivesoftware.smack.*;

/**
 *
 * @author 323326
 */
public class Test 
{
    public static void main(String args[])
    {
        System.out.println("Starting connection test...");
        
        XMPPConnection conn1 = new XMPPConnection("192.168.0.8");
        
        try
        {
            conn1.connect();
            System.out.println("Successfully connected to server");
        }
        catch(XMPPException e)
        {
            e.printStackTrace();
            System.out.println("Failed to connect to server");
            return;
        }
        
        try
        {
            conn1.login("lee", "password");
            System.out.println("Successfully logged into server");
        }
        catch(XMPPException e)
        {
            e.printStackTrace();
            System.out.println("Failed to log in to server");
            return;
        }
    }
}
