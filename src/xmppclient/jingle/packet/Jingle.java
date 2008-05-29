/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle.packet;

import org.jivesoftware.smack.packet.IQ;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Jingle extends IQ
{
    public static final String NAMESPACE = "urn:xmpp:tmp:jingle";
    public static final String NODENAME = "jingle";
    private String initiator; // the user who initiated communication
    private String responder; // the user who should respond to communication
    private String sid; // the session id
    private String responderIP;
    private String initiatorIP;
    private String responderPort;
    private String initiatorPort;
    
    /** Which parties in the session will be generating content; the allowable 
     * values are "initiator", "responder", or "both" (where "both" is the default). */
    private String senders;
    
    // sub-elements of a Jingle packet
    private Content content;
    private Action action;
    private Description description;

    public Jingle()
    {
        super.setType(IQ.Type.SET);
    }
    
    public Jingle(boolean test)
    {
        if(!test) return;
        super.setType(IQ.Type.SET);
        content = Content.FILEOFFER;
        action = Action.SESSIONINITIATE;
        description = new Description(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file..."));
        initiator = "lee@192.168.0.8/home";
        responder = "bob@192.168.0.8/home";
        sid = "851ba2";
    }

    public Description getDescription()
    {
        return description;
    }

    public Jingle(Action action)
    {
        super.setType(IQ.Type.SET);
        this.action = action;
    }

    public String getSid()
    {
        return sid;
    }

    public Action getAction()
    {
        return action;
    }

    public String getInitiator()
    {
        return initiator;
    }

    public String getResponder()
    {
        return responder;
    }

    public void setDescription(Description description)
    {
        this.description = description;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public void setContent(Content content)
    {
        this.content = content;
    }

    public void setSender(String sender)
    {
        this.senders = sender;
    }

    public void setInitiator(String initiator)
    {
        this.initiator = initiator;
    }

    public void setResponder(String responder)
    {
        this.responder = responder;
    }

    public final void setSid(final String sid)
    {
        this.sid = sid;
    }

    public String getInitiatorIP()
    {
        return initiatorIP;
    }

    public void setInitiatorIP(String initiatorIP)
    {
        this.initiatorIP = initiatorIP;
    }

    public String getInitiatorPort()
    {
        return initiatorPort;
    }

    public void setInitiatorPort(String initiatorPort)
    {
        this.initiatorPort = initiatorPort;
    }

    public String getResponderIP()
    {
        return responderIP;
    }

    public void setResponderIP(String responderIP)
    {
        this.responderIP = responderIP;
    }

    public String getResponderPort()
    {
        return responderPort;
    }

    public void setResponderPort(String responderPort)
    {
        this.responderPort = responderPort;
    }

    @Override
    public String getChildElementXML()
    {
        StringBuilder buf = new StringBuilder();

        buf.append("<").append(NODENAME);
        buf.append(" xmlns=\"").append(NAMESPACE).append("\"");
        if (initiator != null)
        {
            buf.append(" initiator=\"").append(initiator).append("\"");
        }
        if (responder != null)
        {
            buf.append(" responder=\"").append(responder).append("\"");
        }
        if (action != null)
        {
            buf.append(" action=\"").append(action).append("\"");
        }
        if (sid != null)
        {
            buf.append(" sid=\"").append(sid).append("\"");
        }
        buf.append(">");

        buf.append("<content");

        if (content != null)
        {
            buf.append(" name=\"").append(content).append("\"");
        }
        if (initiator != null)
        {
            buf.append(" creator=\"").append("initiator").append("\"");
        }
        if (senders != null)
        {
            buf.append(" senders=\"").append(senders).append("\"");
        }

        buf.append(">");
        if(description != null) buf.append(description.toXML());
        buf.append("<transport xmlns=\"urn:xmpp:tmp:jingle:transports:bytestreams\"/>");
        buf.append("</content>");
        buf.append("</jingle>");
        return buf.toString();
    }

    public static enum Content
    {
        FILEOFFER;
        private static String names[] =
        {
            "a-file-offer"
        };

        @Override
        public String toString()
        {
            return names[this.ordinal()];
        }

        /**
         * Returns the content for a String value.
         */
        public static Content getContent(String str)
        {
            for (int i = 0; i < names.length; i++)
            {
                if (names[i].equals(str))
                {
                    return Content.values()[i];
                }
            }
            return null;
        }
    }

    public static enum Action
    {
        CONTENTACCEPT, CONTENTADD, CONTENTDECLINE, CONTENTMODIFY,
        CONTENTREMOVE, DESCRIPTIONADD, DESCRIPTIONDECLINE, DESCRIPTIONINFO, 
        DESCRIPTIONMODIFY, SESSIONACCEPT, SESSIONINFO, SESSIONINITIATE, 
        SESSIONREDIRECT, SESSIONTERMINATE, TRANSPORTACCEPT, TRANSPORTDECLINE,
        TRANSPORTINFO, TRANSPORTMODIFY;
        private static String names[] =
        {
            "content-accept", "content-add", "content-decline", "content-modify",
            "content-remove", "description-accept", "description-decline", "description-info",
            "description-modify", "session-accept", "session-info", "session-initiate",
            "session-redirect", "session-terminate", "transport-accept", "transport-decline",
            "transport-info", "transport-modify"
        };

        /**
         * Returns the String value for an Action.
         */
        @Override
        public String toString()
        {
            return names[this.ordinal()];
        }

        /**
         * Returns the Action for a String value.
         */
        public static Action getAction(String str)
        {
            for (int i = 0; i < names.length; i++)
            {
                if (names[i].equals(str))
                {
                    return Action.values()[i];
                }
            }
            return null;
        }
    }
    
    public static void main(String args[])
    {
        System.out.println(new Jingle().toXML());
    }
}
