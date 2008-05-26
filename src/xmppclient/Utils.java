/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import xmppclient.images.Icons;

/**
 * This class provides a number of general purpose utility methods
 * @author Lee
 */
public class Utils
{
    private static final String PROPERTIES_DESC = "This file contains properties for the specified account name";

    /**
     * 
     * @param f
     * @return
     */
    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Creates an error JLabel which displays an error icon
     * @param label The JLabel to use
     * @param message The message to display in the error label
     */
    public static void createErrorLabel(JLabel label, String message)
    {
        label.setText(message);
        label.setIcon(Icons.error);
    }

    /**
     * Creates a success JLabel which displays a success icon
     * @param label The JLabel to use
     * @param message The message to display in the success label
     */
    public static void createSuccessLabel(JLabel label, String message)
    {
        label.setText(message);
        label.setIcon(Icons.success);
    }

    /**
     * Resizes an image so that it is no higher than the specified height
     * and no wider than 60 pixels
     * @param image The image to resize
     * @param height The maximum height
     * @return The resized image
     */
    public static Icon resizeImage(ImageIcon image, int height)
    {
        ImageIcon resizedImage = null;

        if (image != null)
        {
            if (image.getIconHeight() > height && image.getIconWidth() > 60)
            {
                resizedImage = new ImageIcon(image.getImage().
                        getScaledInstance(60, height,
                        Image.SCALE_DEFAULT));
            }
            else if (image.getIconHeight() > height)
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

    /**
     * Gets the bytes from the given image
     * @param image The image to get the bytes from
     * @return The bytes of the image
     */
    public static byte[] getBytesFromImage(Image image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(convert(image), "PNG", baos);
        }
        catch (IOException ex)
        {
        }
        catch (InterruptedException ex)
        {
        }

        return baos.toByteArray();
    }

    /**
     * 
     * @param image
     * @return
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static BufferedImage convert(Image image) throws InterruptedException, IOException
    {
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        return bi;
    }

    /**
     * Gets the nickname to use for the given roster entry. First checks if the 
     * local user has given them an alias, then checks if the roster entry has
     * given themselves a nickname. If neither of these then the JID is returned.
     * @param rosterEntry The roster entry
     * @return The nickname of the user
     */
    public static String getNickname(RosterEntry rosterEntry)
    {
        VCard vCard = new VCard();

        if (rosterEntry == null)
        {
            return null;
        }
        if (rosterEntry.getName() != null)
        {
            return rosterEntry.getName();
        }
        try
        {
            vCard.load(ContactListUI.connection, rosterEntry.getUser());
            if (vCard.getNickName() != null && !vCard.getNickName().equals(""))
            {
                return vCard.getNickName();
            }
        }
        catch (Exception ex)
        {
        }

        if (rosterEntry.getUser() != null)
        {
            return rosterEntry.getUser();
        }
        return null;
    }

    /**
     * Gets the nickname of the user with the specified JID
     * @param JID The JID to get the nickname of
     * @return The nickname
     * @see getNickname(RosterEntry rosterEntry)
     */
    public static String getNickname(String JID)
    {
        JID = StringUtils.parseBareAddress(JID);
        return getNickname(ContactListUI.connection.getRoster().getEntry(JID));
    }

    /**
     * Gets the nickname of the local user. First checks the vcard for a nickname,
     * if none is found then the JID is returned
     * @return The nickname
     */
    public static String getNickname()
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(ContactListUI.connection);
            if (vCard.getNickName() != null && !vCard.getNickName().equals(""))
            {
                return vCard.getNickName();
            }
        }
        catch (Exception ex)
        {
        }

