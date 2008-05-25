/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AudioFile
{
    private String name;
    private String artist;
    private String album;
    private String track;

    public String getName()
    {
        return name;
    }

    public AudioFile(String name, String artist, String album, String track)
    {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.track = track;
    }

    public AudioFile(String name)
    {
        this.name = name;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getTrack()
    {
        return track;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }
}
