/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import xmppclient.emoticons.Emoticon;
import xmppclient.emoticons.Emoticons;
import xmppclient.formatter.Format;

/**
 * Styled Document for one-to-one and multi user chats. Provides methods for
 * inserting nicknames, the time and messages using appropriate styles.
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

    /**
     * Inserts the current day and time into the document using the specified style
     * @param style The style to use
     */
    public void insertTime(String style)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

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

    /**
     * Inserts the nickname using the nickname style
     * @param nickname The nickname
     */
    public void insertUser(String nickname)
    {
        if (lastUser.equals(nickname))
        {
            return;
        }
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

    /**
     * Inserts a chat message into the document using the default style.
     * The message will be followed by a linebreak.
     * @param message The message to insert
     */
    public void insertMessage(String message)
    {
        insertMessage(message, super.getStyle("default"));
    }

    /**
     * Turns the specified format into a style and adds it to the document. 
     * Inserts the chat message into the document using the new style.
     * The message will be followed by a linebreak.
     * @param message The message to insert
     * @param format The format to derive the style from
     */
    public void insertMessage(String message, Format format)
    {
        // create a new style for the formatted text
        Style newStyle = super.addStyle("newStyle", null);
        StyleConstants.setFontFamily(newStyle, format.getFont().getFamily());
        StyleConstants.setFontSize(newStyle, format.getFont().getSize());
        StyleConstants.setForeground(newStyle, format.getColor());
        StyleConstants.setItalic(newStyle, format.getFont().isItalic());
        StyleConstants.setBold(newStyle, format.getFont().isBold());
        insertMessage(message, newStyle);
        super.removeStyle("newStyle");
    }

    /**
     * Inserts a chat message into the document using the specified style. 
     * The message will be followed by a linebreak.
     * @param message
     * @param style
     */
    public void insertMessage(String message, Style style)
    {
        try
        {
            int start = super.getLength();
            super.insertString(super.getLength(), message + "\n", style);
            int end = super.getLength();
            showEmoticons(start, end);
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Used for inserting information messages into the document
     * @param message The information message
     */
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

    /**
     * Recursively goes through the document from the start location to the end
     * location given and converts any emoticon character sequences into the
     * appropriate emoticon icon
     * @param start The start location
     * @param end The end location
     */
    private void showEmoticons(int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            for (Emoticon e : Emoticons.getEmoticons())
            {
                try
                {
                    if ((i + e.getSequence().length()) > end)
                    {
                        continue;
                    }

                    String newString = super.getText(i, e.getSequence().length());

                    if (newString.equals(e.getSequence()))
                    {
                        super.remove(i, e.getSequence().length());
                        SimpleAttributeSet smi = new SimpleAttributeSet();
                        StyleConstants.setIcon(smi, e.getIcon());
                        super.insertString(i, e.getSequence(), smi);

                        i += e.getSequence().length() - 1;
                        break;
                    }
                }
                catch (BadLocationException ex)
                {
                    Logger.getLogger(ChatTextPaneStyledDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
