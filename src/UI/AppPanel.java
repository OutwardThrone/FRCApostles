package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class AppPanel extends JPanel {

    private JTabbedPane tabs;

    public AppPanel() {
        super();
        init();
    }

    private void init() {

        setFocusable(true);
        setLayout(new GridLayout(1, 1));
        tabs = new JTabbedPane();
        JComponent tuningPanel = tuningPage();
        tabs.addTab("Tuning", tuningPanel);
        tabs.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent recordingPanel = recordingPage();
        tabs.addTab("Record", recordingPanel);
        tabs.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabs);

        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private JComponent tuningPage() {
        JPanel panel = new JPanel();
        panel.setSize(Main.WIDTH, Main.HEIGHT);

        GridBagLayout gbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gbag);

        JComboBox<String> searchBar = new JComboBox<String>(new String[]{"", "Vibhav Peri", "Varun Ananth", "John Yang", "Mustafa Miyaziwala"});

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.ipadx = 150;
        c.gridx = 0;
        c.gridy = 0;
        gbag.setConstraints(searchBar, c);
        panel.add(searchBar);

        JButton tuneButton = new JButton("Tune now");
        c.gridy = 1;
        gbag.setConstraints(tuneButton, c);
        panel.add(tuneButton);

        JPanel statusBar = new JPanel();
        statusBar.setBackground(Color.GREEN);
        c.gridheight = 2;
        c.ipady = 200;
        c.ipadx = 300;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        gbag.setConstraints(statusBar, c);
        panel.add(statusBar);

        return panel;
    }

    private JComponent recordingPage() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("recording page"));
        panel.setLayout(new GridLayout(1,1));
        return panel;
    }
}
