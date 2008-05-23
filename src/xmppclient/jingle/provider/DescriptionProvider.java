/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle.provider;

import xmppclient.jingle.packet.*;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author Lee Boynton (323326)
 */
public class DescriptionProvider implements PacketExtensionProvider
{
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception
    {
        boolean done = false;
        Description desc = new Description();

        while (!done)
        {
            int eventType = parser.next();
            String name = parser.getName();

            if (eventType == XmlPullParser.START_TAG)
            {
                if (name.equals(File.NODENAME))
                {
                    desc.setType(new FileProvider().parseExtension(parser));
                }
                else if(name.equals(Description.NODENAME)) continue;
                else if(name.equals("offer")) continue;
                else
                {
                    throw new Exception("Unknown element \"" + name + "\" in content.");
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (name.equals(Description.NODENAME))
                {
                    done = true;
                }
            }
        }
        return desc;
    }
    
    public static void main(String args[])
    {
        try
        {
            Description desc = new Description(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file..."));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            System.out.printf("Input: %s\n", desc.toXML());
            xpp.setInput(new StringReader(desc.toXML()));
            Description outDescription = (Description) new DescriptionProvider().parseExtension(xpp);
            System.out.printf("Output: %s\n", outDescription.toXML());
        }
        catch (Exception ex)
        {
            Logger.getLogger(FileProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
