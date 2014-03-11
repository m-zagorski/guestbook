package com.appunite.guestbook.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.appunite.guestbook.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EntryDialog extends DialogFragment {

    private static final String ENTRY_CONTENT = "entry_content";

    @InjectView(R.id.entry_dialog_content)
    TextView mContent;

    public static EntryDialog newInstance(String content) {
        EntryDialog entryDialog = new EntryDialog();
        Bundle extra = new Bundle();
        extra.putString(ENTRY_CONTENT, content);
        entryDialog.setArguments(extra);
        return entryDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.entry_dialog_layout, null);
        ButterKnife.inject(this, view);
        mContent.setText(getArguments().getString(ENTRY_CONTENT));

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(view);

        return dialog;
    }

}
