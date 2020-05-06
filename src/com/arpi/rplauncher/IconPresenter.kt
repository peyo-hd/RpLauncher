package com.arpi.rplauncher

import android.view.ViewGroup
import android.widget.ImageView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter

class IconPresenter : Presenter() {
    override fun onCreateViewHolder(viewGroup: ViewGroup): Presenter.ViewHolder {
        val view = ImageCardView(viewGroup.context)
        val res = view.resources
        view.setMainImageDimensions(res.getDimensionPixelSize(R.dimen.icon_width),
                res.getDimensionPixelSize(R.dimen.icon_height))
        return Presenter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val info = item as AppInfo
        val view = viewHolder.view as ImageCardView
        val banner = info.banner
        if (banner == null) {
            view.mainImage = info.icon
            view.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE)
        } else {
            view.mainImage = banner
            view.setMainImageScaleType(ImageView.ScaleType.FIT_XY)
        }
        view.titleText = info.label
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val view = viewHolder.view as ImageCardView
        view.titleText = null
        view.mainImage = null
    }
}