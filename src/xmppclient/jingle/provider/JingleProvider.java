/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle.provider;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import xmppclient.jingle.packet.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author Lee Boynton (323326)
 */
public class JingleProvider implements IQProvider
{
    public JingleProvider()
    {
        super();
    }

    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception
    {
        Jingle jingle = new Jingle();
        boolean done = false;

        // for testing
        //parser.next();

        // Get some attributes for the <jingle> element
        jingle.setSid(parser.getAttributeValue("", "sid"));
        jingle.setInitiator(parser.getAttributeValue("", "initiator"));
        jingle.setResponder(parser.getAttributeValue("", "responder"));
        jingle.setAction(Jingle.Action.getAction(parser.getAttributeValue("", "action")));

        // get subelements  
        while (!done)
        {
            int eventType = parser.next();
            String elementName = parser.getName();

            if (eventType == XmlPullParser.START_TAG)
            {
                if (elementName.equals("content"))
                {
                    jingle.setContent(Jingle.Content.getContent(parser.getAttributeValue("", "name")));
                }
                if (elementName.equals(Description.ELEMENTNAME))
                {
                    jingle.setDescription((Description) new DescriptionProvider().parseExtension(parser));
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (elementName.equals(Jingle.ELEMENTNAME))
                {
                    done = true;
                }
            }
        }

        return jingle;
    }

    public static void main(String args[])
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            String xml = "<jingle xmlns='urn:xmpp:tmp:jingle' action='session-initiate' initiator='kingclaudius@shakespeare.lit/castle' sid='851ba2'><content creator='initiator' name='a-file-offer'><description xmlns='urn:xmpp:tmp:jingle:apps:file-transfer'><offer><file xmlns='http://jabber.org/protocol/si/profile/file-transfer' name='test.txt' size='1022' hash='552da749930852c69ae5d2141d3766b1' date='1969-07-21T02:56:15Z'><desc>This is a test. If this were a real file...</desc></file></offer></description><transport xmlns='urn:xmpp:tmp:jingle:transports:bytestreams'/></content></jingle>";
            xpp.setInput(new StringReader(xml));
            Jingle jingle = (Jingle) new JingleProvider().parseIQ(xpp);
            System.out.printf("Input : %s\n", xml);
            System.out.printf("Output: %s\n", jingle.getChildElementXML());
        }
        catch (Exception ex)
        {
            Logger.getLogger(FileProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
