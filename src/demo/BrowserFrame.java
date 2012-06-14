/*
 * Copyright (C) 2008 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.frostwire.gui.browser.BrowserFactory;
import com.frostwire.gui.browser.WebBrowser;

/**
 * Sample browser implementation.
 */
public class BrowserFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = -7065448910990348234L;

    private JButton bnBack;
    private JButton bnForward;
    private JButton bnGo;
    private JButton bnRefresh;
    private JButton bnReload;
    private JButton bnStop;

    private JTextField edAddress;
    private JTextField edStatusText;
    private JMenu fileJMenu;
    private JToolBar ieStatus;
    private JToolBar ieToolBar;
    private JPanel jPanel1;
    private JLabel lbURL;
    private JMenuBar mainJMenuBar;
    private JMenuItem miExit;
    private JProgressBar pbDownloadDoc;
    private JMenu toolsJMenu;
    private JMenuItem menuRunJS;

    private WebBrowser browser;

    /** Creates new form BrowserFrame */
    public BrowserFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        jPanel1 = new JPanel();
        bnRefresh = new JButton();
        browser = BrowserFactory.instance().createBrowser();
        ieToolBar = new JToolBar();
        bnBack = new JButton();
        bnForward = new JButton();
        bnReload = new JButton();
        bnStop = new JButton();
        lbURL = new JLabel();
        edAddress = new JTextField();
        bnGo = new JButton();
        ieStatus = new JToolBar();
        edStatusText = new JTextField();
        pbDownloadDoc = new JProgressBar();
        mainJMenuBar = new JMenuBar();
        fileJMenu = new JMenu();
        miExit = new JMenuItem();
        toolsJMenu = new JMenu();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/reload.png")).getImage());

        jPanel1.setLayout(new BorderLayout());

        bnRefresh.setFocusable(false);
        bnRefresh.setHorizontalAlignment(SwingConstants.LEADING);
        bnRefresh.setHorizontalTextPosition(SwingConstants.CENTER);
        bnRefresh.setVerticalTextPosition(SwingConstants.BOTTOM);

        bnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnRefreshActionPerformed(evt);
            }
        });

        bnRefresh.getAccessibleContext().setAccessibleName("bnRefresh");

        //browser.setUrl("http://www.google.com");

        jPanel1.add(browser.getComponent(), BorderLayout.CENTER);

        ieToolBar.setRollover(true);

        bnBack.setIcon(new ImageIcon(getClass().getResource("/images/back.png"))); // NOI18N
        bnBack.setToolTipText("Go back one page");
        bnBack.setFocusable(false);
        bnBack.setHorizontalTextPosition(SwingConstants.CENTER);
        bnBack.setVerticalTextPosition(SwingConstants.BOTTOM);

        bnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnBackActionPerformed(evt);
            }
        });
        ieToolBar.add(bnBack);

        bnForward.setIcon(new ImageIcon(getClass().getResource("/images/forward.png"))); // NOI18N
        bnForward.setToolTipText("Go forward one page");
        bnForward.setFocusable(false);
        bnForward.setHorizontalTextPosition(SwingConstants.CENTER);
        bnForward.setVerticalTextPosition(SwingConstants.BOTTOM);

        bnForward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnForwardActionPerformed(evt);
            }
        });
        ieToolBar.add(bnForward);

        bnReload.setIcon(new ImageIcon(getClass().getResource("/images/reload.png"))); // NOI18N
        bnReload.setToolTipText("Reload current page");
        bnReload.setFocusable(false);
        bnReload.setHorizontalTextPosition(SwingConstants.CENTER);
        bnReload.setVerticalTextPosition(SwingConstants.BOTTOM);
        bnReload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnReloadActionPerformed(evt);
            }
        });
        ieToolBar.add(bnReload);

        bnStop.setIcon(new ImageIcon(getClass().getResource("/images/stop.png"))); // NOI18N
        bnStop.setToolTipText("Reload current page");
        bnStop.setFocusable(false);
        bnStop.setHorizontalTextPosition(SwingConstants.CENTER);
        bnStop.setVerticalTextPosition(SwingConstants.BOTTOM);
        bnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnStopActionPerformed(evt);
            }
        });
        ieToolBar.add(bnStop);

        lbURL.setText(" URL:");
        ieToolBar.add(lbURL);

        edAddress.setText("http://");
        edAddress.setToolTipText("URL for navigation");
        edAddress.setPreferredSize(new Dimension(400, 20));
        edAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onNavigate(evt);
            }
        });
        ieToolBar.add(edAddress);

        bnGo.setText("Go");
        bnGo.setToolTipText("Go to entered URL");
        bnGo.setFocusable(false);
        bnGo.setHorizontalTextPosition(SwingConstants.CENTER);
        bnGo.setVerticalTextPosition(SwingConstants.BOTTOM);
        bnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onNavigate(evt);
            }
        });
        ieToolBar.add(bnGo);

        jPanel1.add(ieToolBar, BorderLayout.PAGE_START);

        ieStatus.setFloatable(false);
        ieStatus.setRollover(true);
        ieStatus.setMaximumSize(new Dimension(65536, 20));
        ieStatus.setMinimumSize(new Dimension(10, 20));

        edStatusText.setEditable(false);
        edStatusText.setMaximumSize(new Dimension(2147483647, 20));

        edStatusText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                edStatusTextActionPerformed(evt);
            }
        });
        ieStatus.add(edStatusText);

        pbDownloadDoc.setMaximumSize(new Dimension(100, 16));
        pbDownloadDoc.setPreferredSize(new Dimension(100, 16));
        ieStatus.add(pbDownloadDoc);

        jPanel1.add(ieStatus, BorderLayout.PAGE_END);

        fileJMenu.setText("File");
        fileJMenu.setToolTipText("File Operations");

        miExit.setText("Exit");
        miExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        fileJMenu.add(miExit);

        mainJMenuBar.add(fileJMenu);

        menuRunJS = new JMenuItem();
        menuRunJS.setText("Run JS");
        menuRunJS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuRunJS_actionPerformed(e);
            }
        });

        toolsJMenu.setText("Tools");
        toolsJMenu.add(menuRunJS);

        mainJMenuBar.add(toolsJMenu);

        setJMenuBar(mainJMenuBar);

        GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE));

        pack();
    }

    protected void menuRunJS_actionPerformed(ActionEvent e) {
        //brMain.runJS("alert('test alert');");
        browser.runJS("alert(window.jbrowser.callJava('functionName', 'functionData'));");
    }

    private void edStatusTextActionPerformed(ActionEvent evt) {
    }

    private void miExitActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    private void onNavigate(ActionEvent evt) {
        browser.setUrl(edAddress.getText());
    }

    private void bnBackActionPerformed(ActionEvent evt) {
        browser.back();
    }

    private void bnForwardActionPerformed(ActionEvent evt) {
        browser.forward();
    }

    private void bnReloadActionPerformed(ActionEvent evt) {
        browser.refresh();
    }

    private void bnStopActionPerformed(ActionEvent evt) {
        browser.stop();
    }

    private void bnRefreshActionPerformed(ActionEvent evt) {
    }

    public static void main(String args[]) {
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        new BrowserFrame().setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new BrowserFrame().setVisible(true);
            }
        });
    }
}
