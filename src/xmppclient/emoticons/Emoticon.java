/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient.emoticons;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Emoticon implements Serializable
{
    private String name;
    private ImageIcon icon;
    private String sequence;

    public ImageIcon getIcon()
    {
        return icon;
    }

    public String getName()
    {
        return name;
    }

    public String getSequence()
    {
        return sequence;
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    public Emoticon(String name, ImageIcon icon, String sequence)
    {
        this.name = name;
        this.icon = icon;
        this.sequence = sequence;
    }
}
