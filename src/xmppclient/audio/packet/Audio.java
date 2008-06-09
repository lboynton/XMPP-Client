/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio.packet;

import xmppclient.audio.*;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * An audio packet, used for sending audio library requests and reponses, and also
 * requests for file streams.
 * @author Lee Boynton (323326)
 */
public class Audio extends IQ
{
    /**
     * The namespace associated with the audio library feature. As this is not
     * an official feature any string can be used
     */
    public static final String NAMESPACE = "somenamespace";
    /**
     * The element name of the XML tag
     */
    public static final String ELEMENTNAME = "audio";
    private List<AudioFile> audioFiles;
    private String AId;
    private AudioType audioType;

    /**
     * Creates a new audio packet with an empty list of audio files
     */
    public Audio()
    {
        audioFiles = new ArrayList<AudioFile>();
    }

    /**
     * Creates a new audio packet with the given audio type and IQ type.
     * @param audioType The audio type is either a whole library or an individual file
     * @param type The IQ packet type. Get for requests, set for responses
     */
    public Audio(AudioType audioType, IQ.Type type)
    {
        setType(type);
        setAudioType(audioType);
        audioFiles = new ArrayList<AudioFile>();
    }

    /**
     * Creates a new audio packet, adds the list of files to it, and sets the IQ
     * type to set, as files are only sent in responses
     * @param audioFiles The audio files to add to the packet
     */
    public Audio(List<AudioFile> audioFiles)
    {
        setType(IQ.Type.SET);
        setAudioType(AudioType.LIBRARY);
        this.audioFiles = audioFiles;
    }

    /**
     * Gets the audio type
     * @return Either file or library
     */
    public AudioType getAudioType()
    {
        return audioType;
    }

    /**
     * Sets the audio type
     * @param audioType Either file or library
     */
    public void setAudioType(AudioType audioType)
    {
        this.audioType = audioType;
    }

    /**
     * Adds a file to the packet
     * @param file The file to add
     */
    public void addFile(AudioFile file)
    {
        audioFiles.add(file);
    }

    /**
     * Sets the audio ID. This may or may not be necessary
     * @param AId The new audio ID
     */
    public void setAId(String AId)
    {
        this.AId = AId;
    }

    /**
     * Gets the list of audio files in this packet
     * @return The list of audio files
     */
    public List<AudioFile> getAudioFiles()
    {
        return audioFiles;
    }
    
    /**
     * Gets the first audio file in this packet. Useful for file requests when
     * there is only one file.
     * @return The first audio file
     */
    public AudioFile getAudioFile()
    {
        return audioFiles.get(0);
    }

    @Override
    public String getChildElementXML()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("<audio");
        buf.append(" xmlns=\"").append(NAMESPACE).append("\"");
        if(audioType != null)
        {
            buf.append(" type=\"").append(audioType).append("\"");
        }
        if (AId != null)
        {
            buf.append(" aid=\"").append(AId).append("\"");
        }

        buf.append(">");
        if (getType() == IQ.Type.SET && audioType == AudioType.LIBRARY)
        {
            for (AudioFile file : audioFiles)
            {
                buf.append("<file");
                buf.append(" id=\"").append(file.getId()).append("\"");
                buf.append(" artist=\"").append(StringUtils.escapeForXML(file.getArtist())).append("\"");
                buf.append(" album=\"").append(StringUtils.escapeForXML(file.getAlbum())).append("\"");
                buf.append(" track=\"").append(StringUtils.escapeForXML(file.getTrack())).append("\">");
                buf.append(StringUtils.escapeForXML(file.getName()));
                buf.append("</file>");
            }
        }
        if (audioType == AudioType.FILE)
        {
            for (AudioFile file : audioFiles)
            {
                buf.append("<file id=\"").append(file.getId()).append("\"/>");
            }
        }
        buf.append("</audio>");
        return buf.toString();
    }

    /**
     * The audio type is either an entire library or an individual file
     */
    public static enum AudioType
    {
        /**
         * 
         */
        LIBRARY,
        /**
         * 
         */
        FILE;
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
         * @param str The string to get the audio type from
         * @return The audio type, or null if unrecognised
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
