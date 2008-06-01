package xmppclient;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import org.jivesoftware.smack.packet.Presence;
import xmppclient.images.Icons;

/**
 * A combo box renderer for a combo box containing Presence objects. If a Presence
 * object is found it displays the status message and the icon representing the 
 * presence. Also makes sure JSeparators and JDialogs in the combo box display 
 * correctly.
 * @author Lee Boynton (323326)
 */
public class StatusComboBoxRenderer extends BasicComboBoxRenderer
{
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        if(value instanceof JSeparator)
        {
            JSeparator sep = (JSeparator) value;
            sep.setOpaque(true);
            sep.setEnabled(false);
            sep.setBackground(list.getBackground());
            return (Component) value;
        }
        
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if(value instanceof JDialog)
        {
            lbl.setOpaque(true);
            lbl.setIcon(Icons.add);
            return lbl;
        }
        
        if(value instanceof Presence)
        {
            Presence presence = (Presence) value;
            lbl.setIcon(getIcon(presence));
            lbl.setText(presence.getStatus());
            if(isSelected || cellHasFocus)lbl.setOpaque(true);
            else lbl.setOpaque(false);
        }
        else lbl.setIcon(null);
        
        return lbl;
    }
    
    /**
     * Gets the icon representing the presence depending on the presence type 
     * and presence mode
     * @param presence The presence to get the icon for
     * @return The icon which indicates the given presence
     */
    public Icon getIcon(Presence presence)
    {
        if(presence.getType().equals(Presence.Type.available) && presence.getMode().equals(Presence.Mode.available))
        {
            return Icons.online;
        }
        if(presence.getType().equals(Presence.Type.available) && presence.getMode().equals(Presence.Mode.away))
        {
            return Icons.away;
        }
        if(presence.getType().equals(Presence.Type.available) && presence.getMode().equals(Presence.Mode.xa))
        {
            return Icons.away;
        }
        if(presence.getType().equals(Presence.Type.available) && presence.getMode().equals(Presence.Mode.dnd))
        {
            return Icons.busy;
        }
        if(presence.getType().equals(Presence.Type.available) && presence.getMode().equals(Presence.Mode.chat))
        {
            return Icons.online;
        }
        
        return Icons.offline;
    }
}
