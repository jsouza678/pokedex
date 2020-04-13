package souza.home.com.pokedexapp.presentation.details

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import souza.home.com.extensions.observeOnce
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.data.pokedex.PropertiesPokedexStatus
import souza.home.com.pokedexapp.data.pokedex.VarietiesPokedexStatus
import souza.home.com.pokedexapp.domain.model.PokeProperty
import souza.home.com.pokedexapp.domain.model.PokeVariety
import souza.home.com.pokedexapp.presentation.details.viewpager.SectionsPagerAdapter
import souza.home.com.pokedexapp.presentation.view_utils.DynamicHeightViewPager
import souza.home.com.pokedexapp.utils.ColorFormat
import souza.home.com.pokedexapp.utils.Constants.Companion.FORMAT_ID_POKE_DISPLAY
import souza.home.com.pokedexapp.utils.Constants.Companion.LIMIT_NORMAL_POKES
import souza.home.com.pokedexapp.utils.Constants.Companion.OFFSCREEN_DEFAULT_VIEW_PAGER
import souza.home.com.pokedexapp.utils.Constants.Companion.TIME_BACKGROUND_ANIMATION
import souza.home.com.pokedexapp.utils.cropPokeUrl

class DetailsFragment(private var pokeId: Int, private var pokeName: String) : Fragment(){

