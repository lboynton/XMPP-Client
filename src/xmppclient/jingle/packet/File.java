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
public class File implements PacketExtension
{
    // constant values
    public static final String NODENAME = "file";
    public static final String NAMESPACE = "http://jabber.org/protocol/si/profile/file-transfer";
    
    // variable file information
    private String name;
    private String size;
    private String hash;
    private String date;
    private String desc;

    public File(String name, String size, String hash, String date, String desc)
    {
        this.name = name;
        this.size = size;
        this.hash = hash;
        this.date = date;
        this.desc = desc;
    }

    public File() {}

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    @Override
    public String getNamespace()
    {
        return NAMESPACE;
    }

    @Override
    public String getElementName()
    {
        return NODENAME;
    }

    @Override
    public String toXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName());
        buf.append(" xmlns=\"").append(getNamespace()).append("\"");
        buf.append(" name=\"").append(name).append("\"");
        buf.append(" size=\"").append(size).append("\"");
        buf.append(" hash=\"").append(hash).append("\"");
        buf.append(" date=\"").append(date).append("\">");
        buf.append("<desc>").append(desc).append("</desc>");
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
    
    public static void main(String args[])
    {
        System.out.println(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file...").toXML());
    }
}
