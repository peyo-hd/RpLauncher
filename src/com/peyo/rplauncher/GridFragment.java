package com.peyo.rplauncher;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter.ViewHolder;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.VerticalGridPresenter;

public class GridFragment extends VerticalGridFragment {
    private static final int NUM_COLUMNS = 5;
    private ArrayObjectAdapter mAdapter = new ArrayObjectAdapter(
            new IconPresenter());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter(mAdapter);
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(ViewHolder viewHolder, Object item,
                                      RowPresenter.ViewHolder rowViewHolder, Row row) {
                AppInfo info = (AppInfo) item;
                Intent intent = new Intent();
                intent.setComponent(info.getComponentName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                viewHolder.view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.BLACK);
    }

    public void updateApps(ArrayList<AppInfo> apps) {
        for (int i = 0; i < apps.size(); i++) {
            mAdapter.add(apps.get(i));
        }
    }
}
