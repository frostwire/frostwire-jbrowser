package com.frostwire.gui.browser;

import java.awt.Component;

public interface WebBrowser {

    public Component getComponent();

    public void runJS(String code);

    public void function(BrowserFunction fn);
}
