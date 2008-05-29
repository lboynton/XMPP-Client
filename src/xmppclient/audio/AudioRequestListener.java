package xmppclient.audio;

/**
 * A listener for audio library requests
 * @author Lee Boynton (323326)
 */
public interface AudioRequestListener
{
    /**
     * This method is triggered every time an audio library request is received
     * @param request The audio message request
     */
    public void audioRequested(AudioMessage request);
}
