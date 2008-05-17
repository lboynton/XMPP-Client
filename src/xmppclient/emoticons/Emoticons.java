/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.emoticons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Lee Boynton (323326)
 */
public class Emoticons
{
    public static final ImageIcon angel = new javax.swing.ImageIcon(Emoticons.class.getResource("face-angel.png"));
    public static final ImageIcon crying = new javax.swing.ImageIcon(Emoticons.class.getResource("face-crying.png"));
    public static final ImageIcon devilish = new javax.swing.ImageIcon(Emoticons.class.getResource("face-devilish.png"));
    public static final ImageIcon glasses = new javax.swing.ImageIcon(Emoticons.class.getResource("face-glasses.png"));
    public static final ImageIcon grin = new javax.swing.ImageIcon(Emoticons.class.getResource("face-grin.png"));
    public static final ImageIcon kiss = new javax.swing.ImageIcon(Emoticons.class.getResource("face-kiss.png"));
    public static final ImageIcon monkey = new javax.swing.ImageIcon(Emoticons.class.getResource("face-monkey.png"));
    public static final ImageIcon plain = new javax.swing.ImageIcon(Emoticons.class.getResource("face-plain.png"));
    public static final ImageIcon sad = new javax.swing.ImageIcon(Emoticons.class.getResource("face-sad.png"));
    public static final ImageIcon smile = new javax.swing.ImageIcon(Emoticons.class.getResource("face-smile.png"));
    public static final ImageIcon smileBig = new javax.swing.ImageIcon(Emoticons.class.getResource("face-smile-big.png"));
    public static final ImageIcon surprise = new javax.swing.ImageIcon(Emoticons.class.getResource("face-surprise.png"));
    public static final ImageIcon wink = new javax.swing.ImageIcon(Emoticons.class.getResource("face-wink.png"));
    private static List<Emoticon> emoticons;
    
    static
    {
        // check if emoticons file is present
        if(!new File("emoticons.db").exists())
        {
            // load the default emoticons into the array list
            loadDefaultEmoticons();
            
            // save the array list to file
            saveEmoticons();
        }
        else
        {
            // load the emoticons into the array list
            loadEmoticons();
        }
    }
    
    private static void loadDefaultEmoticons()
    {
        emoticons = new ArrayList<Emoticon>();
        emoticons.add(new Emoticon("angel", angel, "0:)"));
        emoticons.add(new Emoticon("crying", crying, ";("));
        emoticons.add(new Emoticon("devilish", devilish, "(d)"));
        emoticons.add(new Emoticon("glasses", glasses, "8)"));
        emoticons.add(new Emoticon("grin", grin, ":D"));
        emoticons.add(new Emoticon("kiss", kiss, ":x"));
        emoticons.add(new Emoticon("monkey", monkey, "(m)"));
        emoticons.add(new Emoticon("plain", plain, ":|"));
        emoticons.add(new Emoticon("sad", sad, ":("));
        emoticons.add(new Emoticon("smile", smile, ":)"));
        emoticons.add(new Emoticon("smile big", smileBig, ":}"));
        emoticons.add(new Emoticon("surprise", surprise, ":O"));
        emoticons.add(new Emoticon("wink", wink, ";)"));
    }
    
    private static void saveEmoticons()
    {
        try
        {
            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream("emoticons.db"));
            fileOut.writeObject(emoticons);
            fileOut.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Emoticons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Loads the emoticons array list from file
     */
    private static void loadEmoticons()
    {
        try
        {
            ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream("emoticons.db"));
            emoticons = (ArrayList<Emoticon>) fileIn.readObject();
            fileIn.close();
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Emoticons.class.getName()).log(Level.SEVERE, null, ex);
        }        
        catch (IOException ex)
        {
            Logger.getLogger(Emoticons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Emoticon> getEmoticons()
    {
        return emoticons;
    }

    public static void addEmoticon(Emoticon emoticon)
    {
        emoticons.add(emoticon);
        saveEmoticons();
    }

    public static void removeEmoticon(Emoticon emoticon)
    {
        emoticons.remove(emoticon);
    }
}
