/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Lee Boynton (323326)
 */
public class AudioLibrary
{
    private List<AudioFile> audioFiles = new ArrayList<AudioFile>();
    private String path;

    public AudioLibrary(String path)
    {
        this.path = path;
    }

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
                    tag.getTrackNumberOnAlbum()));
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
    
    public void generateListing()
    {
        File dir = new File(path);
        generateListing(dir);
    }

    /**
     * Recursively retrieves audio files from the directory
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
