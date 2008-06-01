package xmppclient.emoticons;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * This class is used for representing emoticons. Allows the name, icon, and 
 * character sequence to be stored for an emoticon.
 * @author Lee Boynton (323326)
 */
public class Emoticon implements Serializable
{
    private String name;
    private ImageIcon icon;
    private String sequence;

    /**
     * Gets the icon of the emoticon
     * @return The emoticon icon
     */
    public ImageIcon getIcon()
    {
        return icon;
    }

    /**
     * Gets the name of this emoticon
     * @return The name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the character sequence of this emoticon
     * @return The character sequence as a string
     */
    public String getSequence()
    {
        return sequence;
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Creates a new emoticon with the given details
     * @param name The name of the emoticon
     * @param icon The graphical emoticon icon
     * @param sequence The character sequence which represents this emoticon
     */
    public Emoticon(String name, ImageIcon icon, String sequence)
    {
        this.name = name;
        this.icon = icon;
        this.sequence = sequence;
    }
}
