/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.log;

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
public class Log
{
    public static void log(JTextPane textPane, String contact)
    {
        // remove resource, if present
        contact = StringUtils.parseBareAddress(contact);
        
        try
        {
            new File("logs").mkdir();
            // Create file 
            FileWriter fstream = new FileWriter("logs/" + contact + ".txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            StyledDocument doc = textPane.getStyledDocument();
            out.write("----------");
            out.newLine();
            String text = doc.getText(0, doc.getLength());
            out.write(text);
            out.flush();
            out.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
