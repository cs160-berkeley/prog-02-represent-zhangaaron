package com.cs160.joleary.catnip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by aaron on 3/2/16.
 */
// Credits: https://gist.github.com/gabrielemariotti/ca2d0a9f79b902b19a65
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    private int zip;

    public SampleGridPagerAdapter(Context context, FragmentManager fm, int zip) {
        super(fm);
        mContext = context;
        this.zip = zip;
        initPages();
        Log.d("D", "ZIP IS ---- " + zip);
    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        Log.d("D", "ZIP IS" + zip);
        if (zip == 94709) {
            row1.addPages(new SimplePage("2012 Vote View", "Obama: 60 Romney 54", R.drawable.obama_romney, R.drawable.obama_romney));
        } else {
            row1.addPages(new SimplePage("2012 Vote View", "Obama: 47 Romney: 51", R.drawable.obama_romney2, R.drawable.obama_romney2));
        }

        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("Bernie Sanders, I", "Bernie Sanders, I", R.drawable.fred_160, R.drawable.fred_160));

        SimpleRow row3 = new SimpleRow();
        row3.addPages(new SimplePage("Hillary Clinton, D", "Hillary Clinton, D", R.drawable.fred_160, R.drawable.fred_160));

        SimpleRow row4 = new SimpleRow();
        row4.addPages(new SimplePage("Dolan Trump, R", "Dolan Trump, R", R.drawable.fred_160, R.drawable.fred_160));

        mPages.add(row1);
        mPages.add(row2);
        mPages.add(row3);
        mPages.add(row4);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        final int _row = row;
        Log.d("D", "Request for row: " + row + " col: " + col);
        if (col != 0) {
            return ActionButton.create(R.mipmap.ic_launcher, R.string.go_to_phone, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go_to_phone = new Intent(mContext, WatchToPhoneService.class);
                    go_to_phone.putExtra("rep_name", mPages.get(_row).getPages(0).mTitle);
                    mContext.startService(go_to_phone);

                }
            });
        }
        SimplePage page = ((SimpleRow)mPages.get(row)).getPages(col);
        CardFragment fragment = CardFragment.create(page.mTitle, page.mText, page.mIconId);
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        SimplePage page = ((SimpleRow)mPages.get(row)).getPages(0);
        Drawable d = mContext.getResources().getDrawable(page.mBackgroundId, mContext.getTheme());
        return d;
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return 2;
    } //TODO: Don't hardcode this
}