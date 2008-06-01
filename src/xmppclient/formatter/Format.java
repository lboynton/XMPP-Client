package xmppclient.formatter;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 * This class is used to wrap a font and colour into one object for ease of use
 * @author Lee Boynton (323326)
 */
public class Format implements Serializable
{
    private Font font;
    private Color colour;

    /**
     * Creates a new format with the given font and colour
     * @param font The font to use in this format
     * @param colour The colour to use in this format
     */
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
        font = new Font("Tahoma", Font.PLAIN, 12);
        colour = Color.black;
    }

    /**
     * Gets the colour
     * @return The colour
     */
    public Color getColour()
    {
        return colour;
    }

    /**
     * Gets the font
     * @return The font
     */
    public Font getFont()
    {
        return font;
    }
}
