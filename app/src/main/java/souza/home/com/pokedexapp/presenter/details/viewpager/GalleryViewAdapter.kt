package souza.home.com.pokedexapp.presenter.details.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.image_view_pager_item.view.*
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.extensions.loadUrl


class GalleryViewPagerAdapter(private val context: Context, private val gallery: MutableList<String>) : PagerAdapter() {

    private companion object {
        const val EMPTY_GALLERY = 0
        const val FIRST_POSITION = 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return gallery.size ?: EMPTY_GALLERY
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_view_pager_item, null)
        val viewPager = container as ViewPager
        val imageView: ImageView = view.picture_image_view_pager_item

        imageView.loadUrl(gallery[position])
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