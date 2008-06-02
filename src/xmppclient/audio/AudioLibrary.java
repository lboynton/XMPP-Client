package xmppclient.audio;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import xmppclient.Utils;

/**
 * This class is used for generating the audio file library. It recursively 
 * goes through a directory and adds MP3 files to the library.
 * @author Lee Boynton (323326)
 */
public class AudioLibrary
{
    private List<AudioFile> audioFiles = new ArrayList<AudioFile>();
    private String path;
    
    /**
     * Gets the file with the given ID. The audio library must have been generated
     * before calling this method.
     * NOTE: If the audio library has been regenerated and there are new audio
     * files, this may not return the intended file.
     * @param id The ID of the file
     * @return The file with the ID
     */
    public File getFile(int id)
    {
        for(AudioFile file:audioFiles)
        {
            if(file.getId() == id)
            {
                return file.getFile();
            }
        }
        
        return null;
    }

    /**
     * Constructor sets the audio path, does not generate the listing.
     * @param path The path to the audio files to generate the listing from
     */
    public AudioLibrary(String path)
    {
        this.path = path;
    }

    /**
     * Gets the list of audio files. The file listing must be generated first
     * if this method is to return any files.
     * @see #generateListing() 
     * @see #generateListing(java.io.File) 
     * @return The list of audio files
     */
    public List<AudioFile> getAudioFiles()
    {
        return audioFiles;
    }

    private void addFile(File file)
    {
        try
        {
            ID3v1 tag = new MP3File(file).getID3v1Tag();

            audioFiles.add(new AudioFile(
                    tag.getSongTitle(),
                    tag.getArtist(),
                    tag.getAlbum(),
                    tag.getTrackNumberOnAlbum(),
                    file));
        }
        catch (IOException ex)
        {
            // could not find the file for some reason
        }
        catch (TagException ex)
        {
            // could not read tag
            Logger.getLogger(AudioLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generates the audio file listing by adding each file to a list. The path 
     * must be set before calling.
     */
    public void generateListing()
    {
        File dir = new File(path);
        generateListing(dir);
    }

    /**
     * Recursively retrieves audio files from the directory and adds them to the
     * audio file list
     * @param directory
     */
    public void generateListing(File directory)
    {
        File[] files = directory.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                if (pathname.isDirectory())
                {
                    return true;
                }
                if (Utils.getExtension(pathname).equals("mp3"))
                {
                    return true;
                }
                return false;
            }
        });

        for (File file : files)
        {
            if(file.isDirectory())
            {
                generateListing(file);
            }
            else
            {
                addFile(file);
            }
        }
    }
}
