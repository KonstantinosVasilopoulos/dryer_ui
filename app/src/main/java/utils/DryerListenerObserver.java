package utils;

import java.util.List;

/**
 * Class acting as the listener's observer. It will accept the listener's output and process the
 * user's voice commands.
 *
 * @see DryerListener
 */
public interface DryerListenerObserver {
    /**
     * Process the results of the listener.
     *
     * @param matches an array containing all the words the listener has matched successfully.
     */
    void listenerUpdated(List<String> matches);
}
