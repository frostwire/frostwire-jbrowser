package com.frostwire.gui.webbrowser;

public class BrowserFunction {

    private final String name;

    public BrowserFunction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String run(String data) {
        return null;
    }

    public String createJS() {
        StringBuilder sb = new StringBuilder("window.");
        sb.append(name);
        sb.append(" = function ");
        sb.append("(data) {return window.jbrowser.callJava('" + name + "', data);}");

        return sb.toString();
    }
}
