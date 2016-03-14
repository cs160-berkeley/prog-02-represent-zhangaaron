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
import java.util.List;

/**
 * Created by aaron on 3/2/16.
 */
// Credits: https://gist.github.com/gabrielemariotti/ca2d0a9f79b902b19a65
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    private int zip;
    private double obama;
    private double romney;
    private List<String> repNames;
    private List<String> repIds;

    public SampleGridPagerAdapter(Context context, FragmentManager fm, double obama, double romney, List<String> repNames, List<String> repIDs) {
        super(fm);
        mContext = context;
        this.obama = obama;
        this.romney = romney;
        this.repNames = repNames;
        this.repIds = repIDs;
        Log.wtf("D", "IN ADAPTERLENGTH IS " + repIds.size());
        initPages();

    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();

        row1.addPages(new SimplePage("", "Obama:" + obama + " Romeny" + romney, R.drawable.obama_romney, R.drawable.obama_romney));
        mPages.add(row1);

        for (int i = 0; i < repNames.size(); i++) {
            SimpleRow row = new SimpleRow();
            row.addPages(new SimplePage(repNames.get(i), repNames.get(i), R.drawable.fred_160, R.drawable.fred_160));
            mPages.add(row);
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        final int _row = row;
        final int _col = col;
        Log.d("D", "Request for row: " + row + " col: " + col);
        if (col != 0) {
            return ActionButton.create(R.mipmap.ic_launcher, R.string.go_to_phone, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go_to_phone = new Intent(mContext, WatchToPhoneService.class);
                    Log.wtf("D", "Row col is:" + _row + ", " + _col);
                    Log.wtf("D", "IN ADAPTER METHOD LENGTH IS " + repIds.size());
                    go_to_phone.putExtra("rep_name", repIds.get(_row - 1));
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