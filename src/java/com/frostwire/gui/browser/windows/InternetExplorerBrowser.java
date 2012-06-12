package com.frostwire.gui.browser.windows;

import java.awt.Component;

import com.frostwire.gui.browser.BrowserFunction;
import com.frostwire.gui.browser.WebBrowser;

public class InternetExplorerBrowser extends BrComponent implements WebBrowser {

    private static final long serialVersionUID = 4910282382001163472L;

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
}
