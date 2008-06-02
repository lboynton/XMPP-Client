package xmppclient.jingle.packet;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Represents a file subelement for the description subelement of the Jingle subelement
 * See http://www.xmpp.org/extensions/xep-0234.html for details.
 * @author Lee Boynton (323326)
 */
public class File implements PacketExtension
{
    // constant values
    /**
     * The element name
     */
    public static final String ELEMENTNAME = "file";
    /**
     * The namespace of this element
     */
    public static final String NAMESPACE = "http://jabber.org/protocol/si/profile/file-transfer";
    
    // variable file information
    private String name;
    private String size;
    private String hash;
    private String date;
    private String desc;

    /**
     * Creates a new file element with the given parameters
     * @param name The name of the file
     * @param size The size of the file
     * @param hash The hash of the file
     * @param date The (modified?) date of the file
     * @param desc A text description of the file
     */
    public File(String name, String size, String hash, String date, String desc)
    {
        this.name = name;
        this.size = size;
        this.hash = hash;
        this.date = date;
        this.desc = desc;
    }

    /**
     * Creates a new file element with the given parameters
     * @param name The name of the file
     * @param size The size of the file
     * @param hash The hash of the file
     */
    public File(String name, String size, String hash)
    {
        this.name = name;
        this.size = size;
        this.hash = hash;
    }

    /**
     * Creates an empty file element
     */
    public File() {}

    /**
     * Sets the date of this file
     * @param date The date
     */
    public void setDate(String date)
    {
        this.date = date;
    }

    /**
     * Sets the description of this file
     * @param desc The description
     */
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    /**
     * Sets the hash of this file
     * @param hash The hash
     */
    public void setHash(String hash)
    {
        this.hash = hash;
    }

    /**
     * Sets the name of this file
     * @param name The name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the file size of this file
     * @param size The size
     */
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
        return ELEMENTNAME;
    }

    /**
     * Gets the date of this file
     * @return The date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Gets the description of this file
     * @return The description
     */
    public String getDesc()
    {
        return desc;
    }

    /**
     * Gets the hash of this file
     * @return The hash
     */
    public String getHash()
    {
        return hash;
    }

    /**
     * Gets the name of this file
     * @return The name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the size of this file
     * @return The file size
     */
    public String getSize()
    {
        return size;
    }

    @Override
    public String toXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName());
        buf.append(" xmlns=\"").append(getNamespace()).append("\"");
        buf.append(" name=\"").append(name).append("\"");
        buf.append(" size=\"").append(size).append("\"");
        if(hash != null)
            buf.append(" hash=\"").append(hash).append("\"");
        if(date != null)
            buf.append(" date=\"").append(date).append("\"");
        buf.append(">");
        if(desc != null)
            buf.append("<desc>").append(desc).append("</desc>");
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
    
    /**
     * Used for testing this class
     * @param args not used
     */
    public static void main(String args[])
    {
        System.out.println(new File("test.txt", "1022", "552da749930852c69ae5d2141d3766b1", "1969-07-21T02:56:15Z", "This is a test. If this were a real file...").toXML());
    }
}
