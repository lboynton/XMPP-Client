/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient.formatter;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author lee
 */
public class Format 
{
    private Font font;
    private Color color;

    public Format(Font font, Color color)
    {
        this.font = font;
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public Font getFont()
    {
        return font;
    }
}
