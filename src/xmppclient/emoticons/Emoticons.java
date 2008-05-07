/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient.emoticons;

import java.util.ArrayList;
import java.util.List;
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
    
    private static List<Emoticon> emoticons = new ArrayList<Emoticon>();
    
    static
    {
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

    public static List<Emoticon> getEmoticons()
    {
        return emoticons;
    }
}
