import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.frostwire.gui.webbrowser.BrowserFactory;
import com.frostwire.gui.webbrowser.BrowserFunction;
import com.frostwire.gui.webbrowser.WebBrowser;

/**
 * Sample browser implementation.
 */
public class BrowserFrame extends JFrame {

    private static final long serialVersionUID = -7065448910990348234L;

    private JButton bnBack;
    private JButton bnForward;
    private JButton bnGo;
    private JButton bnReload;
    private JButton bnStop;

    private JTextField edAddress;
    private JMenu fileJMenu;
    private JToolBar ieToolBar;
    private JPanel jPanel1;
    private JLabel lbURL;
    private JMenuBar mainJMenuBar;
    private JMenuItem miExit;
    private JMenu toolsJMenu;
    private JMenuItem menuRunJS;
    private JMenuItem menuRunJS2;

    private WebBrowser browser;

    public BrowserFrame() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        browser = BrowserFactory.instance().createBrowser();
        ieToolBar = new JToolBar();
        bnBack = new JButton();
        bnForward = new JButton();
        bnReload = new JButton();
        bnStop = new JButton();
        lbURL = new JLabel();
        edAddress = new JTextField();
        bnGo = new JButton();
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

        browser.go("http://www.google.com");

        jPanel1.add(browser.getComponent(), BorderLayout.CENTER);

        ieToolBar.setRollover(true);

        bnBack.setIcon(new ImageIcon(getClass().getResource("/images/back.png")));
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

        bnForward.setIcon(new ImageIcon(getClass().getResource("/images/forward.png")));
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

        bnReload.setIcon(new ImageIcon(getClass().getResource("/images/reload.png")));
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

        bnStop.setIcon(new ImageIcon(getClass().getResource("/images/stop.png")));
        bnStop.setToolTipText("Stop current page");
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

        menuRunJS2 = new JMenuItem();
        menuRunJS2.setText("Run JS 2");
        menuRunJS2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuRunJS2_actionPerformed(e);
            }
        });

        toolsJMenu.setText("Tools");
        toolsJMenu.add(menuRunJS);
        toolsJMenu.add(menuRunJS2);

        mainJMenuBar.add(toolsJMenu);

        setJMenuBar(mainJMenuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE));

        pack();
    }

    protected void menuRunJS_actionPerformed(ActionEvent e) {
        browser.runJS("alert('test alert');");
    }

    protected void menuRunJS2_actionPerformed(ActionEvent e) {
        browser.function(new BrowserFunction("testFn") {
            @Override
            public String run(String data) {
                return "Callback: " + data;
            }
        });
        browser.runJS("alert(testFn('hello'))");
    }

    private void miExitActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    private void onNavigate(ActionEvent evt) {
        browser.go(edAddress.getText());
    }

    private void bnBackActionPerformed(ActionEvent evt) {
        browser.back();
    }

    private void bnForwardActionPerformed(ActionEvent evt) {
        browser.forward();
    }

    private void bnReloadActionPerformed(ActionEvent evt) {
        browser.reload();
    }

    private void bnStopActionPerformed(ActionEvent evt) {
        browser.stop();
    }

    public static void main(String args[]) {
        System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("sun.awt.noerasebackground", "true");

        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        new BrowserFrame().setVisible(true);
    }
}
