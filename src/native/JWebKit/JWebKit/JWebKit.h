//
//  JWebKit.h
//  JWebKit
//

#import <Foundation/Foundation.h>
#import <JavaVM/jni.h>
#import <JavaVM/AWTCocoaComponent.h>
#import <AppKit/AppKit.h>
#import <WebKit/WebView.h>
#import <WebKit/WebFrame.h>
#import <WebKit/WebPreferences.h>
#import <WebKit/WebDocument.h>
#import <WebKit/WebPolicyDelegate.h>

#ifdef __cplusplus
extern "C" {
#endif
    
    JNIEXPORT jlong JNICALL Java_com_frostwire_gui_browser_WebKitBrowser_createNSView1
    (JNIEnv *, jobject);
    
#ifdef __cplusplus
}
#endif

enum {
	JWebKit_loadURL         = 1,
    JWebKit_loadHTMLString  = 2,
    JWebKit_stopLoading     = 3,
    JWebKit_reload          = 4,
    JWebKit_goBack          = 5,
    JWebKit_goForward       = 6,
    JWebKit_runJS           = 7,
    JWebKit_dispose         = 8
};

@interface JWebKit : WebView<AWTCocoaComponent> {
@private
    jobject jowner;
    NSView* html;
}

- (id) initWithFrame: (jobject) owner frame:(NSRect) frame;

-(void)startLoading : (NSString *)url;
-(void)finishLoading;

-(NSString*) callJava: (NSString *)function data:(NSString *)data;

@end
