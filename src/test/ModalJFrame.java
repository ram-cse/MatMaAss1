package test;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ModalJFrame extends JFrame {

    Object currentWindow = this;

    public ModalJFrame() 
    {
        super();
        super.setTitle("Main JFrame");
        super.setSize(500, 500);
        super.setResizable(true);
        super.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        JMenuItem newAction = new JMenuItem("New");
        JMenuItem openAction = new JMenuItem("Open");
        JMenuItem exitAction = new JMenuItem("Exit");
        JMenuItem cutAction = new JMenuItem("Cut");
        JMenuItem copyAction = new JMenuItem("Copy");
        JMenuItem pasteAction= new JMenuItem("Paste");

        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);

        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.addSeparator();
        editMenu.add(pasteAction);

        newAction.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {

                JFrame popupJFrame = new JFrame();

                popupJFrame.addWindowListener(new WindowAdapter()
                {
                      public void windowClosing(WindowEvent e) 
                      {
                          ((Component) currentWindow).setEnabled(true);                     }
                      });

                ((Component) currentWindow).setEnabled(false);
                popupJFrame.setTitle("Pop up JFrame");
                popupJFrame.setSize(400, 500);
/*                popupJFrame.setAlwaysOnTop(true);
*/                popupJFrame.setResizable(false);
                popupJFrame.setLocationRelativeTo(getRootPane());
                popupJFrame.setVisible(true);
                popupJFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        });

        exitAction.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {

        ModalJFrame myWindow = new ModalJFrame();
        myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myWindow.setVisible(true);
    }
}