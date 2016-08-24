package com.b_designworks.android.utils.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.b_designworks.android.utils.MyDevice;

import butterknife.ButterKnife;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected static <T extends BaseDialogFragment> T show(@NonNull T dialogFragment,
                                                           @NonNull FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(dialogFragment.getClass().getName());
        if (prev != null) {
            ft.remove(prev);
            DialogFragment df = (DialogFragment) prev;
            df.dismissAllowingStateLoss();
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, dialogFragment.getClass().getName());
        return dialogFragment;
    }

    protected abstract UiInfo getUiInfo();

    protected UiInfo uiInfo;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        uiInfo = getUiInfo();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parseArguments(getArguments());
        }
        if (savedInstanceState != null) {
            _restoreState(savedInstanceState);
        }
    }

    protected void _restoreState(@NonNull Bundle savedState) {

    }

    protected void parseArguments(@NonNull Bundle args) {
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(uiInfo.getLayoutRes(), container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onResume() {
        super.onResume();
        int width = (int) (MyDevice.getScreenWidthInPixels() * 0.9);
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    protected Context context() { return getActivity(); }

    public FragmentActivity activity() { return getActivity(); }

    protected String textOf(@NonNull TextView tv) {
        return tv.getText().toString();
    }

}
