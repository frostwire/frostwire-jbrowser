
#include "stdafx.h" 
#include "IExplorer.h"
#include "BrMain.h"
#include "BrHolderThread.h"
#include "windowsx.h"


//struct __declspec(uuid("BB1A2AE1-A4F9-11CF-8F20-00805F2CD064")) IActiveScript;
_COM_SMARTPTR_TYPEDEF(IActiveScript, __uuidof(IActiveScript));

//struct __declspec(uuid("BB1A2AE2-A4F9-11CF-8F20-00805F2CD064")) IActiveScriptParse;
_COM_SMARTPTR_TYPEDEF(IActiveScriptParse, __uuidof(IActiveScriptParse));


#if defined(__IHTMLDocument2_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IHTMLDocument2, __uuidof(IHTMLDocument2));
#endif// #if defined(__IHTMLDocument2_INTERFACE_DEFINED__)
#if defined(__IHTMLDocument3_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IHTMLDocument3, __uuidof(IHTMLDocument3));
#endif// #if defined(__IHTMLDocument3_INTERFACE_DEFINED__)
#if defined(__IDisplayServices_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IDisplayServices, __uuidof(IDisplayServices));
#endif// #if defined(__IDisplayServices_INTERFACE_DEFINED__)
#if defined(__IHTMLCaret_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IHTMLCaret, __uuidof(IHTMLCaret));
#endif// #if defined(__IHTMLCaret_INTERFACE_DEFINED__)
#if defined(__IHTMLWindow2_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IHTMLWindow2, __uuidof(IHTMLWindow2));
#endif// #if defined(__IHTMLWindow2_INTERFACE_DEFINED__)
#if defined(__IHTMLElement_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IHTMLElement, __uuidof(IHTMLElement));
#endif// #if defined(__IHTMLElement_INTERFACE_DEFINED__)
/*
#if defined(__IActiveScript_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IActiveScript, __uuidof(IActiveScript));
#endif// #if defined(__IActiveScript_INTERFACE_DEFINED__)
#if defined(__IActiveScriptError_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IActiveScriptError, __uuidof(IActiveScriptError));
#endif// #if defined(__IActiveScriptError_INTERFACE_DEFINED__)
*/


#if defined(__IActiveScriptParseProcedure_INTERFACE_DEFINED__)
_COM_SMARTPTR_TYPEDEF(IActiveScriptParseProcedure, __uuidof(IActiveScriptParseProcedure));
#endif// #if defined(__IActiveScriptParseProcedure_INTERFACE_DEFINED__)


jclass    IExplorer::ms_IExplorerComponent = NULL;

jfieldID  IExplorer::ms_IExplorerComponent_x = NULL;
jfieldID  IExplorer::ms_IExplorerComponent_y = NULL;
jfieldID  IExplorer::ms_IExplorerComponent_width = NULL;
jfieldID  IExplorer::ms_IExplorerComponent_height = NULL;

jfieldID  IExplorer::ms_IExplorerComponent_data = NULL;
jmethodID IExplorer::ms_IExplorerComponent_callJava = NULL;

