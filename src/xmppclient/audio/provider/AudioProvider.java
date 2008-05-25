/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient.audio.provider;

import xmppclient.audio.*;
import xmppclient.audio.packet.Audio;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author Lee Boynton (323326)
 */
public class AudioProvider implements IQProvider
{
    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception
    {
        Audio audio = new Audio();
        
        audio.setAId(parser.getAttributeValue("", "aid"));

        while(true)
        {
            int eventType = parser.next();
            String elementName = parser.getName();
            
            if (eventType == XmlPullParser.START_TAG)
            {
                if (elementName.equals("file"))
                {
                    String artist = parser.getAttributeValue("", "artist");
                    String album = parser.getAttributeValue("", "album");
                    String track = parser.getAttributeValue("", "track");
                    String name = parser.nextText();
                    audio.addFile(new AudioFile(name, artist, album, track));
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (elementName.equals(Audio.ELEMENTNAME))
                {
                    break;
                }
            }
        }
        
        return audio;
    }
    
    public static void main(String args[])
    {
        try
        {
            AudioFile file1 = new AudioFile("test.txt");
            AudioFile file2 = new AudioFile("bleh.mp3");
            AudioFile file3 = new AudioFile("fuck.txt");
            List<AudioFile> files = new ArrayList<AudioFile>();
            files.add(file1);
            files.add(file2);
            files.add(file3);
            Audio audio = new Audio(files);
            audio.setAId(StringUtils.randomString(5));
            
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            System.out.printf("Input: %s\n", audio.getChildElementXML());
            xpp.setInput(new StringReader(audio.getChildElementXML()));
            Audio outFile = (Audio) new AudioProvider().parseIQ(xpp);
            System.out.printf("Output: %s\n", outFile.getChildElementXML());
        }
        catch (Exception ex)
        {
            Logger.getLogger(AudioProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
