package com.frostwire.gui.webbrowser;

import java.awt.Component;

public interface WebBrowser {

    public Component getComponent();

    public void go(String url);

    public void back();

    public void forward();

    public void reload();

    public void stop();

    public void runJS(String code);

    public void function(BrowserFunction fn);
}
