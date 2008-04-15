/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;
import org.jivesoftware.smack.RosterEntry;

/**
 *
 * @author Lee Boynton (323326)
 */
public class TabbedPane extends JTabbedPane
{
    private List<ChatPanel> tabs = new ArrayList<ChatPanel>();
    
    public void add(RosterEntry user)
    {
        //tabs.add(user);
    }
}
