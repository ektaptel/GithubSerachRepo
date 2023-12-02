package  com.example.githubsearchrepo.owner

import android.content.Intent
 import android.os.Bundle

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.Constants
import  com.example.githubsearchrepo.api.models.Item
import  com.example.githubsearchrepo.api.models.Owner
import  com.example.githubsearchrepo.home.RepoAdapter
import  com.example.githubsearchrepo.repo.RepoActivity
import com.squareup.picasso.Picasso

class OwnerActivity : AppCompatActivity(), OwnerView, RepoAdapter.OnItemClickListener, RepoAdapter.OnLoadMoreListener {
    private lateinit var ownerPresenterImpl: OwnerPresenterImpl
    private lateinit var repoAdapter: RepoAdapter
    lateinit var textViewName : TextView
    lateinit var imageViewProfile :ImageView
    lateinit var recyclerViewRepos :RecyclerView
    lateinit var textViewFetching : TextView

      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
          textViewName = findViewById(R.id.textViewName)
          textViewFetching = findViewById(R.id.textViewFetching)
          recyclerViewRepos = findViewById(R.id.recyclerViewRepos);
          imageViewProfile = findViewById(R.id.imageViewProfile);



        ownerPresenterImpl = OwnerPresenterImpl(this)

        initRecyelerView()

        getIntentData()
    }

    private fun getIntentData() {
        val owner = intent.getParcelableExtra<Owner>(Constants.OWNER_DETAILS)
        owner!!.login.toString().also { textViewName.text = it }


        Picasso.get().load(owner.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageViewProfile)

        ownerPresenterImpl.getRepos(owner.login.toString())
    }

    private fun initRecyelerView() {
        recyclerViewRepos.layoutManager = LinearLayoutManager(this)
        recyclerViewRepos.itemAnimator = DefaultItemAnimator()
        ViewCompat.setNestedScrollingEnabled(recyclerViewRepos, false)

        repoAdapter = RepoAdapter(false, recyclerViewRepos)
        repoAdapter.onItemClickListener = this
        repoAdapter.onLoadMoreListener = this
        recyclerViewRepos.adapter = repoAdapter
    }

    override fun attachRepos(items: List<Item>) {
        repoAdapter.updateItems(items)

        textViewFetching.visibility = View.GONE
        recyclerViewRepos.visibility = View.VISIBLE
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, RepoActivity::class.java)
        intent.putExtra(Constants.REPO_DETAILS, item)
        startActivity(intent)
    }

    override fun onLoadMore() {

    }
}