void IExplorer::initIDs(JNIEnv *env, jclass clazz)
{
    ms_IExplorerComponent = getGlobalJavaClazz(
        env,
        "com/frostwire/gui/browser/windows/IExplorerComponent"
    );

    ms_IExplorerComponent_x = env->GetFieldID(ms_IExplorerComponent, "x", "I");
    ms_IExplorerComponent_y = env->GetFieldID(ms_IExplorerComponent, "y", "I");
    ms_IExplorerComponent_width = env->GetFieldID(ms_IExplorerComponent, "width", "I");
    ms_IExplorerComponent_height = env->GetFieldID(ms_IExplorerComponent, "height", "I");
    
	ms_IExplorerComponent_data = env->GetFieldID(clazz, "data", "J");
    ms_IExplorerComponent_callJava = env->GetMethodID(
        clazz, 
        "callJava", 
        "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
}


/************************************************************************
 * IExplorer methods
 */

IExplorer::IExplorer(
    JNIEnv *env, 
    jobject othis
)
:m_synthetic(false),
 m_bBlockNativeInputHandler(false),
 m_this(makeGlobal(env, othis)),
 m_hChildArea(CreateRectRgn(0,0,0,0)),
 m_pThread(BrowserThread::GetInstance())
{
}

HRESULT IExplorer::getTargetRect(
    JNIEnv *env, 
    LPRECT prc)
{
    if(NULL!=m_this){
        prc->left = env->GetIntField(m_this, ms_IExplorerComponent_x);
        prc->top = env->GetIntField(m_this, ms_IExplorerComponent_y);
        prc->right = prc->left + env->GetIntField(m_this, ms_IExplorerComponent_width);
        prc->bottom = prc->top + env->GetIntField(m_this, ms_IExplorerComponent_height);
        return S_OK;
    }
    return E_INVALIDARG;
}

HRESULT IExplorer::create(    
    JNIEnv *env, 
    HWND    hParent,
    int     ePaintAlgorithm)
{
    SEP(_T("create"))
    m_ePaintAlgorithm = ePaintAlgorithm;
    STRACE(_T("paint: %04x"), ePaintAlgorithm);
    OLE_TRY
    RECT rcIE = {0};
    SetWindowLong(
        hParent, 
        GWL_STYLE, 
        GetWindowLong(hParent, GWL_STYLE) & ~(WS_CLIPCHILDREN | WS_CLIPSIBLINGS) );

    OLE_HRT( getTargetRect(
        env, 
        &rcIE))
    OLE_HRT( CreateControl(
        hParent,
        &rcIE,
        NULL))

    HWND hFO = GetFocus();
    if( GetTopWnd()==hFO || GetIEWnd()==hFO ){
        SetFocus(GetParent());
    }
    
	OLE_CATCH
    OLE_RETURN_HR
}

IExplorer::~IExplorer()
{
    SEP0(_T("~BrJComponent"));
    if(!bool(m_spIWebBrowser2) || NULL!=m_this){
        STRACE1(_T("alarm!"));
    }
    if(m_pThread){
        m_pThread->Release();
    }
}

void IExplorer::destroy(JNIEnv *env)
{
    if(m_spIWebBrowser2){
        OLE_TRY
        OLE_HRT( DestroyControl() )
        OLE_CATCH
    }
    releaseGlobal(env, m_this);
    if(m_hChildArea){
        DeleteObject((HGDIOBJ)m_hChildArea);
        m_hChildArea = NULL;
    }
    m_this = NULL;
}

void IExplorer::updateTransparentMask(LPRECT prc)
{
}

void IExplorer::setTransparent(boolean bTransparent)
{
 
}


void IExplorer::RedrawParentRect(LPRECT pRect)
{  
        
}

HRESULT IExplorer::SendIEEvent(
    int iId,
    LPTSTR lpName, 
    LPTSTR lpValue,
    _bstr_t &bsResult)
{
                 return S_OK;                    
}

void IExplorer::CallJava(DISPPARAMS* pDispParams, VARIANT* pVarResult)
{
	if (pDispParams->cArgs == 2)
	{
		BSTR bstrArgName = pDispParams->rgvarg[1].bstrVal;
		BSTR bstrArgData = pDispParams->rgvarg[0].bstrVal;

		JNIEnv *env = (JNIEnv *)JNU_GetEnv(jvm, JNI_VERSION_1_2);
		if (NULL != env)
		{
			jstring jsName = JNU_NewStringPlatform(env, bstrArgName);
			jstring jsData = JNU_NewStringPlatform(env, bstrArgData);
            
			jstring jsRes = (jstring)env->CallObjectMethod(
                    m_this, 
                    ms_IExplorerComponent_callJava,
                    jsName, 
                    jsData);
			
			if (NULL != jsRes)
			{
				pVarResult->vt = VT_BSTR;
				pVarResult->bstrVal = SysAllocString(JStringBuffer(env, jsRes));
				env->DeleteLocalRef(jsRes);
			}

            env->DeleteLocalRef(jsName);
			env->DeleteLocalRef(jsData);
        }
	}
}

LRESULT IExplorer::NewIEProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	return ::CallWindowProc((WNDPROC)GetOldIEWndProc(), hWnd, msg, wParam, lParam);

    LRESULT lRes = 0;
    switch(msg){
    case WM_SETFOCUS:
        setTransparent(false);
        SendIEEvent(-1, _T("OnFocusMove"), _T("true"));
        STRACE0(_T("WM_SETFOCUS"));
        break;
    case WM_KILLFOCUS:
        SendIEEvent(-1, _T("OnFocusMove"), _T("false"));
        STRACE0(_T("WM_KILLFOCUS"));
        setTransparent(true);
        break;
    case WM_KEYDOWN:
    case WM_KEYUP:
    case WM_SYSKEYDOWN:
    case WM_SYSKEYUP:
    case WM_CHAR:
    case WM_SYSCHAR:
    //case WM_INPUT:
        lRes = CBrIELWControl::NewIEProc(hWnd, msg, wParam, lParam);
        return lRes;
    case WM_SETCURSOR:    
        lRes = CBrIELWControl::NewIEProc(hWnd, msg, wParam, lParam);
        {
            //POINT pt;
            //if( GetCursorPos(&pt) && hWnd == WindowFromPoint(pt) ){
			/*
                JNIEnv *env = (JNIEnv *)JNU_GetEnv(jvm, JNI_VERSION_1_2);
                if( NULL!=env ) {
                    jobject target = env->GetObjectField(m_this, ms_jcidWBrComponentPeer_target);
                    if(NULL!=target){
                        jobject ocursor = env->CallObjectMethod(
                            target, 
                            ms_jcidWBrComponent_getCursor);
                        if(NULL!=ocursor){
                            jlong pData = env->GetLongField(ocursor, ms_jcidCursor_pData);
                            if(0!=pData){
                                //Warning:HACK!!!
                                //pData is a pointer to AwtCursor
                                *(HCURSOR *)((LPBYTE)pData + 0x44) = GetCursor();
                                //STRACE1(_T("SET_CURSOR"));
                            }
                            env->DeleteLocalRef(ocursor);
                        }
                        env->DeleteLocalRef(target);
                    }    
                }
				*/
            //}
        }
        return lRes;

    case WM_MOUSEACTIVATE:

    case WM_MOUSEMOVE: 
    case WM_NCLBUTTONDBLCLK:
    case WM_NCLBUTTONDOWN:
    case WM_NCLBUTTONUP:

    case WM_NCRBUTTONDBLCLK:
    case WM_NCRBUTTONDOWN:
    case WM_NCRBUTTONUP:

    case WM_NCMBUTTONDBLCLK:
    case WM_NCMBUTTONDOWN:
    case WM_NCMBUTTONUP:

    case WM_LBUTTONDBLCLK:
    case WM_LBUTTONDOWN:
    case WM_LBUTTONUP:

    case WM_RBUTTONDBLCLK:
    case WM_RBUTTONDOWN:
    case WM_RBUTTONUP:

    case WM_MBUTTONDBLCLK: 
    case WM_MBUTTONDOWN:
    case WM_MBUTTONUP: 

    case WM_MOUSEWHEEL:
        //AwtComponent::WindowProc(msg, wParam, lParam);
        STRACE0(_T("mouse msg:%08x"), msg);
        if( !m_bBlockNativeInputHandler ) {
           lRes = CBrIELWControl::NewIEProc(hWnd, msg, wParam, lParam);
        }
        return lRes;
    default:
        break;
    }
    lRes = CBrIELWControl::NewIEProc(hWnd, msg, wParam, lParam);
    return lRes;
}

