package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;


public class DibbitListActivity extends SingleFragmentActivity{

    protected Fragment createFragment() {
        return new DibbitListFragment();
    }

}