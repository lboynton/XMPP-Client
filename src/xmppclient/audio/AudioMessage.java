/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

import xmppclient.audio.packet.Audio;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AudioMessage
{
    private Audio audio;
    private AudioManager manager;

    public AudioMessage(AudioManager manager, Audio audio)
    {
        this.audio = audio;
        this.manager = manager;
    }
    
    public String getFrom()
    {
        return audio.getFrom();
    }

    public Audio getAudio()
    {
        return audio;
    }
    
    
}
