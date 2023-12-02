package  com.example.githubsearchrepo.repo

 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
 import android.widget.ImageView
 import android.widget.TextView
 import androidx.recyclerview.widget.RecyclerView
 import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.models.Owner
import com.squareup.picasso.Picasso

class ContributorsAdapter : RecyclerView.Adapter<ContributorsAdapter.MyViewHolder>() {

    private var items: MutableList<Owner> = mutableListOf()
    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_contributor, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    fun updateList(list: List<Owner>) {
        this.items = list as MutableList<Owner>
        notifyDataSetChanged()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(owner: Owner) {


            val textViewName = itemView.findViewById<TextView>(R.id.textViewName)
            val imageViewProfile = itemView.findViewById<ImageView>(R.id.imageViewProfile)

            textViewName.text = owner.login

            Picasso.get().load(owner.avatarUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageViewProfile)
        }

        override fun onClick(view: View?) {
            onItemClickListener.onItemClick(items[adapterPosition])
        }
    }

    interface OnItemClickListener {
        fun onItemClick(owner: Owner)
    }

}
