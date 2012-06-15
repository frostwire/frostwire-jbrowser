//////////////////////////////////
// Copyright (C) 2008 Sun Microsystems, Inc. All rights reserved. Use is
// subject to license terms.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the Lesser GNU General Public License as
// published by the Free Software Foundation; either version 2 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA.

// FrostWire Team: a lot of modifications
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
    IExplorer(
        JNIEnv *env, 
        jobject othis);

    virtual void destroy(JNIEnv *env);

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
