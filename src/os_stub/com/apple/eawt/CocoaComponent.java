package com.apple.eawt;

import java.awt.Component;

public class CocoaComponent extends Component {

    private static final long serialVersionUID = 7600039181558780100L;

    public int createNSView() {
        return 0;
    }

    public long createNSViewLong() {
        return 0;
    }

    protected void sendMessage(int id, Object obj) {
    }
}
