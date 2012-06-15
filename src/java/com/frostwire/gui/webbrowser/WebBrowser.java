package com.frostwire.gui.webbrowser;

import java.awt.Component;

public interface WebBrowser {

    public Component getComponent();

    public void setUrl(String url);

    public void back();

    public void forward();

    public void refreshContent();

    public void stop();

    public void runJS(String code);

    public void function(BrowserFunction fn);
}
