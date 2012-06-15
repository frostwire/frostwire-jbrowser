package com.frostwire.gui.webbrowser;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import com.apple.eawt.CocoaComponent;

public class WebKitComponent extends CocoaComponent implements WebBrowser {

    private static final long serialVersionUID = -8610816510893757828L;

    private static final int JWebKit_loadURL = 1;
    private static final int JWebKit_loadHTMLString = 2;
    private static final int JWebKit_stopLoading = 3;
    private static final int JWebKit_reload = 4;
    private static final int JWebKit_goBack = 5;
    private static final int JWebKit_goForward = 6;
    private static final int JWebKit_runJS = 7;
    private static final int JWebKit_dispose = 8;

    private final Map<String, BrowserFunction> functions;

    private long nsObject = 0;

    private String url;

    public WebKitComponent() {
        functions = new HashMap<String, BrowserFunction>();
    }

    @Override
    public int createNSView() {
        return (int) createNSViewLong();
    }

    @Override
    public long createNSViewLong() {
        com.apple.concurrent.Dispatch.getInstance().getBlockingMainQueueExecutor().execute(new Runnable() {
            @Override
            public void run() {
                nsObject = createNSView1();
            }
        });

        return nsObject;
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(25, 25);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, 250);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (url != null) {
            sendMsg(JWebKit_loadURL, url);
        }
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void go(String url) {
        this.url = url;
        if (url != null)
            sendMsg(JWebKit_loadURL, url);
        else {
            sendMsg(JWebKit_loadURL, "about:blank");
        }
    }

    @Override
    public void back() {
        sendMsg(JWebKit_goBack);
    }

    @Override
    public void forward() {
        sendMsg(JWebKit_goForward);
    }

    @Override
    public void stop() {
        sendMsg(JWebKit_stopLoading);
    }

    @Override
    public void reload() {
        sendMsg(JWebKit_reload);
    }

    @Override
    public void runJS(String code) {
        sendMsg(JWebKit_runJS, code);
    }

    @Override
    public void function(BrowserFunction fn) {
        functions.put(fn.getName(), fn);
        runJS(fn.createJS());
    }

    public void startLoading(String url) {
    }

    public void finishLoading() {
    }

    public String callJava(String function, String data) {
        if (functions.containsKey(function)) {
            return functions.get(function).run(data);
        } else {
            return null;
        }
    }

    public boolean runJavaScriptAlertPanelWithMessage(String message) {
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    protected void dispose() {
        sendMsg(JWebKit_dispose);
    }

    private void sendMsg(int messageID) {
        if (nsObject != 0) {
            sendMessage(messageID, null);
        }
    }

    private void sendMsg(int messageID, Object message) {
        if (nsObject != 0) {
            sendMessage(messageID, message);
        }
    }

    private native long createNSView1();
}
