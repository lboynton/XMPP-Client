/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

import java.util.List;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AudioPacket extends Packet
{
    private List<AudioFile> audioFiles;

    public AudioPacket(List<AudioFile> audioFiles)
    {
        this.audioFiles = audioFiles;
    }
    
    @Override
    public String toXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<audio");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
        }
        
        buf.append(">");
        
        for(AudioFile file:audioFiles)
        {
            buf.append("<file>");
            buf.append(file.getName());
            buf.append("</file>");
        }
        buf.append("</audio>");
        return buf.toString();
    }
}
