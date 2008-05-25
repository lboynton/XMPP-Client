/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import org.jivesoftware.smack.util.StringUtils;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AccountManager
{
    // default directories which are created for each XMPP connection
    public static final String LOGS_DIR = "logs";
    public static final String RECEIVED_DIR = "received";
    public static final String ROOT_DIR = "accounts";
    public static final String AUDIO_DIR = "audio";
    
    /** The JID to store account details under */
    private String JID;
    private File rootDir;

    public AccountManager(String JID)
    {
        this.JID = StringUtils.parseBareAddress(JID);
        createRootDirectory();
    }

    public File getRootDir()
    {
        return rootDir;
    }

    private void createRootDirectory()
    {
        rootDir = new File(ROOT_DIR + File.separator + File.separator + JID);
        if(!rootDir.exists()) rootDir.mkdirs();
    }

    public File createDirectory(String name)
    {
        File dir = new File(rootDir.getAbsolutePath() + File.separator + name);
        if(!dir.exists()) dir.mkdir();
        return dir;
    }

    public void logConversation(JTextPane textPane, String contact)
    {
        FileWriter fstream = null;
        
        // remove resource, if present
        contact = StringUtils.parseBareAddress(contact);
        
        try
        {
            File dir = createDirectory(LOGS_DIR);
            fstream = new FileWriter(dir.getAbsolutePath() + File.separator + contact + ".txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            StyledDocument doc = textPane.getStyledDocument();
            out.write("----------");
            out.newLine();
            out.write(doc.getText(0, doc.getLength()));
            out.flush();
            out.close();
            fstream.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