jintArray IExplorer::NativeDraw(LPRECT prcDraw, BOOL bToImage)
{
	return NULL;
}

struct JavaInputStream : public CStubStream
{
    static jclass ms_jcidInputStream;
    static jmethodID ms_jcidInputStream_readBytes; 

    static void initIDs(
        IN JNIEnv *env)
    {
        if(NULL==ms_jcidInputStream){
            ms_jcidInputStream = getGlobalJavaClazz(
                env,
                "java/io/InputStream"
            );
            ms_jcidInputStream_readBytes = env->GetMethodID(ms_jcidInputStream, "read", "([BII)I");
        }
    }

    JavaInputStream(
        IN JNIEnv *env,
        IN jobject jis
    ): CStubStream(),
       m_jis(jis),
       m_env(env)
    {
        initIDs(env);
    }

    virtual  HRESULT STDMETHODCALLTYPE Read(
        OUT void *pv,
        IN  ULONG cb,
        OUT ULONG *pcbRead)
    {
        OLE_DECL
        //java read
        //No more exceptions here!
        jbyteArray jba = m_env->NewByteArray( jsize(cb) );
        if( NULL == jba ){
            OLE_HR = E_OUTOFMEMORY;
        } else {
            jint ret = 0, read;
            while(0 < cb){
                read = m_env->CallIntMethod(
                    m_jis, 
                    ms_jcidInputStream_readBytes,
                    jba,
                    ret,
                    cb);
                if( m_env->ExceptionCheck() ){
                    OLE_HR = E_JAVAEXCEPTION;
                    break;
                }
                if( -1 == read ){
                    break;
                }
                cb -= read;
                ret += read;
            }

            //copy to stream
            if(SUCCEEDED(OLE_HR)){
                jbyte *pSrc = m_env->GetByteArrayElements(jba, NULL);
                if(NULL==pSrc){
                    OLE_HR = E_OUTOFMEMORY;
                } else {
                    memcpy(pv, pSrc, ret);
                    if(pcbRead){
                        *pcbRead = ret;
                    }
                    m_env->ReleaseByteArrayElements(jba, pSrc, JNI_ABORT);
                }
            }
            m_env->DeleteLocalRef(jba);
        }  
        return S_OK;
    }

