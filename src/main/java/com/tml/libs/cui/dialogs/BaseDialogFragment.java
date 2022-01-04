package com.tml.libs.cui.dialogs;

import android.app.Activity;

import androidx.fragment.app.DialogFragment;

/**
 * Created by ManhLinh on 4/9/2017.
 */

public class BaseDialogFragment<T> extends DialogFragment {
    private T mActivityInstance;

    public final T getActivityInstance() {
        return mActivityInstance;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivityInstance = (T) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance = null;
    }
}
