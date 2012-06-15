package com.frostwire.gui.webbrowser;

import java.awt.Toolkit;

import com.frostwire.gui.webbrowser.macosx.WebKitBrowser;
import com.frostwire.gui.webbrowser.windows.IExplorerComponent;
import com.frostwire.gui.webbrowser.windows.InternetExplorerBrowser;

public final class BrowserFactory {

    private static final String IEXPLORER_LIBRARY = "JIExplorer";
    private static final String WEBKIT_LIBRARY = "JWebKit";

    private static final String OS_NAME = System.getProperty("os.name");

    private static final boolean IS_OS_WINDOWS = isCurrentOS("Windows");

    private static final boolean IS_OS_MAC = isCurrentOS("Mac");

    private static boolean nativeLibLoaded = false;

    private static BrowserFactory instance;

    public static synchronized BrowserFactory instance() {
        if (instance == null) {
            instance = new BrowserFactory();
        }
        return instance;
    }

    private BrowserFactory() {
    }

    private boolean loadLibrary() {
        if (!nativeLibLoaded) {
            try {
                //force loading of libjawt.so/jawt.dll
                Toolkit.getDefaultToolkit();

                if (IS_OS_WINDOWS) {
                    System.loadLibrary(IEXPLORER_LIBRARY);
                } else if (IS_OS_MAC) {
                    System.loadLibrary(WEBKIT_LIBRARY);
                }

                nativeLibLoaded = true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return nativeLibLoaded;
    }

    /**
     * Used to check whether the current operating system matches a operating
     * system name (osname).
     * 
     * @param osname
     *            Name of an operating system we are looking for as being part
     *            of the Sytem property os.name
     * @return true, if osname matches the current operating system,false if not
     *         and if osname is null
     */
    private static boolean isCurrentOS(String osname) {
        if (osname == null) {
            return false;
        } else {
            return (OS_NAME.indexOf(osname) >= 0);
        }
    }

    public WebBrowser createBrowser() {
        if (loadLibrary()) {
            if (IS_OS_WINDOWS) {
                //return new InternetExplorerBrowser();
                return new IExplorerComponent();
            } else if (IS_OS_MAC) {
                return new WebKitBrowser();
            }
        }

        return null;
    }
}
