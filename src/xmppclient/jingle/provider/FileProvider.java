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
public class FileProvider implements PacketExtensionProvider
{
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception
    {
        File file = new File();
        Boolean done = false;
        
        file.setName(parser.getAttributeValue("", "name"));
        file.setDate(parser.getAttributeValue("", "date"));
        file.setHash(parser.getAttributeValue("", "hash"));
        file.setSize(parser.getAttributeValue("", "size"));

        while (!done)
        {
            int eventType = parser.next();
            String elementName = parser.getName();

            if (eventType == XmlPullParser.TEXT)
            {
                file.setDesc(parser.getText());
            }
            if (eventType == XmlPullParser.END_TAG)
            {
                if (elementName.equals(File.NODENAME))
                {
                    done = true;
                }
            }
        }

        return file;
    }

    public static void main(String args[])
    {
        try
        {
            File file = new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file...");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            System.out.printf("Input: %s\n", file.toXML());
            xpp.setInput(new StringReader(file.toXML()));
            File outFile = (File) new FileProvider().parseExtension(xpp);
            System.out.printf("Output: %s\n", outFile.toXML());
        }
        catch (Exception ex)
        {
            Logger.getLogger(FileProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
