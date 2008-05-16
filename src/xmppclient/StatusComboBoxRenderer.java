/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmppclient;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
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
        
        if(value instanceof Presence)
        {
            Presence presence = (Presence) value;
            lbl.setIcon(presence.getIcon());
            if(isSelected || cellHasFocus)lbl.setOpaque(true);
            else lbl.setOpaque(false);
        }
        else lbl.setIcon(null);
        
        return lbl;
    }
}
