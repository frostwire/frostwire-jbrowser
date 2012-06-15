
#ifndef JIEXPLORER_H
#define JIEXPLORER_H

#include "BrIELWControl.h"
#include "BrHolderThread.h"
#include <ole2.h>
#include <richedit.h>
#include <richole.h>                                                           

/************************************************************************
 * JIExplorer class
 */

class JIExplorer 
:  public CBrIELWControl
{
private:
	static const DISPID DISPID_JBROWSER_CALLJAVA = DISPID_VALUE + 1;
public:
    static jclass    ms_IExplorerComponent;
    
	static jfieldID  ms_IExplorerComponent_x;
    static jfieldID  ms_IExplorerComponent_y;
	static jfieldID  ms_IExplorerComponent_width;
    static jfieldID  ms_IExplorerComponent_height;

	static jfieldID  ms_IExplorerComponent_data;

	static jmethodID ms_IExplorerComponent_postEvent;
    static jmethodID ms_IExplorerComponent_callJava;


public:
    static void initIDs(JNIEnv *env, jclass clazz);


    HRESULT getTargetRect(
        JNIEnv *env, 
        LPRECT prc);
    HRESULT create(    
        JNIEnv *env, 
        HWND    hParent,
        int     ePaintAlgorithm);
    JIExplorer(
        JNIEnv *env, 
        jobject othis);

    virtual void destroy(JNIEnv *env);

    JIExplorer();
    virtual ~JIExplorer();

    virtual HRESULT SendIEEvent(
        int iId,
        LPTSTR lpName, 
        LPTSTR lpValue,
        _bstr_t &bsResult = _bstr_t());
    virtual HRESULT JIExplorer::Connect(
        IN BSTR bsURL, 
        IN JNIEnv *env, 
        IN jobject jis);

    BrowserThread *GetThread() { return m_pThread; }

private:
    //IELWComp    
    virtual void RedrawParentRect(LPRECT pRect);
    virtual LRESULT NewIEProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
    RECT    m_rcInvalid;
    DWORD   m_dwKey;
    jobject m_this;
    BrowserThread *m_pThread;
public:
    boolean m_synthetic;
    boolean m_bBlockNativeInputHandler;
    HRGN    m_hChildArea;

public:
	virtual HRESULT STDMETHODCALLTYPE GetIDsOfNames(
            REFIID riid,
            LPOLESTR* rgszNames,
            UINT cNames,
            LCID lcid,
            DISPID* rgDispId);

    virtual HRESULT STDMETHODCALLTYPE Invoke(
            DISPID dispIdMember,
            REFIID riid,
            LCID lcid,
            WORD wFlags,
            DISPPARAMS  *pDispParams,
            VARIANT  *pVarResult,
            EXCEPINFO  *pExcepInfo,
            UINT  *puArgErr);

private:
	virtual void NavigateComplete();
	virtual void AddCustomObject(IDispatch* custObj, BSTR name);
	virtual void CallJava(DISPPARAMS* pDispParams, VARIANT* pVarResult);

public:
	void Go(IN BSTR bsURL);
	void GoBack();
	void GoForward();
	void Refresh(BOOL bClearCache);
};

#endif /* IEXPLORER_H */
