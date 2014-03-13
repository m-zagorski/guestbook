package com.appunite.guestbook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.appunite.guestbook.helpers.FormValidator;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewEntryFragment extends BaseFragment implements TextWatcher {

    private static final int CHARACTER_LIMIT = 200;

    @InjectView(R.id.new_entry__char_limit)
    TextView mCharLimit;
    @InjectView(R.id.content)
    EditText mContent;

    private FormValidator mFormValidator;

    public static NewEntryFragment newInstance(){
        return new NewEntryFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCharLimit.setText(String.valueOf(CHARACTER_LIMIT));
        mContent.addTextChangedListener(this);
        mFormValidator = new FormValidator(getActivity(), view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.new_entry_fragment, container, false));
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

    @OnClick(R.id.new_entry_add_button)
    public void onNewEntryClick(){
        addNewEntry();
    }

    private void addNewEntry(){
        if (!mFormValidator.validateContent()) {
            return;
        }
        //TODO Add entry to database, load entries fragment.
    }
}
