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
import com.juniperphoton.myersplash.utils.ImageIO
import com.juniperphoton.myersplash.utils.Pasteur
import com.juniperphoton.myersplash.viewmodel.ClickData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

typealias OnClickPhotoListener = ((ClickData: ClickData) -> Unit)
typealias OnClickQuickDownloadListener = ((image: UnsplashImage) -> Unit)
typealias OnBindListener = ((View, Int) -> Unit)

class PhotoItemView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs
), View.OnClickListener, CoroutineScope by MainScope() {
    companion object {
        private const val TAG = "PhotoItemView"
    }

    @BindView(R.id.row_photo_iv)
    lateinit var simpleDraweeView: SimpleDraweeView

    @BindView(R.id.row_photo_root)
    lateinit var rootView: ViewGroup

    @BindView(R.id.row_photo_download_rl)
    lateinit var downloadRL: ViewGroup

    @BindView(R.id.row_photo_ripple_mask_rl)
    lateinit var rippleMaskRL: ViewGroup

    @BindView(R.id.row_photo_today_tag)
    lateinit var todayTag: View

    @BindView(R.id.row_photo_placeholder)
    lateinit var placeholder: View

    var onClickPhoto: OnClickPhotoListener? = null
    var onClickQuickDownload: OnClickQuickDownloadListener? = null
    var onBind: OnBindListener? = null

    private var unsplashImage: UnsplashImage? = null

    private var extractColorJob: Job? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)
        rippleMaskRL.setOnClickListener(this)
    }

    @OnClick(R.id.row_photo_download_rl)
    fun onClickQuickDownload() {
        unsplashImage?.let {
            onClickQuickDownload?.invoke(it)
        }
    }

    fun bind(image: UnsplashImage?, pos: Int) {
        if (image == null) return

        extractColorJob?.cancel()

        unsplashImage = image

        if (!image.isUnsplash) {
            tryUpdateThemeColor()
        }

        todayTag.setVisible(image.showTodayTag)
        rootView.background = ColorDrawable(image.themeColor.getDarker(0.7f))
        simpleDraweeView.setImageURI(image.listUrl)

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
        simpleDraweeView.getLocationOnScreen(location)

        val clickData = ClickData(
                RectF(
                        location[0].toFloat(),
                        location[1].toFloat(),
                        simpleDraweeView.width.toFloat(),
                        simpleDraweeView.height.toFloat()
                ), unsplashImage!!, rootView)

        onClickPhoto?.invoke(clickData)
    }

    private fun tryUpdateThemeColor() {
        extractColorJob = launch {
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