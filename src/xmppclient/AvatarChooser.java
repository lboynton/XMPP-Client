/*
 * AvatarChooser.java
 *
 * Created on 29 April 2008, 10:49
 */
package xmppclient;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

/**
 * Displays a file chooser for choosing an avatar. Image files are filtered
 * and a preview of the image is shown in the file chooser accessory
 * @author Lee Boynton (323326)
 */
public class AvatarChooser extends javax.swing.JDialog
{
    private XMPPClientUI clientUI;

    /** Creates new form AvatarChooser */
    public AvatarChooser(JFrame owner, boolean modal)
    {
        super(owner, "Choose Avatar", modal);
        clientUI = (XMPPClientUI) owner;
        initComponents();
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        avatarFileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        avatarFileChooser.setAcceptAllFileFilterUsed(false);
        avatarFileChooser.setAccessory(new ImagePreview(avatarFileChooser));
        avatarFileChooser.setCurrentDirectory(new java.io.File("F:\\Pictures"));
        avatarFileChooser.setDialogTitle("Select Avatar");
        avatarFileChooser.setFileFilter(new ImageFilter());
        avatarFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avatarFileChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(avatarFileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(avatarFileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void avatarFileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avatarFileChooserActionPerformed

    if (evt.getActionCommand().equals("ApproveSelection"))
    {
        this.setVisible(false);
        new AvatarSwingWorker().execute();
    }
    else
    {
        dispose();
    }
}//GEN-LAST:event_avatarFileChooserActionPerformed

    /**
     * Filters out all files except image files
     */
    public class ImageFilter extends FileFilter
    {
        /**
         * Accepts image files
         * @param f The file
         * @return True if file is an image, false otherwise
         */
        @Override
        public boolean accept(File f)
        {
            if (f.isDirectory())
            {
                return true;
            }

            String extension = Utils.getExtension(f);
            if (extension != null)
            {
                if (extension.equals("gif") ||
                        extension.equals("jpeg") ||
                        extension.equals("jpg") ||
                        extension.equals("bmp") ||
                        extension.equals("png"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            return false;
        }

        /**
         * The description of the filter that is displayed
         * @return The description
         */
        @Override
        public String getDescription()
        {
            return "Images";
        }
    }

    /**
     * Displays a thumbnail of the selected image
     */
    public class ImagePreview extends JComponent
            implements PropertyChangeListener
    {
        private ImageIcon thumbnail = null;
        private File file = null;
        private int width = 150;
        private int height = 100;

        /**
         * The file chooser which will be selecting the image file
         * @param fc
         */
        public ImagePreview(JFileChooser fc)
        {
            setPreferredSize(new Dimension(width + 10, height + 10));
            fc.addPropertyChangeListener(this);
        }

        /**
         * Generates the thumbnail from the image file
         */
        public void loadImage()
        {
            if (file == null)
            {
                thumbnail = null;
                return;
            }

            //Don't use createImageIcon (which is a wrapper for getResource)
            //because the image we're trying to load is probably not one
            //of this program's own resources.
            ImageIcon tmpIcon = new ImageIcon(file.getPath());
            if (tmpIcon != null)
            {
                if (tmpIcon.getIconWidth() > width)
                {
                    thumbnail = new ImageIcon(tmpIcon.getImage().
                            getScaledInstance(width, -1,
                            Image.SCALE_DEFAULT));
                }
                else
                { //no need to miniaturize

                    thumbnail = tmpIcon;
                }
            }
        }

        /**
         * Fired when a new selection is made in the file chooser.
         * Causes a thumbnail to be displayed if an image is selected
         * @param e
         */
        @Override
        public void propertyChange(PropertyChangeEvent e)
        {
            boolean update = false;
            String prop = e.getPropertyName();

            //If the directory changed, don't show an image.
            if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop))
            {
                file = null;
                update = true;

            //If a file became selected, find out which one.
            }
            else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop))
            {
                file = (File) e.getNewValue();
                update = true;
            }

            //Update the preview accordingly.
            if (update)
            {
                thumbnail = null;
                if (isShowing())
                {
                    loadImage();
                    repaint();
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            if (thumbnail == null)
            {
                loadImage();
            }
            if (thumbnail != null)
            {
                int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
                int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

                if (y < 0)
                {
                    y = 0;
                }

                if (x < 5)
                {
                    x = 5;
                }
                thumbnail.paintIcon(this, g, x, y);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser avatarFileChooser;
    // End of variables declaration//GEN-END:variables

    /**
     * A SwingWorker for resizing the avatar and sending it to the server
     */
    private class AvatarSwingWorker extends SwingWorker<Object, Object>
    {
        @Override
        protected Object doInBackground()
        {
            try
            {
                VCard vCard = new VCard();
                vCard.load(XMPPClientUI.connection);
                ImageIcon image = new ImageIcon(avatarFileChooser.getSelectedFile().toURI().toURL());
                image = (ImageIcon) Utils.resizeImage(image, 48);
                clientUI.setAvatar(image);
                vCard.setAvatar(Utils.getBytesFromImage(image.getImage()));

                // Change packet reply timeout to cater for larger avatars
                int oldTimeout = SmackConfiguration.getPacketReplyTimeout();
                SmackConfiguration.setPacketReplyTimeout(15000);

                // save vcard
                vCard.save(XMPPClientUI.connection);

                // reset packet reply timeout
                SmackConfiguration.setPacketReplyTimeout(oldTimeout);
            }
            catch (MalformedURLException ex)
            {
                JOptionPane.showMessageDialog(AvatarChooser.this,
                        "Could not find the following file\n" +
                        avatarFileChooser.getSelectedFile().getAbsolutePath(),
                        "Invalid Path",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch (XMPPException ex)
            {
                JOptionPane.showMessageDialog(AvatarChooser.this,
                        "The avatar could not be saved on the server\n" +
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            return null;
        }

        @Override
        protected void done()
        {
            AvatarChooser.this.dispose();
        }
    }
}
