package com.peyo.launcherlb;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.widget.ImageView;

public class IconPresenter extends Presenter {
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        ImageCardView view = new ImageCardView(viewGroup.getContext());
        Resources res = view.getResources();
        view.setMainImageDimensions(res.getDimensionPixelSize(R.dimen.icon_width),
                res.getDimensionPixelSize(R.dimen.icon_height));
        return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Object item) {
		AppInfo info = (AppInfo) item;
		ImageCardView view = (ImageCardView) viewHolder.view;
		Drawable banner = info.getBanner();
		if (banner == null) {
			view.setMainImage(info.getIcon());
			view.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
		} else {
			view.setMainImage(banner);
			view.setMainImageScaleType(ImageView.ScaleType.FIT_XY);
		}
        view.setTitleText(info.getLabel());
	}

	@Override
	public void onUnbindViewHolder(ViewHolder viewHolder) {
		ImageCardView view = (ImageCardView) viewHolder.view;
		view.setTitleText(null);
		view.setMainImage(null);
	}
}
