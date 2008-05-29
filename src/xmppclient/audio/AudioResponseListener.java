package xmppclient.audio;

/**
 * A listener for audio library responses
 * @author Lee Boynton (323326)
 */
public interface AudioResponseListener
{
    /**
     * This method is triggered every time an audio library response is received
     * @param response The audio message response
     */
    public void audioResponse(AudioMessage response);
}
