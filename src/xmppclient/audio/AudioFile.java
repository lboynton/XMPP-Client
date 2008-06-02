package xmppclient.audio;

import java.io.File;

/**
 * Represents an audio file
 * @author Lee Boynton (323326)
 */
public class AudioFile
{
    private String name;
    private String artist;
    private String album;
    private String track;
    private static int counter = 0;
    private int id;
    private File file;

    /**
     * Sets the audio file
     * @param file The file
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * Gets the audio file
     * @return The audio file
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Gets the ID of this audio file
     * @return The ID
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the name of the file
     * @return The name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the ID of this audio file
     * @param id The new ID
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets the name of this audio file
     * @param name The name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Creates a blank audio file
     */
    public AudioFile()
    {
    }

    /**
     * Creates a new audio file
     * @param name The name
     * @param artist The artist
     * @param album The album
     * @param track The track
     * @param file The audio file itself
     */
    public AudioFile(String name, String artist, String album, String track, File file)
    {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.file = file;
        this.id = counter++;
    }

    /**
     * Creates a new audio file
     * @param name The name
     */
    public AudioFile(String name)
    {
        this.name = name;
        this.id = counter++;
    }

    /**
     * Gets the album of this audio file
     * @return The album
     */
    public String getAlbum()
    {
        return album;
    }

    /**
     * Sets the album of this audio file
     * @param album The new album
     */
    public void setAlbum(String album)
    {
        this.album = album;
    }

    /**
     * Gets the artist of this audio file
     * @return The artist
     */
    public String getArtist()
    {
        return artist;
    }

    /**
     * Sets the artist of this audio file
     * @param artist The new artist
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    /**
     * Gets the track of this audio file
     * @return The track
     */
    public String getTrack()
    {
        return track;
    }

    /**
     * Sets the track of this audio file
     * @param track The new track
     */
    public void setTrack(String track)
    {
        this.track = track;
    }
    
    @Override
    public String toString()
    {
        return track + ". " + name + " - " + artist + " (" + album + ")";
    }
}
