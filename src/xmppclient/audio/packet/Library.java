/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio.packet;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Library implements PacketExtension
{
    public static final String ELEMENT_NAME = "library";
    public static final String NAMESPACE = "library";
    
    @Override
    public String getElementName()
    {
        return ELEMENT_NAME;
    }

    @Override
    public String getNamespace()
    {
        return NAMESPACE;
    }

    @Override
    public String toXML()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
