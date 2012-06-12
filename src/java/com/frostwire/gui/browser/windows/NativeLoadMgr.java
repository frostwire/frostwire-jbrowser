package com.frostwire.gui.browser.windows;

public class NativeLoadMgr {

    private NativeLoadMgr() {
    }

    public static synchronized boolean loadLibrary(String libname) {
        try {
            System.loadLibrary(libname);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
}
