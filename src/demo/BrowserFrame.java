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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.frostwire.gui.browser.windows.BrComponentEvent;
import com.frostwire.gui.browser.windows.BrComponentListener;
import com.frostwire.gui.browser.windows.InternetExplorerBrowser;


/**
 * Sample browser implementation.
 * @author  uta
 */
public class BrowserFrame extends javax.swing.JFrame
implements BrComponentListener
{
    private static final long serialVersionUID = -7065448910990348234L;
    
    private JMenuItem menuRunJS;
    private InternetExplorerBrowser brMain;
    
    /** Creates new form BrowserFrame */
    public BrowserFrame() {
        com.frostwire.gui.browser.windows.BrComponent.DESIGN_MODE = false;
        initComponents();
        brMain.addBrComponentListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        bnRefresh = new javax.swing.JButton();
        brMain = new InternetExplorerBrowser();
        ieToolBar = new javax.swing.JToolBar();
        bnBack = new javax.swing.JButton();
        bnForward = new javax.swing.JButton();
        bnReload = new javax.swing.JButton();
        bnStop = new javax.swing.JButton();
        lbURL = new javax.swing.JLabel();
        edAddress = new javax.swing.JTextField();
        bnGo = new javax.swing.JButton();
        ieStatus = new javax.swing.JToolBar();
        edStatusText = new javax.swing.JTextField();
        bnLocker = new javax.swing.JButton();
        pbDownloadDoc = new javax.swing.JProgressBar();
        mainJMenuBar = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        miExit = new javax.swing.JMenuItem();
        toolsJMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/reload.png")).getImage());

        jPanel1.setLayout(new java.awt.BorderLayout());

        bnRefresh.setFocusable(false);
        bnRefresh.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        bnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        bnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnRefreshActionPerformed(evt);
            }
        });
        
        bnRefresh.getAccessibleContext().setAccessibleName("bnRefresh");

        brMain.setURL("http://www.google.com");
        brMain.setPreferredSize(new java.awt.Dimension(220, 220));

        brMain.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                onBrowserPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout brMainLayout = new javax.swing.GroupLayout(brMain);
        brMain.setLayout(brMainLayout);
        brMainLayout.setHorizontalGroup(
            brMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );
        brMainLayout.setVerticalGroup(
            brMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 503, Short.MAX_VALUE)
        );

        jPanel1.add(brMain, java.awt.BorderLayout.CENTER);

        ieToolBar.setRollover(true);

        bnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.png"))); // NOI18N
        bnBack.setToolTipText("Go back one page");
        bnBack.setFocusable(false);
        bnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnBack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        bnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnBackActionPerformed(evt);
            }
        });
        ieToolBar.add(bnBack);

        bnForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward.png"))); // NOI18N
        bnForward.setToolTipText("Go forward one page");
        bnForward.setFocusable(false);
        bnForward.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnForward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        bnForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnForwardActionPerformed(evt);
            }
        });
        ieToolBar.add(bnForward);

        bnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reload.png"))); // NOI18N
        bnReload.setToolTipText("Reload current page");
        bnReload.setFocusable(false);
        bnReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnReload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnReloadActionPerformed(evt);
            }
        });
        ieToolBar.add(bnReload);

        bnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop.png"))); // NOI18N
        bnStop.setToolTipText("Reload current page");
        bnStop.setFocusable(false);
        bnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnStopActionPerformed(evt);
            }
        });
        ieToolBar.add(bnStop);

        lbURL.setText(" URL:");
        ieToolBar.add(lbURL);

        edAddress.setText("http://");
        edAddress.setToolTipText("URL for navigation");
        edAddress.setPreferredSize(new java.awt.Dimension(400, 20));
        edAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onNavigate(evt);
            }
        });
        ieToolBar.add(edAddress);

        bnGo.setText("Go");
        bnGo.setToolTipText("Go to entered URL");
        bnGo.setFocusable(false);
        bnGo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnGo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onNavigate(evt);
            }
        });
        ieToolBar.add(bnGo);

        jPanel1.add(ieToolBar, java.awt.BorderLayout.PAGE_START);

        ieStatus.setFloatable(false);
        ieStatus.setRollover(true);
        ieStatus.setMaximumSize(new java.awt.Dimension(65536, 20));
        ieStatus.setMinimumSize(new java.awt.Dimension(10, 20));

        edStatusText.setEditable(false);
        edStatusText.setMaximumSize(new java.awt.Dimension(2147483647, 20));

        //binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, brMain, org.jdesktop.beansbinding.ELProperty.create("${statusText}"), edStatusText, org.jdesktop.beansbinding.BeanProperty.create("text"));
        //bindingGroup.addBinding(binding);

        edStatusText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edStatusTextActionPerformed(evt);
            }
        });
        ieStatus.add(edStatusText);

        bnLocker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/locker.png"))); // NOI18N
        bnLocker.setFocusable(false);
        bnLocker.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bnLocker.setMaximumSize(new java.awt.Dimension(20, 20));
        bnLocker.setMinimumSize(new java.awt.Dimension(20, 20));
        bnLocker.setPreferredSize(new java.awt.Dimension(20, 20));
        bnLocker.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bnLocker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnLockerActionPerformed(evt);
            }
        });
        ieStatus.add(bnLocker);

        pbDownloadDoc.setMaximumSize(new java.awt.Dimension(100, 16));
        pbDownloadDoc.setPreferredSize(new java.awt.Dimension(100, 16));
        ieStatus.add(pbDownloadDoc);

        jPanel1.add(ieStatus, java.awt.BorderLayout.PAGE_END);

        fileJMenu.setText("File");
        fileJMenu.setToolTipText("File Operations");

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
        );

        //bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    protected void menuRunJS_actionPerformed(ActionEvent e) {
        //brMain.runJS("alert('test alert');");
        brMain.runJS("window.jbrowser.callJava();");
    }

    private void edStatusTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edStatusTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edStatusTextActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        //brMain.dispose();
        dispose();
    }//GEN-LAST:event_miExitActionPerformed

    private void bnLockerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnLockerActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_bnLockerActionPerformed

    private void onBrowserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_onBrowserPropertyChange
        String stPN = evt.getPropertyName();
        if(stPN.equals("navigatedURL")) {
            String st[] = ((String)evt.getNewValue()).split(",");
            edAddress.setText( st[0] );
            //brXMLTree.empty();
        } else if(stPN.equals("progressBar")) {
            String st[] = ((String)evt.getNewValue()).split(",");
            int iMax = Integer.parseInt(st[0]),
                iPos = Integer.parseInt(st[1]);
            if(0==iMax){
                pbDownloadDoc.setVisible(false);
                if(brMain.isDocumentReady()){
                    //bnRefreshActionPerformed(null);
                }
            } else {
                pbDownloadDoc.setMaximum(iMax);
                pbDownloadDoc.setValue(iPos);
                pbDownloadDoc.setVisible(true);
            }
        }  else if(stPN.equals("securityIcon")) {
            bnLocker.setVisible(!((String)evt.getNewValue()).equals("0"));
        }
    }//GEN-LAST:event_onBrowserPropertyChange

    private void onNavigate(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onNavigate
        brMain.setURL(edAddress.getText());
}//GEN-LAST:event_onNavigate

    private void bnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnBackActionPerformed
        brMain.back();
    }//GEN-LAST:event_bnBackActionPerformed

    private void bnForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnForwardActionPerformed
        brMain.forward();
    }//GEN-LAST:event_bnForwardActionPerformed

    private void bnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnReloadActionPerformed
        brMain.refresh();
    }//GEN-LAST:event_bnReloadActionPerformed

    private void bnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnStopActionPerformed
        brMain.stop();
    }//GEN-LAST:event_bnStopActionPerformed

   static final String stGetElemByPath =
        "function __gn(stPath)\n" +
        "{\n" +
        "   var childOrder = stPath.split(\"/\");\n" +
        "   htmlNode = document;\n" +
        "   for(var i = 0; null!=htmlNode && i<childOrder.length; ++i) {\n" +
        "       var index = 0;\n" +
        "       var limit = parseInt(childOrder[i]);\n" +
        "       for(\n" +
        "           var htmlNode = htmlNode.firstChild;\n" +
        "           null!=htmlNode && index < limit;\n" +
        "           htmlNode = htmlNode.nextSibling )\n" +
        //ditry HTML
        "       if( -1==htmlNode.nodeName.indexOf(\'/\') &&\n" +
        "            -1==htmlNode.nodeName.indexOf('<') &&\n" +
        "            -1==htmlNode.nodeName.indexOf('!') &&\n" +
        "            -1==htmlNode.nodeName.indexOf('?') &&\n" +
        "            -1==htmlNode.nodeName.indexOf('>')) " +
        "       {\n" +
        "           ++index;\n" +
        "       }\n" +
        "   }\n" +
        "   return htmlNode;\n" +
        "}\n";

    private void bnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnRefreshActionPerformed
        //brXMLTree.setXMLSource(brMain.getXHTML(true));
    }//GEN-LAST:event_bnRefreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BrowserFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bnBack;
    private javax.swing.JButton bnForward;
    private javax.swing.JButton bnGo;
    private javax.swing.JButton bnLocker;
    private javax.swing.JButton bnRefresh;
    private javax.swing.JButton bnReload;
    private javax.swing.JButton bnStop;
    
    private javax.swing.JTextField edAddress;
    private javax.swing.JTextField edStatusText;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JToolBar ieStatus;
    private javax.swing.JToolBar ieToolBar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbURL;
    private javax.swing.JMenuBar mainJMenuBar;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JProgressBar pbDownloadDoc;
    private javax.swing.JMenu toolsJMenu;
   // private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public void onClosingWindowByScript(boolean isChildWindow){
        if(!isChildWindow && JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(
            this,
            "The webpage you are viewing is trying to close the window.\n" +
            "Do you want to close this window?",
            "Warning",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE))
        {
            //System.exit(0);
            dispose();
        }
    }
    
    public String sync(BrComponentEvent e) {
        switch(e.getID()){
        case BrComponentEvent.DISPID_WINDOWCLOSING:
            String stValue = e.getValue();
            if(null!=stValue){
                //OLE boolean: -1 - true, 0 - false; params:(bCancel, bIsChildWindow)                                       
                final boolean isChildWindow = (0!=Integer.valueOf(stValue.split(",")[1]));
                javax.swing.SwingUtilities.invokeLater ( new Runnable() {
                        public void run() {                                                
                            onClosingWindowByScript(isChildWindow);                            
                        }
                });   
            }    
            break;
        }
        return null;
    }
}
