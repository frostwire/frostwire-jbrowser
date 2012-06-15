package com.frostwire.gui.webbrowser.windows;

import java.awt.Component;

import com.frostwire.gui.webbrowser.BrowserFunction;
import com.frostwire.gui.webbrowser.WebBrowser;

public class InternetExplorerBrowser extends BrComponent implements WebBrowser, BrComponentListener {

    private static final long serialVersionUID = 4910282382001163472L;

    public InternetExplorerBrowser() {
        addBrComponentListener(this);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void runJS(String code) {
        super.execJSLater(":" + code);
    }

    @Override
    public void function(BrowserFunction function) {
    }

    @Override
    public String sync(BrComponentEvent e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reload() {
        // TODO Auto-generated method stub
        
    }
}
