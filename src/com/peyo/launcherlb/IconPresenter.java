package com.peyo.launcherlb;

import android.content.pm.LauncherActivityInfo;
import android.content.res.Resources;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;

public class IconPresenter extends Presenter {
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        ImageCardView view = new ImageCardView(
                new ContextThemeWrapper(viewGroup.getContext(),
                    R.style.IconTheme));
        Resources res = view.getResources();
        view.setMainImageDimensions(res.getDimensionPixelSize(R.dimen.icon_width),
                res.getDimensionPixelSize(R.dimen.icon_width));
        return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Object item) {
		LauncherActivityInfo info = (LauncherActivityInfo) item;
		ImageCardView view = (ImageCardView) viewHolder.view;
		view.setTitleText(info.getLabel());
		view.setMainImage(info.getIcon(
				view.getResources().getDisplayMetrics().densityDpi));
	}

	@Override
	public void onUnbindViewHolder(ViewHolder viewHolder) {
		ImageCardView view = (ImageCardView) viewHolder.view;
		view.setTitleText(null);
		view.setMainImage(null);
	}
}
