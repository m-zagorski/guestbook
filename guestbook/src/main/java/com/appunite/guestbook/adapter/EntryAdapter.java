package com.appunite.guestbook.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunite.guestbook.R;
import com.appunite.guestbook.dagger.ForApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryAdapter extends BaseAdapter {
    @Inject
    Picasso mPicasso;
    @Inject
    LayoutInflater mInflater;


    //TODO Change to use actual entry class
    private ArrayList<String> mEntries;
    private int mImageSize;
    private String mAuthorDateFormat;

    private String firstContent = "FirstContent";
    private String temp = "Lorem ipsum lorem impsum lorem ipsum lorem ipsum lorem " +
            "ipsum Lorem ipsum lorem impsum lorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem " +
            "Lorem ipsum lorem impsum lorem ipsum lorem ipsum lorem " +
            "ipsum Lorem ipsum lorem impsum lorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem " +
            "Lorem ipsum lorem impsum lorem ipsum lorem ipsum lorem ";

    @Inject
    public EntryAdapter(@ForApplication Context context){
        mEntries = new ArrayList<String>();
        Resources rs = context.getResources();
        mImageSize = rs.getDimensionPixelSize(R.dimen.entry_photo_size);
        mAuthorDateFormat = rs.getString(R.string.entry_author_and_date);
    }

    public void swapData(List<String> entries){
//        if(mEntries != null){
//            mEntries.addAll(entries);
//        } else {
//            mEntries = new ArrayList<String>(entries);
//        }
        for(int i=0; i<50; i++){
            if(i==0){ mEntries.add(firstContent); } else {
                mEntries.add(temp);}
        }

        notifyDataSetChanged();
    }

    public void addData(String entry){
        mEntries.add(entry);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if(mEntries != null){
            return mEntries.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public static class ViewHolder {
        @InjectView(R.id.entry_photo)
        ImageView mPhoto;
        @InjectView(R.id.content)
        TextView mContent;
        @InjectView(R.id.entry_author)
        TextView mAuthor;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(view == null){
            view = checkNotNull(mInflater.inflate(R.layout.entry_layout, parent, false));
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
            ButterKnife.inject(viewHolder, view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.mContent.setText(getItem(position));
        viewHolder.mAuthor.setText(String.format(mAuthorDateFormat, "AUTHOR", "05.03.2014"));
        mPicasso.load(R.drawable.ava)
                .resize(mImageSize, mImageSize)
                .into(viewHolder.mPhoto);

        return view;
    }
}
