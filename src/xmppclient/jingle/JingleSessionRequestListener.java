package xmppclient.jingle;

/**
 *  A listener interface for Jingle file transfer requests.
 * @author Lee Boynton (323326)
 */
public interface JingleSessionRequestListener
{
    /**
     * This method is triggered every time a Jingle file transfer request is
     * received by a Jingle Manager.
     * @param request
     */
    public void sessionRequested(JingleSessionRequest request);
}
