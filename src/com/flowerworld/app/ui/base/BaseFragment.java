package com.flowerworld.app.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.flowerworld.app.R;
import com.flowerworld.app.interf.IActivityRequestThreadManager;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.ui.widget.Banner;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseFragment extends Fragment implements IActivityRequestThreadManager {
    protected final String TAG = this.getClass().getSimpleName();
    public final static int FRAME_CONTENT_ID = R.id.main_tab_frame_content;

    private ActivityRequestThreadManager requestThreadManager = new ActivityRequestThreadManager();
    private BaseTools base = null;
    private Banner mBanner = null;

    private static Map<String, Object> savedData = new HashMap<String, Object>();

    protected Object getSavedData() {
        return savedData.get(TAG);
    }

    protected void saveData(Object data) {
        LOG.e(TAG, "======savedData: " + TAG);
        savedData.put(TAG, data);
    }

    protected boolean isHasData() {
        return null == getSavedData() ? false : true;
    }

    private View mSavedView = null;

//	protected Fragment.SavedState fgState = null;

    protected void saveView(View v) {
        mSavedView = v;
    }

    protected View getSavedView() {
        return mSavedView;
    }

//	public void setSavedState()
//	{
//		if (null != fgState)
//			this.setInitialSavedState(fgState);
//	}

//	@Override
//	public void onAttach(Activity activity)
//	{
//		super.onAttach(activity);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        if (null == getSavedView()) {
            v = createView(inflater, container, savedInstanceState);
        } else {
            v = getSavedView();
            if (null != v.getParent()) {
                ((ViewGroup) v.getParent()).removeView(v);
            }
        }
        return v;
    }

//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState)
//	{
//		// TODO Auto-generated method stub
//		super.onViewCreated(view, savedInstanceState);
//	}

    @Override
    public void onStart() {
        if (null == mSavedView) {
            mSavedView = ((ViewGroup) getView()).getChildAt(0);
        }

        initView();

        if (isHasData()) {
            resumeData();
        } else {
            initData();
        }

        super.onStart();
    }

//	@Override
//	public void onStop()
//	{
//		fgState = getFragmentManager().saveFragmentInstanceState(this);
//		super.onStop();
//	}

    /**
     * 创建内容view，由onCreateView调用
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化内容view，由onStart调用。
     */
    protected abstract void initView();

    /**
     * 初始化内容view数据，由onStart调用。初始时调用
     */
    protected abstract void initData();

    /**
     * 初始化内容view数据，由onStart调用。唤醒时调用
     */
    protected abstract void resumeData();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        base = new BaseTools(getView());
    }

    protected FragmentTransaction getFragmentTransaction() {
        return getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out,
                R.anim.slide_right_in, R.anim.slide_right_out);
//		return getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    protected View findViewById(int id) {
        return getView().findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestThreadManager.stopAllCurrentActivityRequestThreads();
    }

    @Override
    public void onDetach() {
//		LOG.e(TAG, "======savedData.remove: " + TAG);
//		savedData.remove(TAG);
        super.onDetach();
    }

    public void requestHttp(IHttpProcess process) {
        HttpRequestFacade.requestHttp(this, process);
    }

//	public void requestHttp(int sign, IHttpProcess process)
//	{
//		HttpRequestFacade.requestHttp(this, process, sign);
//	}

    @Override
    public ActivityRequestThreadManager getRequestThreadManager() {
        return requestThreadManager;
    }

    protected void initBanner(int parent, int bg, int title, int titleBg, int leftText, int leftBg, int rightText, int rightBg,
            OnClickListener leftClk, OnClickListener rightClk) {
        removeBanner();
        mBanner = base.initBanner(parent, bg, title, titleBg, leftText, leftBg, rightText, rightBg, leftClk, rightClk);
    }

    protected Banner getBanner() {
        return mBanner;
    }

    protected void removeBanner() {
        Banner banner = getBanner();
        if (null == banner) {
            return;
        }

        ((ViewGroup) banner.getParent()).removeAllViews();
        mBanner = null;
    }

    /**
     * fragment的堆栈回退，为方便而使用与activity的回退同样的名字
     */
    protected void finish() {
        getFragmentManager().popBackStack();
    }

    @Override
    public Context getContextHanler() {
        return getActivity();
    }
}