        return ContactListUI.connection.getUser();
    }

    /**
     * Gets the status message of the given presence
     * @param presence The presence
     * @return The status message
     */
    public static String getStatusMessage(Presence presence)
    {
        if (presence.getStatus() != null)
        {
            return presence.getStatus();
        }
        else
        {
            return getStatus(presence);
        }
    }

    /**
     * Gets the status of the presence
     * @param presence The presence
     * @return Returns either offline or available
     */
    public static String getStatus(Presence presence)
    {
        if (!presence.isAvailable())
        {
            return "Offline";
        }
        return "Available";
    }

    /**
     * Gets the avatar of the specified roster entry and resizes it
     * @param rosterEntry The user
     * @param height The max height
     * @return The resized avatar icon
     */
    public static Icon getAvatar(RosterEntry rosterEntry, int height)
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(ContactListUI.connection, rosterEntry.getUser());
            return Utils.resizeImage(new ImageIcon(vCard.getAvatar()), height);
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

    /**
     * Gets the avatar of the local user and resizes it
     * @param height The max height
     * @return The resized avatar icon
     */
    public static Icon getAvatar(int height)
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(ContactListUI.connection);
            return Utils.resizeImage(new ImageIcon(vCard.getAvatar()), height);
        }
        catch (XMPPException ex)
        {
        }
        catch (NullPointerException ex)
        {
            // user has no avatar
        }

        return null;
    }

    /**
     * Gets the avatar of the given JID and resizes it
     * @param JID The JID
     * @param height The max height
     * @return The resized avatar icon
     */
    public static Icon getAvatar(String JID, int height)
    {
        return getAvatar(ContactListUI.connection.getRoster().getEntry(StringUtils.parseBareAddress(JID)), height);
    }

    /**
     * Gets the status icon of the given presence
     * @param presence The presence
     * @return The status icon
     */
    public static ImageIcon getUserIcon(Presence presence)
    {
        if (presence.getMode() == Presence.Mode.dnd)
        {
            return Icons.busy;
        }
        if (!presence.isAvailable())
        {
            return Icons.offline;
        }
        if (presence.isAway())
        {
            return Icons.away;
        }

        return Icons.online;
    }

    /**
     * Gets all the stored connections
     * @return An array of connections
     */
    public static Connection[] getConnections()
    {
        Properties properties = new Properties();
        File connectionsDir = new File("connections");

        if (!connectionsDir.isDirectory())
        {
            return new Connection[0];
        }
        String[] connections = connectionsDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String name)
            {
                return name.contains(".properties");
            }
        });

        Connection[] connectionsArray = new Connection[connections.length];
        int i = 0;

        for (String connection : connections)
        {
            try
            {
                properties.load(new FileInputStream("connections/" + connection));
                connectionsArray[i] = new Connection(properties.getProperty("name"),
                        properties.getProperty("username"),
                        properties.getProperty("resource"),
                        properties.getProperty("host"),
                        properties.getProperty("port"));
                i++;
            }
            catch (Exception ex)
            {
            }
        }

        return connectionsArray;
    }

    /**
     * 
     * @param username
     * @param resource
     * @param host
     * @param port
     * @param name
     * @throws java.lang.Exception If the filename is invalid
     */
    public static void saveConnection(String username, String resource, String host, String port, String name) throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty("username", username);
        properties.setProperty("resource", resource);
        properties.setProperty("host", host);
        properties.setProperty("port", port);
        properties.setProperty("name", name);

        (new File("connections")).mkdir();

        try
        {
            properties.store(new FileOutputStream("connections/" + name + ".properties"), PROPERTIES_DESC);
        }
        catch (Exception e)
        {
            throw new Exception("Invalid filename");
        }
    }

    /**
     * 
     * @param name
     */
    public static void deleteConnection(String name)
    {
        new File("connections/" + name + ".properties").delete();
    }

    /**
     * 
     * @param filename
     * @return
     */
    public static Icon getFileIcon(String filename)
    {
        Icon icon = null;

        try
        {
            //Create a temporary file with the specified extension
            File file = File.createTempFile("file", "." + getFileExtension(filename));

            System.out.println(file.getName());

            FileSystemView view = FileSystemView.getFileSystemView();
            icon = view.getSystemIcon(file);

            //Delete the temporary file
            file.delete();
        }
        catch (IOException ex)
        {
        }

        return icon;
    }

    /**
     * 
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename)
    {
        String[] split = filename.split("\\.");

        return split[split.length - 1];
    }

    /**
     * 
     * @param accountName
     * @param property
     * @param value
     */
    public static void saveProperty(String accountName, String property, String value)
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream("connections/" + accountName + ".properties"));
            properties.setProperty(property, value);
            properties.store(new FileOutputStream("connections/" + accountName + ".properties"), PROPERTIES_DESC);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @param accountName
     * @param property
     * @return
     */
    public static String loadProperty(String accountName, String property)
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream("connections/" + accountName + ".properties"));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return properties.getProperty(property, "true");
    }

    /**
     * 
     * @return
     */
    public static Presence.Mode[] getPresenceModes()
    {
        Presence.Mode modes[] =
        {
            Presence.Mode.available, Presence.Mode.away, Presence.Mode.chat, Presence.Mode.dnd, Presence.Mode.xa
        };

        return modes;
    }

    /**
     * 
     * @return
     */
    public static Presence.Type[] getPresenceTypes()
    {
        Presence.Type types[] =
        {
            Presence.Type.available, Presence.Type.unavailable
        };

        return types;
    }

    /**
     * Gets the specified user's group names separated by commas
     * @param entry The user
     * @return The groups separated by commas
     */
    public static String getGroupsCSV(RosterEntry entry)
    {
        String csvGroups = "";

        for (RosterGroup group : entry.getGroups())
        {
            csvGroups += group.getName();
            csvGroups += ", ";
        }

        return csvGroups;
    }

    /**
     * 
     * @param path
     * @param create
     * @throws java.io.IOException
     * @throws java.lang.SecurityException
     */
    public static void openFileBrowser(String path, boolean create) throws IOException, SecurityException
    {
        if(create && !new File(path).exists())
        {
            new File(path).mkdirs();
        }
        if (!new File(path).exists())
        {
            JOptionPane.showMessageDialog(null, "The path could not be found", "Path not found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (getSystem().equals("windows"))
        {
            Runtime.getRuntime().exec("explorer.exe " + path);
            return;
        }
        if(getSystem().equals("linux"))
        {
            try
            {
                Runtime.getRuntime().exec("konqueror file:///" + path);
            }
            catch(IOException ex)
            {
                Runtime.getRuntime().exec("nautilus " + path);
            }
            return;
        }
    }

    /**
     * 
     * @return
     */
    public static String getSystem()
    {
        String os = null;

        if (System.getProperty("os.name").startsWith("Windows"))
        {
            // Windows-specific solution here
            os = "windows";
        }
        else if (System.getProperty("os.name").indexOf("Linux") != -1)
        {
            // Linux-specific solution here
            os = "linux";
        }

        return os;
    }
}
