package  com.example.githubsearchrepo.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.models.Item
import com.squareup.picasso.Picasso

class RepoAdapter(val showPic: Boolean, recyclerView: RecyclerView) : RecyclerView.Adapter<RepoAdapter.MyViewHolder>() {

    private var items: MutableList<Item> = mutableListOf()

    private var loading: Boolean = false


    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onLoadMoreListener: OnLoadMoreListener

    init {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    if (!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > items.size - 5) {
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    fun updateItems(items: List<Item>) {
        items.forEach { this.items.add(it) }

        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setLoaded() {
        loading = false
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_item_repo_details, viewGroup, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindData(items[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var textViewName :TextView
        lateinit var textViewFullName :TextView
        lateinit var textViewWatchCommitCount :TextView
        lateinit var imageViewProfile :ImageView


        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(item: Item) {
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewFullName =itemView .findViewById(R.id.textViewFullName)
            textViewWatchCommitCount = itemView.findViewById(R.id.textViewWatchCommitCount)
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile)

            textViewName.text = item.name
          textViewFullName.text = item.fullName
           textViewWatchCommitCount.text = "Watcher Count ${item.watchersCount}, Forks Count ${item.forksCount}"

            if (showPic) {
                Picasso.get().load(item.owner.avatarUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageViewProfile)
            } else {
               imageViewProfile.visibility = View.GONE
            }
        }

        override fun onClick(view: View?) {
            onItemClickListener.onItemClick(items[adapterPosition])
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

}