    private lateinit var viewModel: DetailsViewModel
    private lateinit var tvPokeName: TextView
    private lateinit var tvPokeId: TextView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var galleryViewPager: DetailsGalleryAdapter
    private lateinit var gallery : ViewPager
    private lateinit var mImages : MutableList<String>
    private var count = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details_pokedex, container, false)
        bindViews(view)
        val viewPager: DynamicHeightViewPager = view.findViewById(R.id.fragment_container_details)
        val tabs: TabLayout = view.findViewById(R.id.tab_layout_details_fragments)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_details_fragment)
        mImages = ArrayList()

        setToolbarBackButton(toolbar)
        setPokeAndIdText()
        initViewModel()
        initObserverStatus(viewPager, tabs, view)

        return view
    }

    private fun bindViews(view: View){
        tvPokeName = view.findViewById(R.id.text_view_poke_name_detail)
        tvPokeId = view.findViewById(R.id.text_view_poke_id_detail)
        constraintLayout = view.findViewById(R.id.layout_details)
        gallery = view.findViewById(R.id.image_slider_detail_fragment)
    }

    private fun setToolbarBackButton(toolbar: Toolbar){
        toolbar.setNavigationOnClickListener(View.OnClickListener { activity?.onBackPressed() })
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this, activity?.application?.let {
            DetailsViewModelFactory(pokeId, it)
        }).get(DetailsViewModel::class.java)
    }

    private fun setPokeAndIdText(){
        tvPokeName.text = pokeName.capitalize()
        val textId = FORMAT_ID_POKE_DISPLAY.format(pokeId)
        tvPokeId.text = context?.resources?.getString(R.string.text_view_placeholder_hash, textId)
    }

    private fun initObserverStatus(viewPager: ViewPager, tabs: TabLayout, view: View){
        viewModel.apply {
            this.checkRequestVariationsStatus().observe(viewLifecycleOwner, Observer {
                if(pokeId> LIMIT_NORMAL_POKES){showDataEvolutionPoke(viewPager, tabs)
                }else{ bindRequestVarietiesStatus(it, viewPager, tabs) }
            })
            this.checkRequestPropertiesStatus().observe(viewLifecycleOwner, Observer {
                if(pokeId> LIMIT_NORMAL_POKES){showDataEvolutionPoke(viewPager, tabs)
                }else{ bindRequestPropertiesStatus(it, viewPager, tabs) }
            })
        }
    }

    private fun bindRequestVarietiesStatus(varietiesPokedexStatus: VarietiesPokedexStatus,
                                           viewPager: ViewPager, tabs: TabLayout){
        when(varietiesPokedexStatus){
            VarietiesPokedexStatus.LOADING -> {}
            VarietiesPokedexStatus.DONE ->  initObserverData(viewModel, viewPager, tabs)
            VarietiesPokedexStatus.EMPTY ->  showError()
            // In case of ERROR. Runs normally, because it has cache on some pokes.
            else -> {
                initObserverData(viewModel, viewPager, tabs)
                showError()
            }
        }
    }

    private fun bindRequestPropertiesStatus(propertiesPokedexStatus: PropertiesPokedexStatus,
                                            viewPager: ViewPager, tabs: TabLayout){
        when(propertiesPokedexStatus){
            PropertiesPokedexStatus.LOADING -> {}
            PropertiesPokedexStatus.DONE ->  initObserverData(viewModel, viewPager, tabs)
            PropertiesPokedexStatus.EMPTY ->  showError()
            // In case of ERROR. Runs normally, because it has cache on some pokes.
            else -> {
                initObserverData(viewModel, viewPager, tabs)
                showError()
            }
        }
    }

    private fun initObserverData(viewModel: DetailsViewModel, viewPager: ViewPager, tabs: TabLayout){
        viewModel.apply {
            this.updateVariationsOnViewLiveData()?.observeOnce(viewLifecycleOwner, Observer { pokeVariety->
                pokeVariety?.let { pokeVariety -> showDataNormalPoke(pokeVariety, viewPager, tabs)}
            })
            this.updatePropertiesOnViewLiveData()?.observeOnce(viewLifecycleOwner, Observer {
                loadImages(it)
            })
        }
    }

    private fun loadImages(it: PokeProperty){
        val imagesList = addSpritesToList(it)
        addImagesToList(imagesList)
        view?.let { it1 -> initGalleryViewPager(mImages, it1) }
    }

    private fun showError(){
        view?.let { Snackbar.make(it, getString(R.string.no_conectivity), 800).show() }
    }

    private fun setViewPager(viewPager: ViewPager, sectionsPagerAdapter: SectionsPagerAdapter, tabs: TabLayout){
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun showDataNormalPoke(it: PokeVariety, viewPager: ViewPager, tabs: TabLayout){
        val backgroundColor = ColorFormat.setColor(it.color?.name, pokeId)
        animateBackground(backgroundColor)

        val sectionsPagerAdapter =
            fragmentManager?.let { fm -> SectionsPagerAdapter(fm, pokeId,
                Integer.parseInt(cropPokeUrl(it.evolution_chain?.url!!))) // Here the !! is accepted because // the pokemon has a evolution chain url.
            }
        sectionsPagerAdapter?.let { item -> setViewPager(viewPager, item, tabs) }
        viewPager.offscreenPageLimit = OFFSCREEN_DEFAULT_VIEW_PAGER
    }

    private fun showDataEvolutionPoke(viewPager: ViewPager, tabs: TabLayout){
        val sectionsPagerAdapterEvolution = fragmentManager?.let { fm ->
            SectionsPagerAdapter(fm, pokeId, 0) }
        sectionsPagerAdapterEvolution?.let { item -> setViewPager(viewPager, item, tabs) }
        val backgroundColor = ColorFormat.setColor(getString(R.string.black_color_name), pokeId)
        animateBackground(backgroundColor)
    }

    private fun addSpritesToList(listResult: PokeProperty) : MutableList<String>{
        val auxList = mutableListOf<String>()

        listResult.sprites?.front_default?.let { auxList.add(it) }
        listResult.sprites?.back_default?.let { auxList.add(it) }
        listResult.sprites?.front_female?.let { auxList.add(it) }
        listResult.sprites?.back_female?.let { auxList.add(it) }
        listResult.sprites?.front_shiny?.let { auxList.add(it) }
        listResult.sprites?.back_shiny?.let { auxList.add(it) }
        listResult.sprites?.front_shiny_female?.let { auxList.add(it) }
        listResult.sprites?.back_shiny_female?.let { auxList.add(it) }

        return auxList
    }

    private fun addImagesToList(it: MutableList<String>){
        mImages.addAll(it)
    }

    private fun animateBackground(colorV: Int){
        val backgroundColorAnimator = ObjectAnimator.ofObject(
            constraintLayout,
            "backgroundColor",
            ArgbEvaluator(),
            context?.let { ContextCompat.getColor(it, R.color.blue_poke) },
            context?.let { ContextCompat.getColor(it, colorV) })

        backgroundColorAnimator.duration = TIME_BACKGROUND_ANIMATION
        backgroundColorAnimator.start()
    }

    private fun initGalleryViewPager(travelGallery: MutableList<String>, view: View) {
        galleryViewPager = DetailsGalleryAdapter(view.context, travelGallery)
        gallery.adapter = galleryViewPager
    }
}