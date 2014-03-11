package com.appunite.guestbook.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.appunite.guestbook.R;
import com.appunite.guestbook.helpers.FormValidator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewEntryDialog extends DialogFragment implements TextWatcher{

    public interface NewEntryListener{
        void onNewEntryFinish(String content);
    }

    private static final int CHARACTER_LIMIT = 200;
    private static final int TARGET_REQ_CODE = 1;

    @InjectView(R.id.content)
    EditText mContent;
    @InjectView(R.id.new_entry_dialog_char_limit)
    TextView mCharLimit;

    private FormValidator mFormValidator;

    public static NewEntryDialog newInstance(NewEntryListener listener){
        NewEntryDialog newEntryDialog = new NewEntryDialog();
        newEntryDialog.setTargetFragment((Fragment) listener, TARGET_REQ_CODE);
        return newEntryDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.new_entry_dialog_layout, null);
        ButterKnife.inject(this, view);
        mFormValidator = new FormValidator(getActivity(), view);
        mContent.addTextChangedListener(this);
        mCharLimit.setText(String.valueOf(CHARACTER_LIMIT));

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCharLimit.setText(String.valueOf(CHARACTER_LIMIT - s.length()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @OnClick(R.id.new_entry_dialog_add)
    public void onAddEntryClick(){
     addNewEntry();
    }

    private void addNewEntry(){
        if(!mFormValidator.validateContent()){
            return;
        }
        ((NewEntryListener) getTargetFragment()).onNewEntryFinish(mContent.getText().toString());
        this.dismiss();
    }
}
