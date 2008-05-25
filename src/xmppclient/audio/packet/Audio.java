/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio.packet;

import xmppclient.audio.*;
import java.util.ArrayList;
import java.util.List;
import org.farng.mp3.MP3File;
import org.farng.mp3.id3.ID3v1;
import org.jivesoftware.smack.packet.IQ;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Audio extends IQ
{
    public static final String NAMESPACE = "somenamespace";
    public static final String ELEMENTNAME = "audio";
    private List<AudioFile> audioFiles;
    private String AId;

    public Audio()
    {
        setType(IQ.Type.GET);
        audioFiles = new ArrayList<AudioFile>();
    }

    public Audio(IQ.Type type)
    {
        setType(type);
        audioFiles = new ArrayList<AudioFile>();
    }

    public Audio(List<AudioFile> audioFiles)
    {
        setType(IQ.Type.SET);
        this.audioFiles = audioFiles;
    }

    public void addFile(AudioFile file)
    {
        audioFiles.add(file);
    }

    public void setAId(String AId)
    {
        this.AId = AId;
    }

    public List<AudioFile> getAudioFiles()
    {
        return audioFiles;
    }

    @Override
    public String getChildElementXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<audio");
        buf.append(" xmlns=\"").append(NAMESPACE).append("\"");
        if (AId != null)
        {
            buf.append(" aid=\"").append(AId).append("\"");
        }
        /*if (getFrom() != null)
        {
        buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }*/

        buf.append(">");
        if(getType() == IQ.Type.SET)
        {
            for (AudioFile file : audioFiles)
            {              
                buf.append("<file");
                buf.append(" artist=\"").append(file.getArtist()).append("\"");
                buf.append(" album=\"").append(file.getAlbum()).append("\"");
                buf.append(" track=\"").append(file.getTrack()).append("\">");
                buf.append(file.getName());
                buf.append("</file>");
            }
        }
        buf.append("</audio>");
        return buf.toString();
    }
}
