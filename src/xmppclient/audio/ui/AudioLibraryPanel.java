/*
 * AudioLibraryUI.java
 *
 * Created on 25 May 2008, 17:00
 */
package xmppclient.audio.ui;

import java.awt.Component;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import xmppclient.audio.*;
import xmppclient.audio.packet.Audio;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.jivesoftware.smack.RosterEntry;
import xmppclient.ContactListUI;
import xmppclient.audio.AudioManager;
import xmppclient.images.tango.TangoIcons;
import xmppclient.jingle.IncomingSession;
import xmppclient.jingle.JingleManager;
import xmppclient.jingle.JingleSessionRequest;
import xmppclient.jingle.JingleSessionRequestListener;

/**
 *
 * @author  Lee Boynton (323326)
 */
public class AudioLibraryPanel extends javax.swing.JPanel implements AudioResponseListener, BasicPlayerListener
{
    private RosterEntry entry;
    private AudioManager audioManager;
    private AudioMessage response;
    private JingleManager jingleManager;
    private String show;
    private boolean connected = false;
    private IncomingSession session;

    /** Creates new form AudioLibraryUI */
    public AudioLibraryPanel(AudioManager audioManager, RosterEntry entry, JingleManager jingleManager)
    {
        this.audioManager = audioManager;
        this.entry = entry;
        this.jingleManager = jingleManager;
        initComponents();
        audioManager.addResponseListener(this);
        jingleManager.addSessionRequestListener(new JingleSessionRequestListener()
        {
            @Override
            public void sessionRequested(JingleSessionRequest request)
            {
                session = request.accept();
                connected = true;
                session.getPlayer().addBasicPlayerListener(AudioLibraryPanel.this);
                stopButton.setEnabled(true);
                playButton.setIcon(TangoIcons.pause16x16);
                try
                {
                    session.getPlayer().setGain(new Double(volumeSlider.getValue()) / 100);
                }
                catch (BasicPlayerException ex){}
            }
        });
    }

