package xmppclient.audio;

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

    /**
     * Returns the name of the file
     * @return The name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Creates a new audio file
     * @param name The name
     * @param artist The artist
     * @param album The album
     * @param track The track
     */
    public AudioFile(String name, String artist, String album, String track)
    {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.track = track;
    }

    /**
     * Creates a new audio file
     * @param name The name
     */
    public AudioFile(String name)
    {
        this.name = name;
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
}
