package com.cs160.joleary.catnip;

/**
 * Created by aaron on 3/2/16.
 */

import java.util.ArrayList;
public class SimpleRow {

    ArrayList<SimplePage> mPagesRow = new ArrayList<SimplePage>();

    public void addPages(SimplePage page) {
        mPagesRow.add(page);
    }

    public SimplePage getPages(int index) {
        return mPagesRow.get(index);
    }

    public int size(){
        return mPagesRow.size();
    }
}