    public void refresh()
    {
        audioManager.sendRequest(ContactListUI.connection.getRoster().getPresence(entry.getUser()).getFrom());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        audioList = new javax.swing.JList();
        artistButton = new javax.swing.JButton();
        albumButton = new javax.swing.JButton();
        allButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        volumeSlider = new javax.swing.JSlider();

        audioList.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        audioList.setCellRenderer(new LibraryListRenderer());
        audioList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                audioListMouseClicked(evt);
            }
        });
        audioList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                audioListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(audioList);

        artistButton.setText("Artist");
        artistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artistButtonActionPerformed(evt);
            }
        });

        albumButton.setText("Album");
        albumButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                albumButtonActionPerformed(evt);
            }
        });

        allButton.setText("<");
        allButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allButtonActionPerformed(evt);
            }
        });

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/media-playback-start16x16.png"))); // NOI18N
        playButton.setEnabled(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xmppclient/images/tango/media-playback-stop16x16.png"))); // NOI18N
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setToolTipText("Volume");
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(allButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(artistButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(albumButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(playButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(artistButton)
                    .addComponent(albumButton)
                    .addComponent(allButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void allButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allButtonActionPerformed
    showAll();
}//GEN-LAST:event_allButtonActionPerformed

private void artistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_artistButtonActionPerformed
    showArtist(artistButton.getText());
}//GEN-LAST:event_artistButtonActionPerformed

private void albumButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_albumButtonActionPerformed
    showAlbum(albumButton.getText());
}//GEN-LAST:event_albumButtonActionPerformed

private void audioListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioListMouseClicked
    if (evt.getClickCount() == 2)
    {
        if (audioList.getSelectedValue() instanceof AudioFile)
        {
            sendFileRequest();
            return;
        }
        if (show.equals("artist"))
        {
            showAlbum((String) audioList.getSelectedValue());
        }
        else if (show.equals("all"))
        {
            showArtist((String) audioList.getSelectedValue());
        }
    }
}//GEN-LAST:event_audioListMouseClicked

private void audioListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_audioListValueChanged
    if (audioList.getSelectedValue() instanceof AudioFile)
    {
        playButton.setEnabled(true);
    }
    else
    {
        playButton.setEnabled(false);
    }
}//GEN-LAST:event_audioListValueChanged

private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
    if (connected)
    {
        try
        {
            if (session.getPlayer().getStatus() == BasicPlayer.PLAYING)
            {
                session.getControl().pause();
            }
            else if (session.getPlayer().getStatus() == BasicPlayer.PAUSED)
            {
                session.getControl().resume();
            }
        }
        catch (BasicPlayerException ex)
        {
            Logger.getLogger(AudioLibraryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else
    {
        sendFileRequest();
    }
}//GEN-LAST:event_playButtonActionPerformed

private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
    try
    {//GEN-LAST:event_stopButtonActionPerformed
        session.getControl().stop();
        session.terminate();
        session.getPlayer().removeBasicPlayerListener(this);
    }
    catch (BasicPlayerException ex)
    {
        Logger.getLogger(AudioLibraryPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    connected = false;
    stopButton.setEnabled(false);
    playButton.setIcon(TangoIcons.play16x16);
}

private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
    try
    {//GEN-LAST:event_volumeSliderStateChanged
        session.getControl().setGain(new Double(volumeSlider.getValue()) / 100);
    }
    catch (Exception ex)
    {
        // not playing
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton albumButton;
    private javax.swing.JButton allButton;
    private javax.swing.JButton artistButton;
    private javax.swing.JList audioList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton playButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JSlider volumeSlider;
    // End of variables declaration//GEN-END:variables
    @Override
    public void audioResponse(final AudioMessage response)
    {
        System.out.println("Audio library response received");
        this.response = response;
        showAll();
    }

    private void showAlbum(String album)
    {
        albumButton.setText(album);
        artistButton.setVisible(true);
        albumButton.setVisible(true);
        show = "album";

        DefaultListModel model = new DefaultListModel();
        Audio audio = response.getAudio();

        for (AudioFile file : audio.getAudioFiles())
        {
            if (file.getAlbum().equals(album))
            {
                model.addElement(file);
            }
        }

        audioList.setModel(model);
    }

    private void showAll()
    {
        show = "all";
        artistButton.setVisible(false);
        albumButton.setVisible(false);
        DefaultListModel model = new DefaultListModel();
        Audio audio = response.getAudio();

        for (AudioFile file : audio.getAudioFiles())
        {
            if (!model.contains(file.getArtist()))
            {
                model.addElement(file.getArtist());
            }
        }

        audioList.setModel(model);
    }

    private void showArtist(String artist)
    {
        artistButton.setVisible(true);
        albumButton.setVisible(false);
        artistButton.setText(artist);
        show = "artist";

        DefaultListModel model = new DefaultListModel();
        Audio audio = response.getAudio();

        for (AudioFile file : audio.getAudioFiles())
        {
            if (file.getArtist().equals(artist) && !model.contains(file.getAlbum()))
            {
                model.addElement(file.getAlbum());
            }
        }

        audioList.setModel(model);
    }

    private void sendFileRequest()
    {
        AudioFile file;

        if (audioList.getSelectedValue() instanceof AudioFile)
        {
            file = (AudioFile) audioList.getSelectedValue();
        }
        else
        {
            return;
        }
        System.out.printf("Requesting file: %s\n", file.toString());

        audioManager.sendFileRequest(file, ContactListUI.connection.getRoster().getPresence(entry.getUser()).getFrom());
    }

    public class LibraryListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof AudioFile)
            {
                AudioFile file = (AudioFile) value;

                lbl.setText(file.getTrack() + ". " + file.getName());
                lbl.setIcon(TangoIcons.audio16x16);
            }

            return lbl;
        }
    }

    @Override
    public void opened(Object stream, Map properties)
    {
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
    {
    }

    @Override
    public void stateUpdated(BasicPlayerEvent event)
    {
        if (event.getCode() == BasicPlayerEvent.RESUMED)
        {
            playButton.setIcon(TangoIcons.pause16x16);
        }
        if (event.getCode() == BasicPlayerEvent.PAUSED)
        {
            playButton.setIcon(TangoIcons.play16x16);
        }
    }

    @Override
    public void setController(BasicController controller)
    {
    }
}
