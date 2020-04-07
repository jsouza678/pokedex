package souza.home.com.pokedexapp.presenter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.poke_item_view.view.*
import souza.home.com.pokedexapp.R
import souza.home.com.pokedexapp.network.model.main_model.Pokemon
import souza.home.com.pokedexapp.extensions.loadUrl


class PokesAdapter(private val pokes: MutableList<Pokemon>?, private val context: Context) : RecyclerView.Adapter<PokesAdapter.ViewHolder>() {

    var onItemClick: ((Pokemon) -> Unit)? = null
    private val imageResourceUrl = "https://pokeres.bastionbot.org/images/pokemon/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.poke_item_view, parent, false)
        return ViewHolder(view)
    }

    fun submitList(newData: MutableList<Pokemon>) {
        if (pokes!!.isNotEmpty()) {
            pokes.clear()
        }
        pokes.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pokes!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemBind(pokes!![position])

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokeName: TextView = itemView.tv_name_poke
        private val pokeImage: ImageView = itemView.iv_poke_sprite
        private val pokeId: TextView = itemView.tv_id_poke
        private var formatedNumber: String = ""
        private var pokemonId : String = ""

        fun itemBind(pokes: Pokemon){
            pokeName.text = pokes.name
            pokemonId = pokes.url.substringAfter("n/").substringBefore('/')
            formatedNumber= "%03d".format(Integer.parseInt(pokemonId))
            pokeId.text = context.resources.getString(R.string.placeholder_tv_id, formatedNumber)
            pokeImage.loadUrl("$imageResourceUrl$pokemonId.png")
        }

    init{
        itemView.setOnClickListener{
            onItemClick?.invoke(pokes!![adapterPosition])
            }
        }

    }

}