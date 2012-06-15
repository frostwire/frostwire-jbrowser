package com.frostwire.gui.browser.windows;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.peer.ComponentPeer;
import java.io.InputStream;

import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sun.awt.windows.WComponentPeer;

import com.frostwire.gui.browser.BrowserFunction;
import com.frostwire.gui.browser.WebBrowser;

public class IExplorerComponent extends Canvas implements WebBrowser {

    private static final long serialVersionUID = -7081910735920998419L;

    long        data = 0;
    
    protected int notifyCounter = 0;    
    protected boolean showOnZero = true;
    
   public IExplorerComponent() {
        setMinimumSize(new Dimension(10, 10));
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
                
                for( Container c = getParent(); null != c; c = c.getParent() )
                {
                    @SuppressWarnings("deprecation")
                    ComponentPeer cp = getPeer();
                    if( (cp instanceof WComponentPeer) ){
                        //parentHW = c;
                        data = create( ((WComponentPeer)cp).getHWnd(), 4);
                        //acceptTargetURL();
                        //setEditable(target.isEditable());
                        break;
                    }
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
    public void refreshContent() {
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
    
    
    @Override
    public void setBounds( int x, int y, int width, int height ) 
    {
        super.setBounds(x,y,width,height);
        
        if(data!=0) {
//            Point pt = new Point(x, y);
//            SwingUtilities.convertPointToScreen(pt, this);
//            nativePosOnScreen(
//                    pt.x,
//                    pt.y,
//                    width,
//                    height);
           //nativePaint();
            resizeControl();
            //nativePaint();
        }
    }
    
    boolean isFocusOwner = false;
    
    @Override
    public void validate() {
        super.validate();
        if(data != 0) {
            //clearRgn();
        }
    }
    
    @Override
    public boolean hasFocus() {
        return isFocusOwner;
    }
    
    /**
     * Synchronous callback for notifications from native code.
     * @param iId the event identifier - <code>BrComponentEvent.DISPID_XXXX</code> const
     * @param stName the event name of (optional)
     * @param stValue the event paramenter(s) (optional)
     * @return the application respont
     */
    private String postEvent(
            int iId,
            String stName,
            String stValue)
    {
        switch(iId){
        case BrComponentEvent.DISPID_REFRESH:
            break;
        case BrComponentEvent.DISPID_DOCUMENTCOMPLETE:
//            if(isEditable()!=editable){
//                //System.out.println("setEditable(" + editable + ");");
//                enableEditing(editable);
//            } else if(editable) {
//                //System.out.println("refreshHard");
//                refreshHard();
//            }
//            documentReady = true;
            break;
        case BrComponentEvent.DISPID_ONFOCUCHANGE:
            isFocusOwner = Boolean.parseBoolean(stValue);
            break;
        case BrComponentEvent.DISPID_ONFOCUSMOVE:
            focusMove(Boolean.parseBoolean(stValue));
            break;
        case BrComponentEvent.DISPID_PROGRESSCHANGE:
            break;
        }        
        return processBrComponentEvent(
                new BrComponentEvent(this, iId, stName, stValue));
    }
    
    public native void clearRgn();
    public native void clipChild(int top, int left, int width, int height);
    public native String execJS(String code);
    public native void nativePosOnScreen(int x, int y, int width, int height);
    
    private void focusMove(final boolean bNext)
    {
        SwingUtilities.invokeLater ( new Runnable() {
            public void run() {
                KeyboardFocusManager fm = FocusManager.getCurrentKeyboardFocusManager();
                if(bNext) {
                   fm.focusNextComponent(IExplorerComponent.this);
                } else {
                   fm.focusPreviousComponent(IExplorerComponent.this);
                }    
            }
        });
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
       BrComponentListener listener = null;//ieListener;
       if (listener != null) {
           res = listener.sync(e);
       }
       SwingUtilities.invokeLater ( new Runnable() { public void run() {
               //System.out.println(e);
               String stValue = e.getValue();
               stValue = (null==stValue)?"":stValue; 
               switch(e.getID()){
               case BrComponentEvent.DISPID_STATUSTEXTCHANGE:
                   //setStatusText(stValue);
                   break;
               case BrComponentEvent.DISPID_BEFORENAVIGATE2:
                   break;
               case BrComponentEvent.DISPID_NAVIGATECOMPLETE2:
                   //setNavigatedURL(stValue);
                   break;
               case BrComponentEvent.DISPID_DOWNLOADCOMPLETE:
                   //setDocumentReady(true);
                   break;
               case BrComponentEvent.DISPID_PROGRESSCHANGE:
                   //setProgressBar(stValue);
                   break;
               case BrComponentEvent.DISPID_SETSECURELOCKICON:
                   //setSecurityIcon(stValue);
                   break;
               case BrComponentEvent.DISPID_TITLECHANGE:
                   //setWindowTitle(stValue);
                   break;
               case BrComponentEvent.DISPID_COMMANDSTATECHANGE:
                   {
                       String st[] = stValue.split(",");
                       boolean enable = (0!=Integer.valueOf(st[0]));
                       switch(Integer.valueOf(st[1])){
                       case -1:    
                           ///setToolbarChanged(enable);                            
                           break;
                       case 1:    
                           //setGoForwardEnable(enable);                            
                           break;
                       case 2:    
                           //setGoBackEnable(enable);                                                        
                           break;                            
                       }
                   }    
                   break;
               }
       }});//end posponed operation   
       return res;
   }
    
    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        //System.out.println("paint");
        if (data != 0) {
            //nativePaint();
           //resizeControl();
        }
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        if (data != 0) {
            //resizeControl();
            //nativePaint();
        }
    }
    
}
