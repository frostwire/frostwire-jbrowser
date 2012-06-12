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

package com.frostwire.gui.browser.windows;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;


import javax.swing.*;

/**
 * <p>A <code>BrComponent</code> component represents a wrapper for embedded browser 
 * in which the application can display webpages or from which the application 
 * can trap browser events. In order to show  <code>BrComponent</code> component 
 * in GUI, users need to add <code>BrComponent</code> to a top-level container, 
 * such as Frame.
 * </p><p>
 * The class that is interested in processing a <code>BrComponent</code> event 
 * should implement interface <code>BrComponentListener</code>, and the object 
 * created with that class should use <code>BrComponent</code>'s
 * <code>addBrComponentListener</code> method to register as a listener.
 * </p><p>
 * As an <code>JComponent</code> component, a <code>BrComponent</code> component 
 * must be hosted by a native container somewhere higher up in the component tree 
 * (for example, by a <code>JPanel</code> object) and it can have the child 
 * <code>JComponent</code> with correct Z-order mixing procedure.
 * </p>
 * Attention! You use to set {@link #DESIGN_MODE} global variable 
 * the <code>false</code> value while top-level container initialization procedure. 
 * 
 * @author uta
 */            

public class BrComponent
    extends JComponent
    implements FocusListener, BrComponentConsts
{
    private static final long serialVersionUID = 632746088999002599L;

    /**
     * <p>
     * While the value is <code>true</code> the 
     * <code>BrComponent</code> class have a stub painting procedure that makes 
     * it lightweight completely. It solves smart NetBeans design mode problem.
     * </p><p>
     * Warning! Don't forget to set it <code>false</code> at run-time.
     * </p><p>Example:<code><pre style="background-color:#B8BFD8;width:80ex">
     * public class BrowserFrame extends javax.swing.JFrame {
     *     // Creates new form BrowserFrame
     *     public BrowserFrame() {
     *         org.jdic.web.BrComponent.DESIGN_MODE = false;
     *         initComponents();
     *     }
     *     ....
     * }</pre></code></p>
     */
    public static boolean DESIGN_MODE = false;
    
    /**
     * String for java-stream URL navigation.
     * Not implemented yet.
     */
    public static final String stJavaStream = "js:";
    
    /**
     * Initial navigation URL
     */
    public static final String stAboutBlank = "about:blank";    

    
    /**
     * Buffered browser context paint as background for {@link #paintContent} 
     * paint callback.
     */
    public static final int PAINT_JAVA = 0x0001;
    
    /**
     * Direct browser context paint just before {@link #paintContent} 
     * paint callback.
     */
    public static final int PAINT_JAVA_NATIVE = 0x0002;
    
    /**
     * Only direct browser implementation of painting algorithm.
     */
    public static final int PAINT_NATIVE = 0x0004;

     /**
     * The global hint constants for native browser and {@link #paintContent} 
     * concurent rendering. Write-only.
     * One of <code>PAINT_XXXX</code> constans. 
     * <code>PAINT_JAVA</code> by default.
     */
    protected static int defaultPaintAlgorithm = PAINT_JAVA;
    
    /**
     * Setter for {@link #defaultPaintAlgorithm} property.
     * @param _defaultPaintAlgorithm PAINT_XXXX const
     */
    public static void setDefaultPaintAlgorithm(int _defaultPaintAlgorithm)
    {
        defaultPaintAlgorithm = _defaultPaintAlgorithm;
    }
    
    
    /**
     * The instance hint constants for native browser and {@link #paintContent} 
     * concurent rendering. It's equeal to {@link #defaultPaintAlgorithm} 
     * by default.
     * One of <code>PAINT_XXXX</code> constans. 
     * <code>PAINT_JAVA</code> by default.
     */
    public int paintAlgorithm = defaultPaintAlgorithm;

    
    /**
     * The sprites list to be rendered while default {@link #paintContent} 
     * procedure.
     */
    private java.util.List<BrISprite>    sprites;  
    
    /**
     * The listener for notifications callbacks.
     */    
    transient BrComponentListener ieListener; //TODO: make it stackable.   
    
    /**
     * Peer wrapper object for native browser.
     */
    private BrComponentPeer brPeer;
    
    /**
     * The string that holds a current document URL.
     */
    public String stURL;

    /**
     * Streamed provider for HTML data loader.
     * Is used forom native async reader.
     */
    public InputStream isHTMLSrc;
    
    public boolean isPeerReady()
    {
        return null!=brPeer && brPeer.isNativeReady();
    }
    /**
     * Sets the current document URL. If the peer browser object is ready an 
     * asyncronous navigation starts.
     * @param _stURL the string with the current document URL, if the value is 
     * <code>null</code> the <code>stURL</code> propery contains an empty string 
     * <code>""</code>
     */
    public void setURL(String _stURL) {
        isHTMLSrc = null;
        stURL = (null==_stURL || 0==_stURL.length()) ? stAboutBlank : _stURL;
        if(isPeerReady()) {
            setDocumentReady(false);
            brPeer.acceptTargetURL();
        }
    }
    
    /**
     * Loads the document from the input stream.
     * @param _isHTMLSrc the input stream to load document from.
     * @param _stURL the string incarnation of the document URL. 
     * <cote>null</cote> value means <code>about:blank</code> URL
     */
    public void setHTML(InputStream _isHTMLSrc, String _stURL) {
        if(null==_isHTMLSrc){
            setURL(stAboutBlank);
            return;
        }
        isHTMLSrc = _isHTMLSrc;
        stURL = _stURL;
        if(isPeerReady()) {
            setDocumentReady(false);
            brPeer.acceptTargetURL();
        }
    }
    
    
    /**
     * Loads the document from the string. The document URL doesn't change while this 
     * call. It simplifies the document post-processing procedure.
     * @param stHTML the string with document content inside.
     */
    public void setHTML(String stHTML)
    {
        try {
            setHTML(new ByteArrayInputStream(stHTML.getBytes("UTF-8")), stURL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }                
    }

    /**
     * Loads the document from the input stream opened by java connection to URL.
     * @param url the URL paramenter for <code>FileInputStream</code> 
     * constructor. <cote>null</cote> value means <code>about:blank</code>. 
     * @see #setHTML(InputStream _isHTMLSrc, String _stURL)
     * @throws java.io.FileNotFoundException
     */
    public void setURL(URL url) throws FileNotFoundException {
        if(null==url){
            setURL(stAboutBlank);
            return;
        }
        setHTML(new FileInputStream(url.toExternalForm()), url.toString());
    }
    
    /**
     * Getter for the current document URL.
     * @return the string of the current document URL 
     */
    public String getURL() {
        if( isPeerReady() ){
            stURL = brPeer.getURL();
        }
        return stURL;
    }
    
    /**
     * Getter for the current document HTML content with native browser 
     * formatting. 
     * @return the string of the current document HTML content. 
     * <code>null</code> if peer object is not ready.
     */
    public String getHTML() {
        if( isPeerReady() ){
            return brPeer.getNativeHTML();
        }
        return null;
    }
    
    /**
     * Getter for the current document HTML content with XHTML formatting.
     * @return the string of the current document HTML content 
     * <code>null</code> if peer object is not ready.* 
     */
    public String getXHTML() {
        return getXHTML(false);
    }
    
    /**
     * Getter for the current document HTML content with XHTML formatting.
     * @param bWithUniqueID if <code>false</code> HTML content returns as it is, 
     * <code>true</code> extends tags by 
     * <a href="http://msdn2.microsoft.com/en-us/library/ms534704(VS.85).aspx">
     * unique id</a>.
     * @return the string of the current document HTML content 
     * <code>null</code> if peer object is not ready. 
     */
    public  String getXHTML(boolean bWithUniqueID) {
        if( isPeerReady() ){
            return brPeer.getNativeXHTML(bWithUniqueID);
        }
        return null;
    }

    /**
     * Getter for the sprites list.
     * @return the list of rendered sprite objects. Those objects are the java 
     * sprites related with the document, but are not a part of the document. 
     * For example the map zoom selection area.
     */
    public java.util.List<BrISprite> getSprites()
    {
        if(null==sprites){
            sprites = new java.util.LinkedList<BrISprite>();
        }
        return sprites;
    }

    /**
     * Makes the component visible or invisible.  
     * @param aFlag  <code>true</code> to make the component visible; 
     * <code>false</code> to make it invisible
     */
    @Override
    public void setVisible(boolean aFlag){
        super.setVisible(aFlag);
        if( isPeerReady() ) {
            brPeer.setVisible(isVisible());
        }
    }

    /**
     * Sets whether or not this component is enabled. A component that is 
     * enabled may respond to user input, while a component that is not enabled 
     * cannot respond to user input. Some components may alter their visual 
     * representation when they are disabled in order to provide feedback to 
     * the user that they cannot take input. <br/>
     * Note: Disabling a component does not disable its children.<br/>
     * @param enabled <code>true</code> if this component should be enabled, 
     * <code>false</code> otherwise
     */
    @Override
    public void setEnabled(boolean enabled){
        super.setVisible(enabled);
        if( isPeerReady() ) {
            brPeer.setEnabled(isEnabled());
        }
    }

    /**
     * Resets the base JComponent to default state
     */
    private void init(){        
        enableEvents(AWTEvent.KEY_EVENT_MASK |
            AWTEvent.INPUT_METHOD_EVENT_MASK |
            AWTEvent.MOUSE_EVENT_MASK |
            AWTEvent.MOUSE_MOTION_EVENT_MASK |
            AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusable(true);
        addFocusListener(this);
        setRequestFocusEnabled(true);
        brPeer = new WBrComponentPeer(this);
    }

    /**
     * Creates the semilightweight browser component with URL-defined 
     * document.
     * @param stURL the string with URL to be load. <code>null</code> means 
     * <code>about:blank</code> URL.
     */
    public BrComponent(String stURL) {
        super();
        init();
        setURL(stURL);
    }
    
    /**
     * Creates the semilightweight browser component with empty 
     * document.
     */
    public BrComponent() {
       this(stAboutBlank);
   }
    
    /**
     * Creates the semilightweight browser component with streamed document.
     * @param _isHTMLSrc the input stream to load document from.
     * @param stURL the string incarnation of the document URL. 
     */
     public BrComponent(InputStream _isHTMLSrc, String stURL) {
       super();
       init();
       setHTML(_isHTMLSrc, stURL);
    }

    //public actions
    public void back()
    {
         execJSLater(":window.history.back()");
    }
    public void forward()
    {
        execJSLater(":window.history.forward()");
    }
    public void refresh()
    {
        execJSLater(":window.history.go(0)");
    }
    
    public void stop()
    {
        execJSLater("##stop");//can be done correctly only by IBrComponent
    }

    public void save()
    {
        execJSLater(":document.execCommand(\"SaveAs\", true)");
    }

    public void save(String stHTMLPath)
    {
        execJSLater(":document.execCommand(\"SaveAs\", false, \"" + stHTMLPath + "\")");
    }

    public void open()
    {
        Frame fr = (Frame)getTopLevelAncestor();
        FileDialog fd = new FileDialog(fr, "Open document", FileDialog.LOAD);
        //Rectangle w = fr.getBounds();
        //Rectangle cprc = fd.getBounds();
        //cprc.x = w.x + 22;
        fd.setVisible(true);
        //fd.setLocation(new Point(fr.getX(), fr.getY()));
        if(null!=fd.getFile()){
            open( fd.getDirectory() + fd.getFile() );            
        }
    }

    public void open(String stHTMLPath)
    {
        setURL(stHTMLPath);
    }


    public void print()
    {
        execJSLater(":document.execCommand(\"print\", true)");
    }

    @Override
    public void validate()
    {
        super.validate(); 
        if( isPeerReady() ) {
            brPeer.validate();
        }
    }
    
//    @Override
//    public void reshape(int x, int y, int width, int height)
//    {
//        super.reshape(x, y, width, height);
//        if( isPeerReady() ) {
//            brPeer.reshape(x, y, width, height);
//            brPeer.validate();                    
//        }
//    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if( isPeerReady() ) {
            brPeer.reshape(x, y, width, height);
            brPeer.validate();                    
        }
    }

    public JComponent getCentralPanel()
    {
        return brPeer.getCentralPanel();
    }

    /**
     * Makes this Component displayable by connecting it to a
     * native screen resource.
     * This method is called internally by the toolkit and should
     * not be called directly by programs.
     * @see       #removeNotify
     */
    @Override
    public void addNotify() {
        super.addNotify();
        brPeer.onAddNotify();        
    }
    
    /**
     * Removes the <code>BrComponent</code>'s peer.
     * The peer allows us to modify the appearance of the
     * <code>BrComponent</code> without changing its
     * functionality.
     */
    @Override
    public void removeNotify() {
        brPeer.onRemoveNotify();
        super.removeNotify();
    }

    /**
     * Postponed Java Script execution.
     * @param code the string with Java Script code.
     */
    public void execJSLater(final String code) {
        javax.swing.SwingUtilities.invokeLater ( new Runnable() {
            public void run() {
                execJS(code);
            }
        });
    }
    
    /**
     * Synchronous Java Script execution.
     * @param code the string with Java Script code.
     * @return the string with execution result.
     */
    public String execJS(String code) {
        if( isPeerReady() && null!=code || 0!=code.length() ) {
            return brPeer.execJS(code);
        }
        return null;
    }

    /**
     * Switches browser acccess to user input.
     * @param dropNativeAction <code>true</code> switches off browser from user 
     * input; <code>false</code> switches on native input events treatment. 
     */
    public void blockNativeInputHandler(boolean dropNativeAction)
    {
        if( isPeerReady() ) {
           brPeer.blockNativeInputHandler(dropNativeAction);
        }
    }
    
    public void paintPlaceHolder(
            Graphics g,
            int x, int y, int width, int height,
            String stLabel)
    {
        g.setColor(Color.green);
        GradientPaint gradient = new GradientPaint(
            x, y,
            new Color(0.0F, 1.0F, 1.0F, 0.5F),
            x + width, y + height,
            new Color(1.0F, 1.0F, 0.0F, 0.5F),
            false); // true means to repeat pattern

        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(gradient);
        g2.fill(new Rectangle(x,y,width,height));
        g2.setPaint(null);
        g2.setColor(Color.BLACK);
        g2.drawRect(
            x, y,
            width-1, height-1
        );

        if( null!=stLabel && 0!=stLabel.length() ) {
            FontMetrics fm   = g2.getFontMetrics(g2.getFont());    
            Rectangle2D rect = fm.getStringBounds(stLabel, g);
            int textHeight = (int)(rect.getHeight());
            int textWidth  = (int)(rect.getWidth());
            if(
                textHeight <= height &&
                textWidth  <= width)
            {
                g2.drawString(
                    stLabel,
                    x + (width - textWidth)/2,
                    y + (height + textHeight)/2
                );
                g2.setColor(Color.white);
                g2.drawLine(
                    x + 1, y + 1,
                    x + width - 2, y + height - 2
                );
            }
        }        
    }

    public void paintDesignMode(Graphics g) {
        paintPlaceHolder(
                g,
                0, 0,
                getWidth(), getHeight(),
                getClass().getName() + " " + getName() );        
    }

    public void paintContent(Graphics g) {
        if(null!=sprites){
            for (Object sprite : sprites) {
                ((BrISprite) sprite).drawOn(this, g);
            }
        }
    }

    static int iPaintCount = 0;
    public void paintClientArea(Graphics g,  boolean withPeer) {
        if( isPeerReady() && withPeer ){
            brPeer.paintClientArea(g, paintAlgorithm);
        } else if(DESIGN_MODE){
            paintDesignMode(g);
        }
        paintContent(g);
        if(isDebugDrawBorder()){
            Rectangle updateRect = g.getClipBounds();
            paintPlaceHolder(
                g, 
                updateRect.x, updateRect.y,
                updateRect.width, updateRect.height, 
                (++iPaintCount) + "");
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        //execJS("##showCaret(false)");
        super.paintComponent(g);
        if( !isPeerReady() || !brPeer.isSelfPaint()  ){
            paintClientArea(g, true);
        }    
    }


    /**
     * Adds the specified browser event listener to receive stURL events
     * from this stURL component.
     * If <code>l</code> is <code>null</code>, no exception is
     * thrown and no action is performed.
     *
     * @param l the browser event listener
     * #see           	#removeTextListener
     * #see           	#getTextListeners
     * @see           	java.awt.event.TextListener
     */
    public void addBrComponentListener(BrComponentListener l) {
        ieListener = l;
    }

    public void removeBrComponentListener(BrComponentListener l) {
        if( ieListener == l ) {
            ieListener = null;
        }
    }

            
     /**
      * Internal browser event processor. Converts some events to 
      * <code>BrComponentListener</code> inteface callbacks and property-changed 
      * notifications.
      * @param e the happened browser event
      */
     public String processBrComponentEvent(final BrComponentEvent e) {
        //System.out.println( "IEE:" + e.getID() + " Name:" + e.getName() + " Value:"+ e.getValue() ); 
        String res = null;
        BrComponentListener listener = ieListener;
        if (listener != null) {
            res = listener.sync(e);
        }
        SwingUtilities.invokeLater ( new Runnable() { public void run() {
                //System.out.println(e);
                String stValue = e.getValue();
                stValue = (null==stValue)?"":stValue; 
                switch(e.getID()){
                case BrComponentEvent.DISPID_STATUSTEXTCHANGE:
                    setStatusText(stValue);
                    break;
                case BrComponentEvent.DISPID_BEFORENAVIGATE2:
                    break;
                case BrComponentEvent.DISPID_NAVIGATECOMPLETE2:
                    setNavigatedURL(stValue);
                    break;
                case BrComponentEvent.DISPID_DOWNLOADCOMPLETE:
                    setDocumentReady(true);
                    break;
                case BrComponentEvent.DISPID_PROGRESSCHANGE:
                    setProgressBar(stValue);
                    break;
                case BrComponentEvent.DISPID_SETSECURELOCKICON:
                    setSecurityIcon(stValue);
                    break;
                case BrComponentEvent.DISPID_TITLECHANGE:
                    setWindowTitle(stValue);
                    break;
                case BrComponentEvent.DISPID_COMMANDSTATECHANGE:
                    {
                        String st[] = stValue.split(",");
                        boolean enable = (0!=Integer.valueOf(st[0]));
                        switch(Integer.valueOf(st[1])){
                        case -1:    
                            setToolbarChanged(enable);                            
                            break;
                        case 1:    
                            setGoForwardEnable(enable);                            
                            break;
                        case 2:    
                            setGoBackEnable(enable);                                                        
                            break;                            
                        }
                    }    
                    break;
                }
        }});//end posponed operation   
        return res;
    }

    /**
     * Returns a string representing the state of the browser component.
     * The method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     * @return the parameter string of this stURL component
     */
    @Override
    protected String paramString() {
        return super.paramString() + ",URL=" + stURL;
    }

    //FocusListener
    public void focusGained(FocusEvent e) {
        if( isPeerReady() ){
            brPeer.focusGain(true);
        }
    }
    public void focusLost(FocusEvent e) {
    }

    //Input methods capturing
    public void checkMouse(MouseEvent e) {
        if( isPeerReady() ){
            brPeer.sendMouseEvent(e);
        }
    }
    
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if( !e.isConsumed() ){
            checkMouse(e);
        }
    }
    
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        if( !e.isConsumed() ){
            checkMouse(e);
        }
    }
    protected void processMouseWheelEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        if( !e.isConsumed() ){
            checkMouse(e);
        }
    }


    /**
     * Utility field used by bound properties.
     */
    protected java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param ls The listener to add.
     */
    @Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener ls) {
        propertyChangeSupport.addPropertyChangeListener(ls);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param ls The listener to remove.
     */
    @Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener ls) {
        propertyChangeSupport.removePropertyChangeListener(ls);
    }

    /**
     * Holds value of property navigatedURL to show in address bar.
     */
    private String navigatedURL;
    
    /**
     * Getter for property navigatedURL.
     * 
     * @return Value of property navigatedURL.
     */
    public String getNavigatedURL() {
        return this.navigatedURL;
    }

    /**
     * Setter for property navigatedURL.
     * 
     * @param addressURL New value of property navigatedURL.
     */
    protected void setNavigatedURL(String addressURL) {
        String oldAddressURL = this.navigatedURL;
        this.navigatedURL = addressURL;
        propertyChangeSupport.firePropertyChange ("navigatedURL", oldAddressURL, addressURL);
    }

    /**
     * Holds value of property statusText to show in status bar.
     */
    private String statusText;

    /**
     * Getter for property statusText.
     * @return Value of property statusText.
     */
    public String getStatusText() {
        return this.statusText;
    }

    /**
     * Setter for property statusText.
     * @param statusText New value of property statusText.
     */
    protected void setStatusText(String statusText) {
        String oldStatusText = this.statusText;
        this.statusText = statusText;
        propertyChangeSupport.firePropertyChange ("statusText", oldStatusText, statusText);
    }

    /**
     * Holds value of property windowTitle to show in window title.
     */
    private String windowTitle;

    /**
     * Getter for property windowTitle.
     * @return Value of property windowTitle.
     */
    public String getWindowTitle() {
        return this.windowTitle;
    }

    /**
     * Setter for property windowTitle.
     * @param windowTitle New value of property windowTitle.
     */
    protected void setWindowTitle(String windowTitle) {
        String oldWindowTitle = this.windowTitle;
        this.windowTitle = windowTitle;
        propertyChangeSupport.firePropertyChange ("windowTitle", oldWindowTitle, windowTitle);
    }

    /**
     * Holds value of property progressBar.
     */
    private String progressBar;

    /**
     * Getter for property progressBar.
     * @return Value of property progressBar.
     */
    public String getProgressBar() {
        return this.progressBar;
    }

    /**
     * Setter for property progressBar.
     * @param progressBar New value of property progressBar.
     */
    protected void setProgressBar(String progressBar) {
        String oldProgressBar = this.progressBar;
        this.progressBar = progressBar;
        propertyChangeSupport.firePropertyChange ("progressBar", oldProgressBar, progressBar);
    }

    /**
     * Holds value of property debugDrawBorder.
     */
    private boolean debugDrawBorder;

    /**
     * Getter for property debugDrawBorder.
     * @return Value of property debugDrawBorder.
     */
    public boolean isDebugDrawBorder() {
        return this.debugDrawBorder;
    }

    /**
     * Setter for property debugDrawBorder.
     * @param debugDrawBorder New value of property debugDrawBorder.
     */
    public void setDebugDrawBorder(boolean debugDrawBorder) {
        boolean old = this.debugDrawBorder;
        this.debugDrawBorder = debugDrawBorder;
        propertyChangeSupport.firePropertyChange("debugDrawBorder", old, debugDrawBorder);
    }
    
    private boolean editable;    
    /**
     * Indicates whether or not this stURL component is editable.
     * @return     <code>true</code> if this stURL component is
     *                  editable; <code>false</code> otherwise.
     * @see        BrComponent#setEditable
     */
    public boolean isEditable() {
	return editable;
    }

    /**
     * Sets the flag that determines whether or not this
     * stURL component is editable.
     * <p>
     * If the flag is set to <code>true</code>, this stURL component
     * becomes user editable. If the flag is set to <code>false</code>,
     * the user cannot change the stURL of this stURL component.
     * By default, non-editable stURL components have a background color
     * of SystemColor.control.  This default can be overridden by
     * calling setBackground.
     *
     * @param     b   a flag indicating whether this stURL component
     *                is user editable.
     * @see       BrComponent#isEditable
     */
    public void setEditable(boolean b) {
        if (editable == b) {
            return;
        }
        editable = b;
        if( isPeerReady() ) {
            brPeer.setEditable(b);
        }
    }

    /**
     * Holds value of property securityIcon.
     */
    private String securityIcon;

    /**
     * Getter for property securityIcon.
     * @return Value of property securityIcon.
     */
    public String getSecurityIcon() {
        return this.securityIcon;
    }

    /**
     * Setter for property securityIcon.
     * One of secureLockIconXXXX const
     * @param securityIcon New value of property securityIcon.
     */
    protected void setSecurityIcon(String securityIcon) {
        String oldSecurityIcon = this.securityIcon;
        this.securityIcon = securityIcon;
        propertyChangeSupport.firePropertyChange ("securityIcon", oldSecurityIcon, securityIcon);
    }

    /**
     * Holds value of property mandatoryDispose.
     */
    private boolean mandatoryDispose = true;

    /**
     * Getter for property mandatoryDispose.
     * @return Value of property mandatoryDispose.
     */
    public boolean isMandatoryDispose() {
        return mandatoryDispose;
    }

    /**
     * Setter for property mandatoryDispose.
     * @param mandatoryDispose New value of property mandatoryDispose.
     */
    public void setMandatoryDispose(boolean mandatoryDispose) {
        boolean old = this.mandatoryDispose;
        this.mandatoryDispose = mandatoryDispose;
        propertyChangeSupport.firePropertyChange("mandatoryDispose", old, mandatoryDispose);
    }

    /**
     * Holds value of property goBackEnable.
     */
    private boolean goBackEnable;

    /**
     * Getter for property goBackEnable.
     * @return Value of property goBackEnable.
     */
    public boolean isGoBackEnable() {
        return this.goBackEnable;
    }

    /**
     * Setter for property goBackEnable.
     * @param goBackEnable New value of property goBackEnable.
     */
    public void setGoBackEnable(boolean goBackEnable) {
        boolean old = this.goBackEnable;
        this.goBackEnable = goBackEnable;
        propertyChangeSupport.firePropertyChange("goBackEnable", old, goBackEnable);
    }

    /**
     * Holds value of property goForwardEnable.
     */
    private boolean goForwardEnable;

    /**
     * Getter for property goForwardEnable.
     * @return Value of property goForwardEnable.
     */
    public boolean isGoForwardEnable() {
        return this.goForwardEnable;
    }

    /**
     * Setter for property goForwardEnable.
     * @param goForwardEnable New value of property goForwardEnable.
     */
    public void setGoForwardEnable(boolean goForwardEnable) {
        boolean old = this.goForwardEnable;
        this.goForwardEnable = goForwardEnable;
        propertyChangeSupport.firePropertyChange("goForwardEnable", old, goForwardEnable);
    }

    /**
     * Holds value of property toolbarChanged.
     */
    private boolean toolbarChanged;

    /**
     * Getter for property toolbarChanged.
     * @return Value of property toolbarChanged.
     */
    public boolean isToolbarChanged() {
        return this.toolbarChanged;
    }

    /**
     * Setter for property toolbarChanged.
     * @param toolbarChanged New value of property toolbarChanged.
     */
    public void setToolbarChanged(boolean toolbarChanged) {
        boolean old = this.toolbarChanged;
        this.toolbarChanged = toolbarChanged;
        propertyChangeSupport.firePropertyChange("toolbarChanged", old, toolbarChanged);
    }

    /**
     * Holds value of property documentReady.
     */
    private boolean documentReady;

    /**
     * Getter for property documentReady.
     * @return Value of property documentReady.
     */
    public boolean isDocumentReady() {
        return this.documentReady;
    }

    /**
     * Setter for property documentReady.
     * @param documentReady New value of property documentReady.
     */
    protected void setDocumentReady(boolean documentReady) {
        boolean old = this.documentReady;
        this.documentReady = documentReady;
        propertyChangeSupport.firePropertyChange("documentReady", old, documentReady);
    }
    
    public long getNativeHandle() {
        return brPeer.getNativeHandle();
    }
    
    public int  setActionFiler(int flag, boolean busyState)
    {
        return brPeer.setActionFiler(flag, busyState);
    }

    @Override
    public boolean isFocusOwner() {
        return super.isFocusOwner() || ( isPeerReady() && brPeer.hasFocus() );
    }
   
    public BrComponentPeer getBrPeer()
    {
        return brPeer;
    }
}
    