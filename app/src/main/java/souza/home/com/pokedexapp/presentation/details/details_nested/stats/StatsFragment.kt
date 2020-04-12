package souza.home.com.pokedexapp.presentation.details.details_nested.stats

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.data.pokedex.remote.model.response.PropertyResponse
import souza.home.com.pokedexapp.domain.model.PokeProperty
import souza.home.com.pokedexapp.presentation.details.details_nested.NestedViewModelFactory


class StatsFragment(var pokemon: Int) : Fragment() {

    private lateinit var viewModel: PokeStatsViewModel
    private lateinit var tvHp : TextView
    private lateinit var pbHp : ProgressBar
    private lateinit var tvAttack : TextView
    private lateinit var pbAttack : ProgressBar
    private lateinit var tvDeffense : TextView
    private lateinit var tvHeight : TextView
    private lateinit var tvWeight : TextView
    private lateinit var pbDeffense : ProgressBar
    private lateinit var tvSpecialAttack: TextView
    private lateinit var pbSpecialAttack : ProgressBar
    private lateinit var tvSpecialDeffense : TextView
    private lateinit var pbSpecialDeffense : ProgressBar
    private lateinit var tvSpeed : TextView
    private lateinit var pbSpeed : ProgressBar
    private var menuVisible: Boolean = false
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_poke_stats, container, false)

        viewModel = ViewModelProviders.of(this,
            NestedViewModelFactory(
                pokemon,
                activity!!.application
            )
        )
            .get(PokeStatsViewModel::class.java)

        pbHp = view.findViewById(R.id.progress_bar_hp)
        pbAttack = view.findViewById(R.id.progress_bar_attack)
        pbDeffense = view.findViewById(R.id.progress_bar_deffense)
        pbSpecialAttack = view.findViewById(R.id.progress_bar_special_attack)
        pbSpecialDeffense = view.findViewById(R.id.progress_bar_special_deffense)
        pbSpeed = view.findViewById(R.id.progress_bar_speed)
        tvAttack = view.findViewById(R.id.tv_poke_attack)
        tvHp = view.findViewById(R.id.tv_poke_hp)
        tvDeffense = view.findViewById(R.id.tv_poke_deffense)
        tvSpecialAttack = view.findViewById(R.id.tv_poke_special_attack)
        tvSpecialDeffense = view.findViewById(R.id.tv_poke_special_deffense)
        tvSpeed = view.findViewById(R.id.tv_poke_speed)
        tvWeight = view.findViewById(R.id.tv_detail_weight)
        tvHeight = view.findViewById(R.id.tv_detail_height)

        initObservers()

        return view
    }

    private fun initObservers(){
        viewModel.apply {
           this.updatePropertiesOnViewLiveData()?.observe(this@StatsFragment, Observer {
                if(it!=null){
                    initStats(it)
                }
            })
        }
    }

    private fun initStats(item: PokeProperty?){

        item?.stats?.get(5)?.base_stat.let { it?.let { it1 -> Integer.valueOf(it1) } }?.let { animateStats(it, tvHp) }
        pbHp.progress = item?.stats?.get(5)?.base_stat?.let { Integer.parseInt(it) }!!

        item.stats?.get(4)?.base_stat?.let { Integer.valueOf(it) }?.let { animateStats(it, tvAttack) }
        pbAttack.progress = item.stats?.get(4)?.base_stat?.let { Integer.parseInt(it) }!!

        item.stats?.get(3)?.base_stat?.let { Integer.valueOf(it) }?.let { animateStats(it, tvDeffense) }
        pbDeffense.progress = item.stats?.get(3)?.base_stat?.let { Integer.parseInt(it) }!!

        item.stats?.get(2)?.base_stat?.let { Integer.valueOf(it) }?.let { animateStats(it, tvSpecialAttack) }
        pbSpecialAttack.progress = item.stats?.get(2)?.base_stat?.let { Integer.parseInt(it) }!!

        item.stats?.get(1)?.base_stat?.let { Integer.valueOf(it) }?.let { animateStats(it, tvSpecialDeffense) }
        pbSpecialDeffense.progress = item.stats?.get(1)?.base_stat?.let { Integer.parseInt(it) }!!

        item.stats?.get(0)?.base_stat?.let { Integer.valueOf(it) }?.let { animateStats(it, tvSpeed) }
        pbSpeed.progress = item.stats?.get(0)?.base_stat?.let { Integer.parseInt(it) }!!

        animateStats(item.weight?.let { Integer.valueOf(it) }, tvWeight)
        animateStats(item.height?.let { Integer.valueOf(it) }, tvHeight)
    }

    private fun animateStats(item: Int?, tv: TextView){
        val animator = ValueAnimator()
        animator.setObjectValues(0, item)// here you set the range, from 0 to "count" value
        animator.addUpdateListener {
                animation -> tv.text = animation.animatedValue.toString()
        }
        animator.duration = 600 // here you set the duration of the anim
        animator.start()
    }

/*    //Get the moment when the fragment is visible
    override fun setMenuVisibility(visible: Boolean){
        super.setMenuVisibility(visible)
        if (visible) {
        //Used to verify when the fragment is visible/focused on viewpager
        } else {
            menuVisible = false
        }
    }*/


}
