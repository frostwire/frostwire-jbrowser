#ifndef IEXPLORER_H
#define IEXPLORER_H

#include "BrIELWControl.h"
#include "BrHolderThread.h"
#include <ole2.h>
#include <richedit.h>
#include <richole.h>                                                           

/************************************************************************
 * IExplorer class
 */
enum EBrComponentEvent {
    DOCUMENT_COMPLITE = 1
};

class IExplorer 
:  public CBrIELWControl
{
public:
    static jclass    ms_IExplorerComponent;
    
	static jfieldID  ms_IExplorerComponent_x;
    static jfieldID  ms_IExplorerComponent_y;
	static jfieldID  ms_IExplorerComponent_width;
    static jfieldID  ms_IExplorerComponent_height;

	static jfieldID  ms_IExplorerComponent_data;
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
    IExplorer(
        JNIEnv *env, 
        jobject othis);

    virtual void destroy(JNIEnv *env);
    virtual void updateTransparentMask(LPRECT prc);
    virtual void setTransparent(boolean bTransparent);

    IExplorer();
    virtual ~IExplorer();

    virtual HRESULT SendIEEvent(
        int iId,
        LPTSTR lpName, 
        LPTSTR lpValue,
        _bstr_t &bsResult = _bstr_t());
	virtual void CallJava(DISPPARAMS* pDispParams, VARIANT* pVarResult);
    virtual HRESULT IExplorer::Connect(
        IN BSTR bsURL, 
        IN JNIEnv *env, 
        IN jobject jis);

    BrowserThread *GetThread() { return m_pThread; }

private:
    //IELWComp    
    virtual void RedrawParentRect(LPRECT pRect);
    virtual LRESULT NewIEProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

public:
    //actors     
    jintArray NativeDraw(LPRECT prcDraw, BOOL bToImage);
       
private:
    RECT    m_rcInvalid;
    DWORD   m_dwKey;
    jobject m_this;
    BrowserThread *m_pThread;
public:
    boolean m_synthetic;
    boolean m_bBlockNativeInputHandler;
    HRGN    m_hChildArea;
};

#endif /* IEXPLORER_H */
