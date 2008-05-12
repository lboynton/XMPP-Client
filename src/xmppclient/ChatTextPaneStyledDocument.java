/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Lee Boynton (323326)
 */
public class ChatTextPaneStyledDocument extends DefaultStyledDocument
{
    private String lastUser = "";
    
    /**
     * Adds default styles
     */
    public ChatTextPaneStyledDocument()
    {
        // create the default style
        Style def = super.addStyle("default", null);
        
        // create the nickname style
        Style nickname = super.addStyle("nickname", def);
        StyleConstants.setBold(nickname, true);
        
        Style info = super.addStyle("info", def);
        StyleConstants.setItalic(info, true);
        StyleConstants.setFontSize(info, 10);
    }
    
    public void insertTime(String style)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        String time = sdf.format(cal.getTime());
        
        try
        {

            super.insertString(super.getLength(), "(" + time + ") ", super.getStyle(style));
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertUser(String nickname)
    {
        if(lastUser.equals(nickname)) return;
        
        try
        {
            insertTime("default");
            super.insertString(super.getLength(), nickname + ": ", super.getStyle("nickname"));
            lastUser = nickname;
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertMessage(String message)
    {
        try
        {
            super.insertString(super.getLength(), message + "\n", super.getStyle("default"));
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertInfo(String message)
    {
        try
        {
            insertTime("info");
            super.insertString(super.getLength(), message + "\n", super.getStyle("info"));
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
