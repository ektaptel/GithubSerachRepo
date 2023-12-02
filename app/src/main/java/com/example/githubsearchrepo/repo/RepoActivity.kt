package  com.example.githubsearchrepo.repo

import android.content.Intent
 import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
 import android.view.TextureView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.Constants
import  com.example.githubsearchrepo.api.models.Item
import  com.example.githubsearchrepo.api.models.Owner
import  com.example.githubsearchrepo.owner.OwnerActivity
import  com.example.githubsearchrepo.webview.WebViewActivity
import com.squareup.picasso.Picasso

class RepoActivity : AppCompatActivity(), RepoView, ContributorsAdapter.OnItemClickListener {

    lateinit var textViewName : TextView
    lateinit var textViewLink :TextView
    lateinit var textViewDesc :TextView
    lateinit var textViewLinkClick :TextView
    lateinit var textViewFetching :TextView
    lateinit var  imageViewProfile :ImageView
    lateinit var recyclerViewContributors :RecyclerView


    private var repoPresenterImpl: RepoPresenterImpl? = null
    private var contributorsAdapter: ContributorsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        textViewName = findViewById(R.id.textViewName)
        textViewLink = findViewById(R.id.textViewLink)
        textViewDesc = findViewById(R.id.textViewDesc)
        textViewFetching = findViewById(R.id.textViewFetching)
        textViewLinkClick = findViewById(R.id.textViewLinkClick)
        imageViewProfile = findViewById(R.id.imageViewProfile)
        recyclerViewContributors = findViewById(R.id.recyclerViewContributors)


        repoPresenterImpl = RepoPresenterImpl(this)

        initRecyclerView()

        getIntentData()
    }

    private fun getIntentData() {
        val item: Item = intent.getParcelableExtra(Constants.REPO_DETAILS)!!

        textViewName.text = item.name
        textViewLink.text = item.htmlUrl
        textViewDesc.text = item.description

        Picasso.get().load(item.owner.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageViewProfile)

        repoPresenterImpl?.getContributors(item.owner.login.toString(), item.name.toString())

        textViewLinkClick.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(Constants.REPO_NAME, item.name)
            intent.putExtra(Constants.REPO_URL, item.htmlUrl)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        recyclerViewContributors.itemAnimator = DefaultItemAnimator()
        recyclerViewContributors.layoutManager = GridLayoutManager(this, 4)
        ViewCompat.setNestedScrollingEnabled(recyclerViewContributors, false)

        contributorsAdapter = ContributorsAdapter()
        contributorsAdapter!!.onItemClickListener = this

        recyclerViewContributors.adapter = contributorsAdapter
    }

    override fun attachContributors(list: List<Owner>) {
        contributorsAdapter?.updateList(list)
        hideMessage()
    }

    override fun changeMessage(message: String) {
        textViewFetching.text = message
        showMessage()
    }

    private fun hideMessage() {
        textViewFetching.visibility = View.GONE
        recyclerViewContributors.visibility = View.VISIBLE
    }

    private fun showMessage() {
        recyclerViewContributors.visibility = View.GONE
        textViewFetching.visibility = View.VISIBLE
    }

    override fun onItemClick(owner: Owner) {
        launchProfileActivity(owner)
    }

    private fun launchProfileActivity(owner: Owner) {
        val intent = Intent(this, OwnerActivity::class.java)
        intent.putExtra(Constants.OWNER_DETAILS, owner)
        startActivity(intent)
    }


}
