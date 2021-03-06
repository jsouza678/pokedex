package com.souza.pokedetail.presentation.pokedetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.souza.extensions.loadImageUrl
import com.souza.pokedetail.R
import com.souza.pokedetail.utils.Constants.Companion.ABSOLUTE_ZERO
import kotlinx.android.synthetic.main.image_view_pager_item.view.picture_image_view_pager_item

class PokeDetailsGalleryAdapter(
    private val gallery: MutableList<String>
) : PagerAdapter() {

    private companion object {
        const val FIRST_POSITION = ABSOLUTE_ZERO
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return gallery.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_view_pager_item, container, false)
        val viewPager = container as ViewPager
        val imageView: ImageView = view.picture_image_view_pager_item

        imageView.loadImageUrl(gallery[position])
        viewPager.addView(view,
            FIRST_POSITION
        )
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}
