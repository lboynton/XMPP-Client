/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

import xmppclient.audio.packet.Audio;

/**
 * An AudioMessage is a container for audio packets and is sent to the listeners
 * for requests and responses
 * @author Lee Boynton (323326)
 */
public class AudioMessage
{
    private Audio audio;
    private AudioManager manager;

    /**
     * Creates a new audio message
     * @param manager The AudioManager associated with the audio message 
     * @param audio The audio packet
     */
    public AudioMessage(AudioManager manager, Audio audio)
    {
        this.audio = audio;
        this.manager = manager;
    }
    
    /**
     * Gets the JID of the user the audio message was sent from
     * @return The JID
     */
    public String getFrom()
    {
        return audio.getFrom();
    }

    /**
     * Gets the audio packet contained in this message
     * @return The audio packet
     */
    public Audio getAudio()
    {
        return audio;
    }
}
