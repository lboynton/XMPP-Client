/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio.packet;

import xmppclient.audio.*;
import java.util.ArrayList;
import java.util.List;
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
    private AudioType audioType;

    public Audio()
    {
        audioFiles = new ArrayList<AudioFile>();
    }

    public Audio(AudioType audioType, IQ.Type type)
    {
        setType(type);
        setAudioType(audioType);
        audioFiles = new ArrayList<AudioFile>();
    }

    public Audio(List<AudioFile> audioFiles)
    {
        setType(IQ.Type.SET);
        this.audioFiles = audioFiles;
    }

    public AudioType getAudioType()
    {
        return audioType;
    }

    public void setAudioType(AudioType audioType)
    {
        this.audioType = audioType;
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


        buf.append(">");
        if (getType() == IQ.Type.SET)
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

    public static enum AudioType
    {
        LIBRARY, FILE;
        private static String names[] =
        {
            "library", "file"
        };

        /**
         * Returns the String value for an AudioType.
         */
        @Override
        public String toString()
        {
            return names[this.ordinal()];
        }

        /**
         * Returns the AudioType for a String value.
         */
        public static AudioType getAudioType(String str)
        {
            for (int i = 0; i < names.length; i++)
            {
                if (names[i].equals(str))
                {
                    return AudioType.values()[i];
                }
            }
            return null;
        }
    }
}
