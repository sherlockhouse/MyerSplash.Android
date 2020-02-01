package com.juniperphoton.myersplash.widget.item

import android.content.Context
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.drawee.view.SimpleDraweeView
import com.juniperphoton.myersplash.R
import com.juniperphoton.myersplash.extension.extractThemeColor
import com.juniperphoton.myersplash.extension.getDarker
import com.juniperphoton.myersplash.extension.setVisible
import com.juniperphoton.myersplash.extension.toHexString
import com.juniperphoton.myersplash.model.UnsplashImage
import com.juniperphoton.myersplash.model.getDisplayRatio
import com.juniperphoton.myersplash.utils.ImageIO
import com.juniperphoton.myersplash.utils.Pasteur
import com.juniperphoton.myersplash.viewmodel.ClickData
import kotlinx.coroutines.*

typealias OnClickPhotoListener = ((ClickData: ClickData) -> Unit)
typealias OnClickQuickDownloadListener = ((image: UnsplashImage) -> Unit)
typealias OnBindListener = ((View, Int) -> Unit)

class PhotoItemView(context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs), View.OnClickListener {
    companion object {
        private const val TAG = "PhotoItemView"
    }

    @BindView(R.id.row_photo_iv)
    lateinit var photoView: SimpleDraweeView

    @BindView(R.id.row_photo_root)
    lateinit var rootView: ViewGroup

    @BindView(R.id.row_photo_today_tag)
    lateinit var todayTag: View

    @BindView(R.id.card_view)
    lateinit var cardView: View

    var onClickPhoto: OnClickPhotoListener? = null
    var onClickQuickDownload: OnClickQuickDownloadListener? = null
    var onBind: OnBindListener? = null

    private var unsplashImage: UnsplashImage? = null

    private var extractColorJob: Job? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            ButterKnife.bind(this, this)
            cardView.setOnClickListener(this)
        }
    }

    @OnClick(R.id.download_btn)
    fun onClickQuickDownload() {
        unsplashImage?.let {
            onClickQuickDownload?.invoke(it)
        }
    }

    fun bind(image: UnsplashImage?, pos: Int) {
        if (image == null) return

        extractColorJob?.cancel()

        unsplashImage = image

        val lp = cardView.layoutParams as LayoutParams
        lp.dimensionRatio = image.getDisplayRatio(context)
        cardView.layoutParams = lp

        if (!image.isUnsplash) {
            tryUpdateThemeColor()
        }

        todayTag.setVisible(image.showTodayTag)
        photoView.setImageURI(image.listUrl)
        photoView.hierarchy.setPlaceholderImage(ColorDrawable(image.themeColor.getDarker(0.7f)))

        onBind?.invoke(rootView, pos)
    }

    override fun onClick(v: View?) {
        val url = unsplashImage?.listUrl ?: return

        val memoryCached = ImageIO.isInMemoryCache(url)

        if (memoryCached) {
            invokeOnClick()
            return
        }

        val diskCached = ImageIO.isInDiskCache(url)

        if (!diskCached) {
            Pasteur.warn(TAG) {
                "not cached, return"
            }
            return
        }

        Pasteur.info(TAG) {
            "only has disk cache"
        }

        invokeOnClick()
    }

    private fun invokeOnClick() {
        val location = IntArray(2)
        photoView.getLocationOnScreen(location)

        val clickData = ClickData(
                RectF(
                        location[0].toFloat(),
                        location[1].toFloat(),
                        photoView.width.toFloat(),
                        photoView.height.toFloat()
                ), unsplashImage!!, rootView)

        onClickPhoto?.invoke(clickData)
    }

    private fun tryUpdateThemeColor() {
        extractColorJob = GlobalScope.launch {
            try {
                val color = unsplashImage?.extractThemeColor() ?: Int.MIN_VALUE
                if (color != Int.MIN_VALUE) {
                    unsplashImage?.color = color.toHexString()
                }
            } catch (e: Exception) {
                // ignore cancellation
            }
        }
    }
}