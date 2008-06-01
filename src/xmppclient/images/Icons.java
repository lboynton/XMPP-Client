package xmppclient.images;

import javax.swing.ImageIcon;

/**
 * Icons from the FamFamFam Silk icon set
 * http://www.famfamfam.com/lab/icons/silk/
 * @author Lee Boynton (323326)
 */
public class Icons
{
    /**
     * Represents the busy status of a user
     */
    public static final ImageIcon busy = new javax.swing.ImageIcon(Icons.class.getResource("status_busy.png"), "Busy status icon");
    /**
     * Represents the offline status of a user
     */
    public static final ImageIcon offline = new javax.swing.ImageIcon(Icons.class.getResource("status_offline.png"), "Offline status icon");
    /**
     * Represents the away status of a user
     */
    public static final ImageIcon away = new javax.swing.ImageIcon(Icons.class.getResource("status_away.png"), "Away status icon");
    /**
     * Represents the online/available status of a user
     */
    public static final ImageIcon online = new javax.swing.ImageIcon(Icons.class.getResource("status_online.png"), "Online status icon");
    /**
     * An error icon
     */
    public static final ImageIcon error = new javax.swing.ImageIcon(Icons.class.getResource("error.png"), "Error icon");
    /**
     * A success icon
     */
    public static final ImageIcon success = new javax.swing.ImageIcon(Icons.class.getResource("accept.png"), "Success icon");
    /**
     * A vcard icon
     */
    public static final ImageIcon vcard = new javax.swing.ImageIcon(Icons.class.getResource("vcard.png"), "VCard icon");
    /**
     * A user comment icon
     */
    public static final ImageIcon userComment = new javax.swing.ImageIcon(Icons.class.getResource("user_comment.png"), "User comment icon");
    /**
     * A add symbol icon
     */
    public static final ImageIcon add = new javax.swing.ImageIcon(Icons.class.getResource("add.png"), "Add icon");
    /**
     * A delete icon
     */
    public static final ImageIcon delete = new javax.swing.ImageIcon(Icons.class.getResource("delete.png"), "Delete icon");
}
