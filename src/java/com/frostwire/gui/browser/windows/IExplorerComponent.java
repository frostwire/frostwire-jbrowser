package com.frostwire.gui.browser.windows;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.peer.ComponentPeer;
import java.io.InputStream;

import sun.awt.windows.WComponentPeer;

import com.frostwire.gui.browser.BrowserFunction;
import com.frostwire.gui.browser.WebBrowser;

public class IExplorerComponent extends Canvas implements WebBrowser {

    private static final long serialVersionUID = -7081910735920998419L;

    long        data = 0;
    
    protected int notifyCounter = 0;    
    protected boolean showOnZero = true;
    
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
        onAddNotify();        
    }
    
    /**
     * Removes the <code>BrComponent</code>'s peer.
     * The peer allows us to modify the appearance of the
     * <code>BrComponent</code> without changing its
     * functionality.
     */
    @Override
    public void removeNotify() {
        onRemoveNotify();
        super.removeNotify();
    }
    
    public String callJava(String name, String data) {
        return name + "-" + data;
    }
    
    public void onAddNotify() {
        if(0 == notifyCounter){
                initIDs();
                
                    @SuppressWarnings("deprecation")
                    ComponentPeer cp = getPeer();
                    if( (cp instanceof WComponentPeer) ){
                        long hWnd = ((WComponentPeer)cp).getHWnd();
                        data = create( hWnd, 4);
                    }
        }
        ++notifyCounter;
    }

    public void onRemoveNotify() {
        --notifyCounter;                
       if( 0==notifyCounter ){
            if( 0!=data ) {
               destroy();
               data = 0;
            }
        }
    }
    
    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void setUrl(String url) {
        setURL(url, null);
    }

    @Override
    public void back() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void forward() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void runJS(String code) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void function(BrowserFunction fn) {
        // TODO Auto-generated method stub
        
    }
    
    private static native void initIDs();
    native long create(long hwnd, int iPaintAlgorithm);
    public native void destroy();
    public native void setURL(String stURL, InputStream is);

    public native void resizeControl();
    public native void nativePaint();
    
    public void setSize( int width, int height ) 
    {
        super.setSize(width,height);
        if(data!=0)
            resizeControl();
    }

    public void setSize( Dimension d ) 
    {
        super.setSize(d);
        if(data!=0)
            resizeControl();
    }

    public void setBounds( int x, int y, int width, int height ) 
    {
        super.setBounds(x,y,width,height);
        if(data!=0)
            resizeControl();
    }

    public void setBounds( Rectangle r ) 
    {
        super.setBounds(r);
        if(data!=0)
            resizeControl();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (data != 0) {
            nativePaint();
        }
    }
}
