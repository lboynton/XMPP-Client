/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Lee Boynton (323326)
 */
public class FileChooserFilter extends FileFilter
{
    private List<String> extensions = new ArrayList<String>();
    private String description;

    public FileChooserFilter(String[] extensions, String description)
    {
        this.extensions.addAll(Arrays.asList(extensions));
    }
    
    public FileChooserFilter(String extension, String description)
    {
        this.description = description;
        extensions.add(extension);
    }
    
    public void addExtension(String extension)
    {
        extensions.add(extension);
    }

    /**
     * Accepts image files
     * @param f The file
     * @return True if file is an image, false otherwise
     */
    @Override
    public boolean accept(File f)
    {
        if (f.isDirectory())
        {
            return true;
        }

        String fileExtension = Utils.getExtension(f);
        if (fileExtension != null)
        {
            for(String extension:extensions)
            {
                if(fileExtension.equals(extension)) return true;
            }
        }

        return false;
    }

    /**
     * The description of the filter that is displayed
     * @return The description
     */
    @Override
    public String getDescription()
    {
        return description;
    }
}