    jobject m_jis;
    JNIEnv *m_env; 
};


HRESULT IExplorer::Connect(
    IN BSTR bsURL, 
    IN JNIEnv *env, 
    IN jobject jis)
{
    OLE_DECL
    if(NULL!=jis){
        OLE_NEXT_TRY
        IHTMLDocument2Ptr doc;     
        OLE_HRT( m_spIWebBrowser2->get_Document((LPDISPATCH *)&doc) )
        OLE_CHECK_NOTNULLSP(doc)
        IPersistStreamInitPtr ipsi(doc);
        JavaInputStream is(env, jis);
	OLE_HRT( ipsi->InitNew() )
	OLE_HRT( ipsi->Load(&is) )
        OLE_CATCH
    }else{
        OLE_HR = CBrIELWControl::Connect(bsURL);
    }
    OLE_RETURN_HR
}

/************************************************************************
 * IExplorer native methods
 */

extern "C" {

/*
 * Class:     com_frostwire_gui_browser_windows_IExplorerComponent
 * Method:    initIDs
  */
JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_initIDs(
    JNIEnv *env, 
    jclass cls)
{
    IExplorer::initIDs(env, cls);
}

/*
 * Class:     com_frostwire_gui_browser_windows_IExplorerComponent
 * Method:    create
  */
struct CreateAction2 : public BrowserAction
{
    HWND    m_parent;
    IExplorer *m_pThis;
    int m_ePaintAlgorithm;

    CreateAction2(
        IExplorer *pThis,
        HWND parent,
        int ePaintAlgorithm)
    : m_pThis(pThis),
      m_parent(parent),
      m_ePaintAlgorithm(ePaintAlgorithm)
    {}

    virtual HRESULT Do(JNIEnv *env){
        return m_pThis->create(env, m_parent, m_ePaintAlgorithm);
    }
};

JNIEXPORT jlong JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_create(
    JNIEnv *env, 
    jobject self,
    jlong parent,
    jint  ePaintAlgorithm)
{
	IExplorer *pThis = new IExplorer(env, self);
	if(pThis){
        OLE_TRY
        OLE_HRT(pThis->GetThread()->MakeAction(
            env,
            "Browser create error",
            CreateAction2(
                pThis,
                (HWND)parent,
                ePaintAlgorithm)));
        OLE_CATCH
        if(FAILED(OLE_HR)){
            pThis->destroy(env);
            delete pThis;
            pThis = NULL;
        }
    }
    return jlong(pThis);
}

/*
 * Class:     com_frostwire_gui_browser_windows_IExplorerComponent
 * Method:    destroy
  */
struct DestroyAction : public BrowserAction
{
    IExplorer *m_pThis;
    DestroyAction(IExplorer *pThis)
    : m_pThis(pThis)
    {}

    virtual HRESULT Do(JNIEnv *env){
        m_pThis->destroy(env);
        return S_OK;
    }
};
JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_destroy(
    JNIEnv *env, 
    jobject self)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis){
        pThis->GetThread()->MakeAction(
            env,
            "Browser destroy error",
            DestroyAction(pThis));
        delete pThis;
        env->SetLongField(self, IExplorer::ms_IExplorerComponent_data, 0L);
    }
}

