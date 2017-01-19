package com.framgia.mixrecorder.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.framgia.mixrecorder.R;

/**
 * Created by hoang on 1/16/2017.
 */
public class RenameDialogFragment extends DialogFragment {
    private static final String ARG_NAME = "Name";
    private RenameDialogFragment.RenameDialogListener mRenameDialogListener;
    private String mName;
    private EditText mEditTextName;

    public static RenameDialogFragment newInstance(String name) {
        RenameDialogFragment fragment = new RenameDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mRenameDialogListener == null) mRenameDialogListener = (RenameDialogFragment
            .RenameDialogListener)
            getTargetFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rename, null);
        mEditTextName = (EditText) view.findViewById(R.id.edit_text_name);
        mEditTextName.setText(mName);
        builder.setTitle(R.string.title_rename).setView(view)
            .setPositiveButton(R.string.action_rename, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mRenameDialogListener.onDialogRenameClick(mEditTextName.getText().toString());
                }
            })
            .setNegativeButton(R.string.action_cancel, null);
        return builder.create();
    }

    public interface RenameDialogListener {
        void onDialogRenameClick(String newName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RenameDialogListener) {
            mRenameDialogListener = (RenameDialogListener) context;
        }
    }
}
