/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmppclient.tabs;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * A horizontal renderer for JLists to be used as tabs.
 * @author Lee Boynton (323326)
 */
public class Renderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel item = (JLabel) value;
        
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        lbl.setText(item.getText());
        lbl.setIcon(item.getIcon());
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lbl.setPreferredSize(new Dimension(list.getSize().height, list.getSize().height));
        return lbl;
    }
}
