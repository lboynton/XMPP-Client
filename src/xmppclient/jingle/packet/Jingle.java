package xmppclient.jingle.packet;

import org.jivesoftware.smack.packet.IQ;

/**
 * A Jingle packet. Jingle packets are subelements of IQ packets.
 * See http://www.xmpp.org/extensions/xep-0234.html for details.
 * @author Lee Boynton (323326)
 */
public class Jingle extends IQ
{
    /**
     * The namespace of this packet
     */
    public static final String NAMESPACE = "urn:xmpp:tmp:jingle";
    /**
     * The element name of this packet
     */
    public static final String ELEMENTNAME = "jingle";
    private String initiator; // the user who initiated communication
    private String responder; // the user who should respond to communication
    private String sid; // the session id
    
    /** Which parties in the session will be generating content; the allowable 
     * values are "initiator", "responder", or "both" (where "both" is the default). */
    private String senders;
    
    // sub-elements of a Jingle packet
    private Content content;
    private Action action;
    private Description description;

    /**
     * Creates a new Jingle packet with IQ type of set. This is used for responses.
     */
    public Jingle()
    {
        super.setType(IQ.Type.SET);
    }
    
    /**
     * Used for testing purposes only.
     * @param test True if testing
     */
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

    /**
     * Gets the description subelement of this jingle packet
     * @return The description subelement
     */
    public Description getDescription()
    {
        return description;
    }

    /**
     * Sets the action of this jingle packet. Also sets the IQ type to set
     * @param action
     */
    public Jingle(Action action)
    {
        super.setType(IQ.Type.SET);
        this.action = action;
    }

    /**
     * Gets the session ID of the jingle packet
     * @return The ID
     */
    public String getSid()
    {
        return sid;
    }

    /**
     * Gets the session action
     * @return The action
     */
    public Action getAction()
    {
        return action;
    }

    /**
     * Gets the initiator of the session
     * @return The initiator JID
     */
    public String getInitiator()
    {
        return initiator;
    }

    /**
     * Gets the responder of the session
     * @return The responder JID
     */
    public String getResponder()
    {
        return responder;
    }

    /**
     * Sets the description subelement of this jingle packet
     * @param description The description subelement
     */
    public void setDescription(Description description)
    {
        this.description = description;
    }

    /**
     * Sets the action
     * @param action The action of this jingle packet
     */
    public void setAction(Action action)
    {
        this.action = action;
    }

    /**
     * Sets the content
     * @param content The content
     */
    public void setContent(Content content)
    {
        this.content = content;
    }

    /**
     * Sets the sender of this jingle packet
     * @param sender The sender JID
     */
    public void setSender(String sender)
    {
        this.senders = sender;
    }

    /**
     * Sets the initiator of this jingle packet
     * @param initiator The initiator JID
     */
    public void setInitiator(String initiator)
    {
        this.initiator = initiator;
    }

    /**
     * Sets the responder of this jingle packet
     * @param responder The responder JID
     */
    public void setResponder(String responder)
    {
        this.responder = responder;
    }

    /**
     * Sets the ID of this jingle packet
     * @param sid the ID
     */
    public final void setSid(final String sid)
    {
        this.sid = sid;
    }

    @Override
    public String getChildElementXML()
    {
        StringBuilder buf = new StringBuilder();

        buf.append("<").append(ELEMENTNAME);
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

    /**
     * The content subelement of the jingle packet
     */
    public static enum Content
    {
        /**
         * Indicates the jingle packet is for file transfers
         */
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
         * @param str the string to get the content from
         * @return the content
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

    /**
     * The action associated with this jingle packet
     */
    public static enum Action
    {
        /**
         * Not used yet 
         */
        CONTENTACCEPT,
        /**
         * Not used yet
         */
        CONTENTADD,
        /**
         * Not used yet
         */
        CONTENTDECLINE,
        /**
         * Not used yet
         */
        CONTENTMODIFY,
        /**
         * Not used yet
         */
        CONTENTREMOVE,
        /**
         * Not used yet
         */
        DESCRIPTIONADD,
        /**
         * Not used yet
         */
        DESCRIPTIONDECLINE,
        /**
         * Not used yet
         */
        DESCRIPTIONINFO,
        /**
         * Not used yet
         */
        DESCRIPTIONMODIFY,
        /**
         * Accepts the jingle session
         */
        SESSIONACCEPT,
        /**
         * Not used yet
         */
        SESSIONINFO,
        /**
         * Initiates a jingle session
         */
        SESSIONINITIATE,
        /**
         * Not used yet
         */
        SESSIONREDIRECT,
        /**
         * Terminates a jingle session
         */
        SESSIONTERMINATE,
        /**
         * Not used yet
         */
        TRANSPORTACCEPT,
        /**
         * Not used yet
         */
        TRANSPORTDECLINE,
        /**
         * Not used yet
         */
        TRANSPORTINFO,
        /**
         * Not used yet
         */
        TRANSPORTMODIFY;
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
         * @param str the string to get the action for
         * @return The action
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
    
    /**
     * Used for testing the XML output of the jingle packet
     * @param args not used
     */
    public static void main(String args[])
    {
        System.out.println(new Jingle().toXML());
    }
}