/*
 * Class:     sun.awt.windows.WBrComponentPeer
 * Method:    execJS
 */
struct ExecJSAction : public BrowserAction
{
    IExplorer *m_pThis;
    JStringBuffer m_jstCode;
    _bstr_t m_bsResult;

    ExecJSAction(
        IExplorer *pThis,
        JNIEnv *env,
        jstring jsCode
    ): m_pThis(pThis),
       m_jstCode(env, jsCode),
       m_bsResult(_T("error:Null interface"))
    {}

    virtual HRESULT Do(JNIEnv *env)
    {
        SEP(_T("_ExecJS"))
        LPTSTR pCode = (LPTSTR)m_jstCode;
        STRACE(_T("code:%s"), pCode);
        if(NULL==pCode){
            return S_FALSE;
        } else if( 0==_tcsncmp(pCode, _T("##"), 2) ){
            if( 0==_tcscmp(pCode, _T("##stop")) ){
                OLE_TRY
                OLE_HRT( m_pThis->m_spIWebBrowser2->Stop() );
                OLE_CATCH
                OLE_RETURN_HR
            } else if( 0==_tcsncmp(pCode, _T("##setFocus"), 10) ){
                OLE_TRY
                STRACE0(_T("##setFocus"));
                IOleObjectPtr spOleObject(m_pThis->m_spIWebBrowser2);
                OLE_CHECK_NOTNULLSP(spOleObject)
                OLE_HRT( spOleObject->DoVerb(
                    OLEIVERB_UIACTIVATE, 
                    NULL, 
                    m_pThis, 
                    0, 
                    NULL, 
                    NULL))
                if( 0!=_tcscmp(pCode, _T("##setFocus(false)")) ){
                    HWND hwnd = m_pThis->GetIEWnd();
                    if(NULL==hwnd)
                        hwnd = m_pThis->GetTopWnd();
                    SendMessage(hwnd, WM_KEYDOWN, VK_TAB, 0x0000000);
                    SendMessage(hwnd, WM_KEYUP, VK_TAB, 0xc000000);
                }
                //::OLE_CoPump();//Flash hole. Dead circle if is.
                OLE_CATCH
                OLE_RETURN_HR
            } else if( 0==_tcsncmp(pCode, _T("##setNativeDraw("), 16) ) {
                m_pThis->m_ePaintAlgorithm = pCode[17] - _T('0'); 
                //STRACE(_T(">>>>>>>>NativeDraw %d"), m_pThis->m_ePaintAlgorithm);
                return S_OK;
            } else if( 0==_tcsncmp(pCode, _T("##showCaret"), 11) ) {
                OLE_TRY
                IWebBrowser2Ptr br(m_pThis->m_spIWebBrowser2);
                OLE_CHECK_NOTNULLSP(br)

                IHTMLDocument2Ptr doc;     
                OLE_HRT( br->get_Document((LPDISPATCH *)&doc) )
                OLE_CHECK_NOTNULLSP(doc)
                
                IDisplayServicesPtr ds(doc);                     
                OLE_CHECK_NOTNULLSP(ds)

                IHTMLCaretPtr cr;                     
                OLE_HRT( ds->GetCaret(&cr) )
                OLE_CHECK_NOTNULLSP(cr)

                if( 0==_tcscmp(pCode, _T("##showCaret(true)")) ){
                   OLE_HRT( cr->Show(FALSE) )
                   STRACE1(_T("{Show------"));
                } else {
                   OLE_HRT( cr->Hide() )
                   STRACE1(_T("}Hide------"));
                }
                OLE_CATCH
                OLE_RETURN_HR
            }
            return S_FALSE;
        }
        OLE_TRY
        IWebBrowser2Ptr br(m_pThis->m_spIWebBrowser2);
        OLE_CHECK_NOTNULLSP(br)

        //that can be any type of document
        //Acrobat Reader for example
        IDispatchPtr docO;     
        OLE_HRT( br->get_Document(&docO) )

        //we are trying customize it to HTML document
        //empty document is a valid argument
        IHTMLDocument2Ptr doc(docO);     
        OLE_CHECK_NOTNULLSP(doc)

        IHTMLWindow2Ptr wnd;
        OLE_HRT( doc->get_parentWindow(&wnd) )
        OLE_CHECK_NOTNULLSP(wnd)

        _variant_t vtResult;
        STRACE0(_T("makeScript"));
        _bstr_t bsEval( ('#'!=*pCode && ':'!=*pCode)
            ? (_B(L"document.documentElement.setAttribute(\'javaEval\', eval(\'") + pCode + L"\'))")
            : _B(pCode+1)
        );
        
        STRACE0(_T("execScript"));
        OLE_HRT( wnd->execScript( bsEval, _B(""), &vtResult) )
        vtResult.Clear();

        if( ':'!=*pCode ){
            IHTMLDocument3Ptr doc3(doc);     
            OLE_CHECK_NOTNULLSP(doc3)
            
            IHTMLElementPtr el;
            OLE_HRT( doc3->get_documentElement(&el))
            OLE_CHECK_NOTNULLSP(el)

            OLE_HRT( el->getAttribute(_B("javaEval"), 0, &vtResult) )
        }

        if(VT_NULL!=vtResult.vt){
            m_bsResult = vtResult;
        } else {
            m_bsResult = "";
        }
        STRACE0(_T("result:%s"), (LPCTSTR)m_bsResult);
        OLE_CATCH
        if(FAILED(OLE_HR)){
            m_bsResult = _T("error:");
            m_bsResult += _B(_V(OLE_HR));
            m_bsResult += _T("code:");
            m_bsResult += pCode;
        }
        OLE_RETURN_HR
    }
};

