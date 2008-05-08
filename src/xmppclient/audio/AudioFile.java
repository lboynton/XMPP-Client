/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

import java.io.Serializable;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AudioFile implements Serializable
{
    private String name;

    public String getName()
    {
        return name;
    }

    public AudioFile(String name)
    {
        this.name = name;
    }
}
