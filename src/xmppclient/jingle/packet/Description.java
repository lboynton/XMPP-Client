package xmppclient.jingle.packet;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Represents the Description subelement of a Jingle packet
 * See http://www.xmpp.org/extensions/xep-0234.html for details.
 * @author Lee Boynton (323326)
 */
public class Description implements PacketExtension
{
    // constant values
    /**
     * The XML element name
     */
    public static final String ELEMENTNAME = "description";
    /**
     * The namespace identifying this packet extension
     */
    public static final String NAMESPACE = "urn:xmpp:tmp:jingle:apps:file-transfer";
    // variable content
    private PacketExtension type;

    /**
     * Creates a description element with a subelement
     * @param type The subelement
     */
    public Description(PacketExtension type)
    {
        this.type = type;
    }

    /**
     * Creates an empty description Jingle element
     */
    public Description()
    {
    }

    /**
     * Sets the subelement of the description element
     * @param type
     */
    public void setType(PacketExtension type)
    {
        this.type = type;
    }

    /**
     * Gets the subelement of the description element
     * @return The subelement
     */
    public PacketExtension getType()
    {
        return type;
    }

    @Override
    public String getElementName()
    {
        return ELEMENTNAME;
    }

    @Override
    public String getNamespace()
    {
        return NAMESPACE;
    }

    @Override
    public String toXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName());
        buf.append(" xmlns=\"").append(getNamespace()).append("\">");
        buf.append("<offer>");
        buf.append(type.toXML());
        buf.append("</offer>");
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    /**
     * Used for testing
     * @param args not used
     */
    public static void main(String args[])
    {
        System.out.println(new Description(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file...")).toXML());
    }
}
