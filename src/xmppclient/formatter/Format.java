/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient.formatter;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 *
 * @author lee
 */
public class Format implements Serializable
{
    private Font font;
    private Color colour;

    public Format(Font font, Color colour)
    {
        this.font = font;
        this.colour = colour;
    }
    
    /**
     * Creates a new format with the default font and colour
     */
    public Format()
    {
        font = new Font("Tahoma", 12, Font.PLAIN);
        colour = Color.black;
    }

    public Color getColor()
    {
        return colour;
    }

    public Font getFont()
    {
        return font;
    }
}
