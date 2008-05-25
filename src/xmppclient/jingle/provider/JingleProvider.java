/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle.provider;

import xmppclient.jingle.packet.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

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
                if (elementName.equals(Description.NODENAME))
                {
                    jingle.setDescription((Description) new DescriptionProvider().parseExtension(parser));
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (elementName.equals(Jingle.NODENAME))
                {
                    done = true;
                }
            }
        }

        return jingle;
    }
}
