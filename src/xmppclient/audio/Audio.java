/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import xmppclient.Utils;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Audio
{
    private String path;
    private List<AudioFile> audioFiles = new ArrayList<AudioFile>();

    public Audio(String path)
    {
        this.path = path;
    }

    public List<AudioFile> getAudioFiles()
    {
        return audioFiles;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
    
    public void generateDirectory()
    {
        new File(path).mkdir();
    }
    
    public void generateListing()
    {
        File dir = new File(path);
        
        File[] files = dir.listFiles(new FileFilter() 
        {
            @Override
            public boolean accept(File pathname)
            {
                if(Utils.getExtension(pathname).equals("mp3")) return true;
                
                return false;
            }
        });
        
        for(File file:files)
        {
            audioFiles.add(new AudioFile(file.getName()));
        }
    }
}
