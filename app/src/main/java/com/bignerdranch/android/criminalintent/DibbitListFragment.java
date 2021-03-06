package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 08/10/2015.
 */
public class DibbitListFragment extends Fragment {

    private RecyclerView mDibbitRecyclerView;
    private DibbitAdapter mAdapter;
    private boolean mSubtitleVisible;
    private LinearLayout mLinearLayout;
    private Button mAddButton;
    private int mChangedPosition;

    private static final String TAG = "DibbitListFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    //private static final String POSITION_OF_DIBBIT = "position_of_dibbit";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dibbit_list, container, false);

        TabHost tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tab_list);
        tabSpec.setIndicator(getResources().getString(R.string.view_list_label));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("calendar");
        tabSpec.setContent(R.id.tab_calendar);
        tabSpec.setIndicator(getResources().getString(R.string.view_calendar_label));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("map");
        tabSpec.setContent(R.id.tab_map);
        tabSpec.setIndicator(getResources().getString(R.string.view_map_label));
        tabHost.addTab(tabSpec);

        mDibbitRecyclerView = (RecyclerView) view.findViewById(R.id.dibbit_recycler_view);
        mDibbitRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLinearLayout = (LinearLayout) view.findViewById(R.id.empty_dibbit_list);
        mAddButton = (Button) view.findViewById(R.id.add_dibbit_button);


        updateUI();

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dibbit_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_dibbit:
                addDibbit();
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateSubtitle() {
        DibbitLab dibbitLab = DibbitLab.get(getActivity());
        int dibbitCount = dibbitLab.getDibbits().size();
//        String subtitle = getString(R.string.subtitle_format, dibbitCount);
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, dibbitCount, dibbitCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        DibbitLab dibbitLab = DibbitLab.get(getActivity());
        List<Dibbit> dibbits = dibbitLab.getDibbits();

        if (mAdapter == null) {
            mAdapter = new DibbitAdapter(dibbits);
            mDibbitRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyItemChanged(mChangedPosition);
            mChangedPosition = RecyclerView.NO_POSITION; //Not sure
            /*if (getArguments() == null) {
                mAdapter.notifyDataSetChanged();
            }
            else {
                mAdapter.notifyItemChanged(getArguments().getInt(POSITION_OF_DIBBIT));
            }*/
        }
        if(dibbits.size()>0){
            mLinearLayout.setVisibility(View.GONE);
        }
        else {
            mLinearLayout.setVisibility(View.VISIBLE);
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDibbit();
                }
            });
        }
        updateSubtitle();
    }

    private class DibbitHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Dibbit mDibbit;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private int mLocation;

        public DibbitHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_dibbit_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_dibbit_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_dibbit_done_check_box);
        }

        public void bindDibbit(Dibbit dibbit, int location) {
            mDibbit = dibbit;
            mLocation = location;
            mTitleTextView.setText(mDibbit.getTitle());
            mDateTextView.setText(mDibbit.getDate().toString());
            mSolvedCheckBox.setChecked(mDibbit.isDone());
        }

        @Override
        public void onClick(View v) {
            Intent intent = DibbitPagerActivity.newIntent(getActivity(), mDibbit.getId());
            startActivity(intent);
            mChangedPosition = mLocation;
        }
    }
    public void addDibbit(){
        Dibbit dibbit = new Dibbit();
        DibbitLab.get(getActivity()).addDibbit(dibbit);
        Intent intent = DibbitPagerActivity.newIntent(getActivity(), dibbit.getId());
        startActivity(intent);
    }


    private class DibbitAdapter extends RecyclerView.Adapter<DibbitHolder> {

        private List<Dibbit> mDibbits;

        public DibbitAdapter(List<Dibbit> dibbits) {
            mDibbits = dibbits;
        }

        @Override
        public DibbitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_dibbit, parent, false);
            return new DibbitHolder(view);
        }

        @Override
        public void onBindViewHolder(DibbitHolder holder, int position) {
            Dibbit dibbit = mDibbits.get(position);
            holder.bindDibbit(dibbit, position);
        }

        @Override
        public int getItemCount() {
            return mDibbits.size();
        }
    }


}
