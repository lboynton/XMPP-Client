package xmppclient.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPException;
import xmppclient.audio.packet.Audio;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import xmppclient.audio.provider.AudioProvider;
import xmppclient.jingle.JingleManager;

/**
 * The audio manager handles requests and responses for the audio library
 * @author Lee Boynton (323326)
 */
public class AudioManager
{
    private XMPPConnection connection;
    private AudioLibrary library;
    private List<AudioRequestListener> audioRequestListeners;
    private List<AudioResponseListener> audioResponseListeners;
    
    static
    {
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addIQProvider(Audio.ELEMENTNAME, Audio.NAMESPACE, new AudioProvider());

        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
        {
            @Override
            public void connectionCreated(XMPPConnection connection)
            {
                AudioManager.setServiceEnabled(connection, true);
            }
        });
    }

    /**
     * Creates a new audio manager for handling sending and receiving of audio
     * library requests and responses
     * @param connection The XMPP connection the manager should be associated with
     * @param libraryPath The path to the audio library containing the audio files
     * @param jingleManager The Jingle Manager that will handle sending and
     * receiving audio streams
     */
    public AudioManager(final XMPPConnection connection, final String libraryPath, final JingleManager jingleManager)
    {
        this.connection = connection;
        library = new AudioLibrary(libraryPath);
        this.addRequestListener(new AudioRequestListener()
        {
            @Override
            public void audioRequested(AudioMessage request)
            {
                // both audio library and file requests could come through as these
                // aren't filtered yet
                if(request.getAudio().getAudioType().equals(Audio.AudioType.LIBRARY))
                {
                    System.out.println("Audio library requested");

                    library.generateListing();
                    Audio audio = new Audio(library.getAudioFiles());
                    audio.setFrom(connection.getUser());
                    audio.setTo(request.getFrom());
                    connection.sendPacket(audio);
                }

                if(request.getAudio().getAudioType().equals(Audio.AudioType.FILE))
                {
                    System.out.println("Received file request");

                    File file = library.getFile(request.getAudio().getAudioFile().getId());
                    
                    if (file == null)
                    {
                        System.out.println("Could not find the requested file");
                        return;
                    }
                    
                    try
                    {
                        jingleManager.createOutgoingSession(request.getAudio().getFrom(), file.getAbsolutePath());
                    }
                    catch (XMPPException ex)
                    {
                        Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (FileNotFoundException ex)
                    {
                        Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * Used to enable or disable the audio library feature for the given XMPP connection
     * @param connection The XMPP connection
     * @param enabled True to enable, false otherwise
     */
    public synchronized static void setServiceEnabled(XMPPConnection connection, boolean enabled)
    {
        if (isServiceEnabled(connection) == enabled)
        {
            return;
        }

        if (enabled)
        {
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Audio.NAMESPACE);
        }
        else
        {
            ServiceDiscoveryManager.getInstanceFor(connection).removeFeature(Audio.NAMESPACE);
        }
    }

    /**
     * Determines if the audio library feature is enabled for the given XMPP connection instance
     * @param connection The connection to query
     * @return True if enabled, false otherwise
     */
    public static boolean isServiceEnabled(XMPPConnection connection)
    {
        return ServiceDiscoveryManager.getInstanceFor(connection).includesFeature(
                Audio.NAMESPACE);
    }

    /**
     * Sends a request for the given JID's audio library
     * @param JID The JID the request should be sent to
     */
    public void sendRequest(String JID)
    {
        Audio request = new Audio();
        request.setTo(JID);
        request.setFrom(connection.getUser());
        request.setAudioType(Audio.AudioType.LIBRARY);
        connection.sendPacket(request);
    }

    /**
     * Adds a response listener which will be notified every time a response is
     * received from an audio library request
     * @param responseListener The response listener to add
     */
    public void addResponseListener(AudioResponseListener responseListener)
    {
        if (responseListener != null)
        {
            if (audioResponseListeners == null)
            {
                initAudioResponseListeners();
            }
            synchronized (responseListener)
            {
                audioResponseListeners.add(responseListener);
            }
        }
    }

    /**
     * Adds an audio library request listener which will be notified every time
     * a request is received
     * @param requestListener The request listener to add
     */
    public void addRequestListener(AudioRequestListener requestListener)
    {
        if (requestListener != null)
        {
            if (audioRequestListeners == null)
            {
                initAudioRequestListeners();
            }
            synchronized (requestListener)
            {
                audioRequestListeners.add(requestListener);
            }
        }
    }

    private void initAudioRequestListeners()
    {
        PacketFilter initRequestFilter = new PacketFilter()
        {
            // Return true if we accept this packet
            @Override
            public boolean accept(Packet packet)
            {
                if (packet instanceof IQ)
                {
                    IQ iq = (IQ) packet;

                    if (iq.getType().equals(IQ.Type.GET))
                    {
                        if (iq instanceof Audio)
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        };

        audioRequestListeners = new ArrayList<AudioRequestListener>();

        // add a packet listener for audio request packets
        connection.addPacketListener(new PacketListener()
        {
            @Override
            public void processPacket(Packet packet)
            {
                triggerAudioRequested((Audio) packet);
            }
        }, initRequestFilter);
    }

    private void initAudioResponseListeners()
    {
        PacketFilter initResponseFilter = new PacketFilter()
        {
            // Return true if we accept this packet
            @Override
            public boolean accept(Packet packet)
            {
                if (packet instanceof IQ)
                {
                    IQ iq = (IQ) packet;

                    if (iq.getType().equals(IQ.Type.SET))
                    {
                        if (iq instanceof Audio)
                        {
                            return true; 
                        }
                    }
                }
                return false;
            }
        };

        audioResponseListeners = new ArrayList<AudioResponseListener>();

        // add a packet listener for audio response packets
        connection.addPacketListener(new PacketListener()
        {
            @Override
            public void processPacket(Packet packet)
            {
                triggerAudioResponse((Audio) packet);
            }
        }, initResponseFilter);
    }

    private void triggerAudioRequested(Audio audio)
    {
        AudioRequestListener[] audioRequestListeners = null;

        // Make a synchronized copy of the audio reqest listeners
        synchronized (this.audioRequestListeners)
        {
            audioRequestListeners = new AudioRequestListener[this.audioRequestListeners.size()];
            this.audioRequestListeners.toArray(audioRequestListeners);
        }

        // ... and let them know of the event
        AudioMessage request = new AudioMessage(this, audio);
        for (int i = 0; i < audioRequestListeners.length; i++)
        {
            audioRequestListeners[i].audioRequested(request);
        }
    }

    private void triggerAudioResponse(Audio audio)
    {
        AudioResponseListener[] audioResponseListeners = null;

        // Make a synchronized copy of the audio response listeners
        synchronized (this.audioResponseListeners)
        {
            audioResponseListeners = new AudioResponseListener[this.audioResponseListeners.size()];
            this.audioResponseListeners.toArray(audioResponseListeners);
        }

        // ... and let them know of the event
        AudioMessage response = new AudioMessage(this, audio);
        for (int i = 0; i < audioResponseListeners.length; i++)
        {
            audioResponseListeners[i].audioResponse(response);
        }
    }

    /**
     * Sends a file stream request
     * @param file The file to request
     * @param JID The JID to send the request to
     */
    public void sendFileRequest(AudioFile file, String JID)
    {
        Audio request = new Audio(Audio.AudioType.FILE, IQ.Type.GET);
        request.setTo(JID);
        request.addFile(file);
        connection.sendPacket(request);
    }
}
