package xmppclient.emoticons;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * This class stores the default emoticon set
 * @author Lee Boynton (323326)
 */
public class Emoticons
{
    /**
     * An angel emoticon
     */
    public static final ImageIcon angel = new javax.swing.ImageIcon(Emoticons.class.getResource("face-angel.png"));
    /**
     * A crying face emoticon
     */
    public static final ImageIcon crying = new javax.swing.ImageIcon(Emoticons.class.getResource("face-crying.png"));
    /**
     * A devil emoticon
     */
    public static final ImageIcon devilish = new javax.swing.ImageIcon(Emoticons.class.getResource("face-devilish.png"));
    /**
     * A face with glasses emoticon
     */
    public static final ImageIcon glasses = new javax.swing.ImageIcon(Emoticons.class.getResource("face-glasses.png"));
    /**
     * A face with a grin emoticon
     */
    public static final ImageIcon grin = new javax.swing.ImageIcon(Emoticons.class.getResource("face-grin.png"));
    /**
     * A kissing emoticon
     */
    public static final ImageIcon kiss = new javax.swing.ImageIcon(Emoticons.class.getResource("face-kiss.png"));
    /**
     * A monkey emoticon
     */
    public static final ImageIcon monkey = new javax.swing.ImageIcon(Emoticons.class.getResource("face-monkey.png"));
    /**
     * A plain face emoticon
     */
    public static final ImageIcon plain = new javax.swing.ImageIcon(Emoticons.class.getResource("face-plain.png"));
    /**
     * A sad face emoticon
     */
    public static final ImageIcon sad = new javax.swing.ImageIcon(Emoticons.class.getResource("face-sad.png"));
    /**
     * A smiley face emoticon
     */
    public static final ImageIcon smile = new javax.swing.ImageIcon(Emoticons.class.getResource("face-smile.png"));
    /**
     * A face with a big smile emoticon
     */
    public static final ImageIcon smileBig = new javax.swing.ImageIcon(Emoticons.class.getResource("face-smile-big.png"));
    /**
     * A surprised face emoticon
     */
    public static final ImageIcon surprise = new javax.swing.ImageIcon(Emoticons.class.getResource("face-surprise.png"));
    /**
     * A winking face emoticon
     */
    public static final ImageIcon wink = new javax.swing.ImageIcon(Emoticons.class.getResource("face-wink.png"));
    /**
     * A face with sticking out tongue emoticon
     */
    public static final ImageIcon tongue = new javax.swing.ImageIcon(Emoticons.class.getResource("emoticon_tongue.png"));
    private static List<Emoticon> emoticons;
    
    static
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
        emoticons.add(new Emoticon("tongue", tongue, ":P"));
    }
    
    /**
     * Gets the default emoticons as a list
     * @return The list of default emoticons
     */
    public static List<Emoticon> getDefaultEmoticons()
    {
        return emoticons;
    }
}