JNIEXPORT jstring JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_execJS(
    JNIEnv *env, 
    jobject self,
    jstring jsCode)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis){
        OLE_TRY
        ExecJSAction a(
            pThis,
            env,
            jsCode);
        OLE_HRT( pThis->GetThread()->MakeAction(
            env,
            "Browser JScript execution error",
            a));
        LPCTSTR lpRes = a.m_bsResult;
        return JNU_NewStringPlatform(env, (LPCTSTR) (lpRes ? lpRes : _T("")));
        OLE_CATCH
    }
    return 0;
}

/*
 * Class:     org_jdic_web_peer_WBrComponentPeer
 * Method:    setURL
 * Signature: (Ljava/lang/String;)V
 */
struct SetURLAction : public BrowserAction{
    JStringBuffer m_jstURL;
    IExplorer *m_pThis;
    jobject m_jisURL;

    SetURLAction(
        IExplorer *pThis,
        JNIEnv *env,
        jstring jURL,
        jobject jisURL
    ):m_pThis(pThis),
      m_jstURL(env, jURL),
      m_jisURL(makeGlobal(env, jisURL))
    {}
    virtual HRESULT Do(JNIEnv *env)
    {
        OLE_TRY
        OLE_HRT( m_pThis->Connect(_B(m_jstURL), env, m_jisURL) )
        OLE_CATCH_ALL
        if(!repeatOnError(OLE_HR)){
            releaseGlobal(env, m_jisURL);
        }
        OLE_RETURN_HR
    }
};

JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_setURL(
    JNIEnv *env, 
    jobject self,
    jstring jsURL,
    jobject jisURL)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis){
        pThis->GetThread()->MakeAction(
            env,
            "URL navigation error",
            SetURLAction(
                pThis,
                env,
                jsURL,
                jisURL));
    }
}


/*
 * Class:     com_frostwire_gui_browser_windows_WBrComponentPeer
 * Method:    setVisible
 */
struct ShowAction : public BrowserAction
{
    IExplorer *m_pThis;
    BOOL bShow;

    ShowAction(
        IExplorer *pThis,
        BOOL _bShow
    ):m_pThis(pThis),
      bShow(_bShow)
    {}
    virtual HRESULT Do(JNIEnv *env)
    {
        ::ShowWindow(m_pThis->GetTopWnd(), bShow ? SW_SHOW : SW_HIDE );        
        return S_OK; 
    }
};

JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_setVisible(
    JNIEnv *env, 
    jobject self,
    jboolean aFlag)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis){
        pThis->GetThread()->MakeAction(
            env,
            "ShowWindow error",
            ShowAction(
                pThis,
                aFlag));
    }
}

/*
 * Class:     com_frostwire_gui_browser_windows_WBrComponentPeer
 * Method:    setEnabled
 */
struct EnableAction : public BrowserAction
{
    IExplorer *m_pThis;
    BOOL bEnable;

    EnableAction(
        IExplorer *pThis,
        BOOL _bEnable
    ):m_pThis(pThis),
      bEnable(_bEnable)
    {}
    virtual HRESULT Do(JNIEnv *env)
    {
        ::EnableWindow(m_pThis->GetTopWnd(), bEnable);
        return S_OK; 
    }
};

JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_setEnabled(
    JNIEnv *env, 
    jobject self,
    jboolean enabled)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis){
        pThis->GetThread()->MakeAction(
            env,
            "EnableWindow error",
            EnableAction(
                pThis,
                enabled));
    }
}

/*
 * Class:     com_frostwire_gui_browser_windows_WBrComponentPeer
 * Method:    clearRgn
 */
JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_clipChild(
    JNIEnv *env, 
    jobject self,
    jint x, jint y, jint w, jint h)
{
    SEP0(_T("clipChild"))
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis ){
        HRGN rg = CreateRectRgn(x, y, x+w, y+h);
        if(NULL!=rg){
            CombineRgn(
                pThis->m_hChildArea,
                pThis->m_hChildArea,
                rg,
                RGN_OR
            );
            DeleteObject((HGDIOBJ)rg);
        }
    }
}

/*
 * Class:     com_frostwire_gui_browser_windows_IExplorerComponent
 * Method:    nativeRepaint
 */
JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_nativeRepaint(
    JNIEnv *env, 
    jobject self)
{
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis ){
		RECT rc;
		::GetWindowRect(pThis->GetParent(), &rc);
		HWND hwndChild = GetWindow(pThis->GetParent(), GW_CHILD);
       ::InvalidateRect(hwndChild, &rc, TRUE);
	   ::UpdateWindow(hwndChild);
    }
}

/*
 * Class:     com_frostwire_gui_browser_windows_IExplorerComponent
 * Method:    clearRgn
 */
JNIEXPORT void JNICALL Java_com_frostwire_gui_browser_windows_IExplorerComponent_resizeControl(
    JNIEnv *env, 
    jobject self)
{
    SEP0(_T("resizeControl"))
    IExplorer *pThis = (IExplorer *)env->GetLongField(self, IExplorer::ms_IExplorerComponent_data);
    if(pThis ){
		RECT rc;
		::GetWindowRect(pThis->GetParent(),&rc);
		HWND hwndChild = GetWindow(pThis->GetParent(), GW_CHILD);
        ::SetWindowPos(hwndChild,NULL,0,0,rc.right-rc.left,rc.bottom-rc.top,SWP_NOZORDER|SWP_NOACTIVATE|SWP_SHOWWINDOW|SWP_NOMOVE);
		::InvalidateRect(pThis->GetTopWnd(), &rc, FALSE);
    }
}

} /* extern "C" */
