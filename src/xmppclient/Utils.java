/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import xmppclient.images.Icons;

/**
 * This class provides a number of general purpose utility methods
 * @author Lee
 */
public class Utils 
{
    public static String getExtension(File f) 
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static Icon resizeImage(ImageIcon image, int height)
    {
        ImageIcon resizedImage = null;
        
        if (image != null) 
        {
            if (image.getIconHeight() > height) 
            {
                resizedImage = new ImageIcon(image.getImage().
                    getScaledInstance(-1, height,
                    Image.SCALE_DEFAULT));
            } 
            else 
            { 
                resizedImage = image;
            }
        }
        
        return resizedImage;
    }
    
    public static String getNickname(RosterEntry rosterEntry)
    {
        VCard vCard = new VCard();
        
        if(rosterEntry.getName() != null) return rosterEntry.getName();
        
        try
        {
            vCard.load(XMPPClientUI.connection, rosterEntry.getUser());
            if(vCard.getNickName() != null && !vCard.getNickName().equals(""))
                return vCard.getNickName();
        }
        catch (Exception ex) {}
        
        if(rosterEntry.getUser() != null) return rosterEntry.getUser();
        
        return null;
    }
    
    public static String getStatus(Presence presence)
    {
        if(presence.getStatus() != null)
        {
            return presence.getStatus();
        }
        else return null;
    }
    
    public static Icon getAvatar(RosterEntry rosterEntry)
    {
        VCard vCard = new VCard();
        
        try
        {
            vCard.load(XMPPClientUI.connection, rosterEntry.getUser());
            return Utils.resizeImage(new ImageIcon(vCard.getAvatar()), 60);
        }
        catch (XMPPException ex) 
        {
            System.err.printf("Error loading avatar for user %s: %s", rosterEntry.getUser(), ex.getMessage());
        }
        catch (NullPointerException ex)
        {
            // user has no avatar
        }
        
        return null;
    }
    
    public static ImageIcon getUserIcon(Presence presence)
    {
        if(presence.getMode() == Presence.Mode.dnd)
        {
            return Icons.busy;
        }
        if(!presence.isAvailable())
        {
            return Icons.offline;
        }
        if(presence.isAway())
        {
            return Icons.away;
        }

        return Icons.online;
    }
}
