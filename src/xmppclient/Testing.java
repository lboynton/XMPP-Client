package xmppclient;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
 
class Testing extends JFrame
{
  String[] cities = {"London","Madrid","New York","Rome","Sydney","Toronto","Washington"};
  DefaultListModel listModel = new DefaultListModel();
  JList list = new JList(listModel);
  int currentSelection = -1;
  public Testing()
  {
    setLocation(400,300);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    list.setCellRenderer(new MyRenderer());
    for(int x = 0; x < cities.length; x++) listModel.addElement(cities[x]);
    JScrollPane sp = new JScrollPane(list);
    sp.setPreferredSize(new Dimension(100,150));
    getContentPane().add(sp);
    pack();
    list.addListSelectionListener(new ListSelectionListener(){
      public void valueChanged(ListSelectionEvent lse){
        //if(lse.getValueIsAdjusting() == false) list.setCellRenderer(new MyRenderer());}});//reset
        //this block comes from this recent post, and would be better
          //http://saloon.javaranch.com/cgi-bin/ubb/ultimatebb.cgi?ubb=get_topic&f=2&t=006729
          //comment out the above reset line, and uncomment this block
          for(int x = 0; x < listModel.size(); x++)
          {
            listModel.setElementAt(listModel.getElementAt(x),x);
          }
        }});
        
  }
  public static void main(String[] args){new Testing().setVisible(true);}
}
class MyRenderer extends DefaultListCellRenderer
{
  public Component getListCellRendererComponent(JList list,Object value,
                      int index,boolean isSelected,boolean cellHasFocus)
  {
    JLabel lbl = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
    if(list.getSelectedIndex() == index) lbl.setPreferredSize(new Dimension(100,75));
    else lbl.setPreferredSize(new Dimension(100,20));
    return lbl;
  }
}