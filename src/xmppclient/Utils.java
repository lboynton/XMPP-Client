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

    public static void createErrorLabel(JLabel label, String message)
    {
        label.setText(message);
        label.setIcon(Icons.error);
    }

    public static void createSuccessLabel(JLabel label, String message)
    {
        label.setText(message);
        label.setIcon(Icons.success);
    }

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

    public static BufferedImage convert(Image im) throws InterruptedException, IOException
    {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

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
            vCard.load(XMPPClientUI.connection, rosterEntry.getUser());
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

    public static String getNickname(String JID)
    {
        JID = StringUtils.parseBareAddress(JID);
        return getNickname(XMPPClientUI.connection.getRoster().getEntry(JID));
    }

    public static String getNickname()
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(XMPPClientUI.connection);
            if (vCard.getNickName() != null && !vCard.getNickName().equals(""))
            {
                return vCard.getNickName();
            }
        }
        catch (Exception ex)
        {
        }

        return XMPPClientUI.connection.getUser();
    }

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

    public static String getStatus(Presence presence)
    {
        if (!presence.isAvailable())
        {
            return "Offline";
        }
        return "Available";
    }

    public static Icon getAvatar(RosterEntry rosterEntry, int height)
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(XMPPClientUI.connection, rosterEntry.getUser());
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

    public static Icon getAvatar(int height)
    {
        VCard vCard = new VCard();

        try
        {
            vCard.load(XMPPClientUI.connection);
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

    public static Icon getAvatar(String JID, int height)
    {
        return getAvatar(XMPPClientUI.connection.getRoster().getEntry(StringUtils.parseBareAddress(JID)), height);
    }

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

    public static void deleteConnection(String name)
    {
        new File("connections/" + name + ".properties").delete();
    }

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

    public static String getFileExtension(String filename)
    {
        String[] split = filename.split("\\.");

        return split[split.length - 1];
    }

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

    public static Presence.Mode[] getPresenceModes()
    {
        Presence.Mode modes[] =
        {
            Presence.Mode.available, Presence.Mode.away, Presence.Mode.chat, Presence.Mode.dnd, Presence.Mode.xa
        };

        return modes;
    }

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
            Runtime.getRuntime().exec("konqueror file:///" + path);
            return;
        }
    }

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
