/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.jingle.packet;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Description implements PacketExtension
{
    // constant values
    public static final String NODENAME = "description";
    public static final String NAMESPACE = "urn:xmpp:tmp:jingle:apps:file-transfer";
    // variable content
    private PacketExtension type;

    public Description(PacketExtension type)
    {
        this.type = type;
    }

    public Description()
    {
    }

    public void setType(PacketExtension type)
    {
        this.type = type;
    }

    public PacketExtension getType()
    {
        return type;
    }

    @Override
    public String getElementName()
    {
        return NODENAME;
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

    public static void main(String args[])
    {
        System.out.println(new Description(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file...")).toXML());
    }
}
