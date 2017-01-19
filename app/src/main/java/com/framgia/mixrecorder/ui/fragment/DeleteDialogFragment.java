package com.framgia.mixrecorder.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.framgia.mixrecorder.R;

/**
 * Created by hoang on 1/15/2017.
 */
public class DeleteDialogFragment extends DialogFragment {
    private DeleteDialogListener mDeleteDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDeleteDialogListener == null) mDeleteDialogListener = (DeleteDialogListener)
            getTargetFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.msg_delete)
            .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mDeleteDialogListener.onDialogPositiveClick();
                }
            })
            .setNegativeButton(R.string.action_no, null);
        return builder.create();
    }

    public interface DeleteDialogListener {
        public void onDialogPositiveClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteDialogListener) {
            mDeleteDialogListener = (DeleteDialogListener) context;
        }
    }
}
