package  com.example.githubsearchrepo.home

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearchrepo.GithubApplication
import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.Constants
import  com.example.githubsearchrepo.api.models.Item
import  com.example.githubsearchrepo.repo.RepoActivity
import  com.example.githubsearchrepo.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog


class HomeActivity : AppCompatActivity(), HomeView, View.OnClickListener,
    RepoAdapter.OnItemClickListener, RepoAdapter.OnLoadMoreListener {

    private lateinit var homePresenterImpl: HomePresenterImpl
    private lateinit var repoAdapter: RepoAdapter

    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetView: View
    lateinit var imageViewFilters: ImageView
    lateinit var imageViewSearch: ImageView
    lateinit var editTextSearch: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var textViewStars: TextView
    lateinit var textViewMessage: TextView
    lateinit var textViewForks: TextView
    lateinit var textViewApply: TextView
    lateinit var textViewAscending: TextView
    lateinit var textViewClear: TextView
    lateinit var textViewDescending: TextView
    lateinit var textViewUpdated: TextView

    private var orderBy: String = ""
    private var sortBy: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        imageViewSearch = findViewById<ImageView>(R.id.imageViewSearch)
        imageViewFilters = findViewById<ImageView>(R.id.imageViewFilters)
        editTextSearch = findViewById(R.id.editTextSearch)
        recyclerView = findViewById(R.id.recyclerView)
        textViewMessage = findViewById(R.id.textViewMessage)


        imageViewSearch.setOnClickListener(this)
        imageViewFilters.setOnClickListener(this)

        homePresenterImpl = HomePresenterImpl(this)

        initRecyclerView()
        initBottomSheet()

        editTextSearch.setText("android")
         imageViewSearch.performClick()
    }

    private fun initRecyclerView() {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(this)

        repoAdapter = RepoAdapter(true, recyclerView)
        repoAdapter.onItemClickListener = this
        repoAdapter.onLoadMoreListener = this

        recyclerView.adapter = repoAdapter
    }

    private fun initBottomSheet() {
        bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_sheet_filter, null)

        textViewStars = bottomSheetView.findViewById(R.id.textViewStars)
        textViewForks= bottomSheetView.findViewById(R.id.textViewForks)
        textViewUpdated= bottomSheetView.findViewById(R.id.textViewUpdated)
        textViewAscending= bottomSheetView.findViewById(R.id.textViewAscending)
        textViewDescending= bottomSheetView.findViewById(R.id.textViewDescending)
        textViewApply= bottomSheetView.findViewById(R.id.textViewApply)
        textViewClear= bottomSheetView.findViewById(R.id.textViewClear)

        textViewStars.setOnClickListener(this)
        textViewForks.setOnClickListener(this)
        textViewUpdated.setOnClickListener(this)
        textViewAscending.setOnClickListener(this)
        textViewDescending.setOnClickListener(this)
        textViewApply.setOnClickListener(this)
        textViewClear.setOnClickListener(this)

        dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetView)
        dialog.setOnCancelListener {
            homePresenterImpl.onDialogCancel()
        }
    }

    private fun clearSortFilters() {
        textViewStars.isSelected = false
        textViewForks.isSelected = false
        textViewUpdated.isSelected = false
    }

    private fun clearOrderByFilters() {
        textViewAscending.isSelected = false
        textViewDescending.isSelected = false
    }

    private fun manageSortFilters() {
        clearSortFilters()

        when (sortBy) {
            Utils.SortBy.stars.name -> textViewStars.isSelected = true
            Utils.SortBy.forks.name -> textViewForks.isSelected = true
            Utils.SortBy.updated.name -> textViewUpdated.isSelected = true
        }
    }

    private fun manageOrderByFilters() {
        clearOrderByFilters()

        when (orderBy) {
            Utils.OrderBy.asc.name -> textViewAscending.isSelected = true
            Utils.OrderBy.desc.name -> textViewDescending.isSelected = true
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imageViewSearch -> {
                homePresenterImpl.searchRepos(editTextSearch.text.toString(), sortBy, orderBy)
            }

            R.id.imageViewFilters -> {
                dialog.show()
            }

            R.id.textViewStars -> {
                sortBy = Utils.SortBy.stars.name
                manageSortFilters()
            }

            R.id.textViewForks -> {
                sortBy = Utils.SortBy.forks.name
                manageSortFilters()
            }

            R.id.textViewUpdated -> {
                sortBy = Utils.SortBy.updated.name
                manageSortFilters()
            }

            R.id.textViewAscending -> {
                orderBy = Utils.OrderBy.asc.name
                manageOrderByFilters()
            }

            R.id.textViewDescending -> {
                orderBy = Utils.OrderBy.desc.name
                manageOrderByFilters()
            }

            R.id.textViewApply -> {
                dialog.dismiss()
                homePresenterImpl.onFilterApply()
                imageViewSearch.performClick()
            }

            R.id.textViewClear -> {
                dialog.dismiss()
                homePresenterImpl.onFilterClear()
            }
        }
    }

    override fun onLoadMore() {
        homePresenterImpl.onLoadMore()
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, RepoActivity::class.java)
        intent.putExtra(Constants.REPO_DETAILS, item)
        startActivity(intent)
    }

    override fun changeMessage(message: String) {
        textViewMessage.text = message
        showMessage()
    }

    override fun showMessage() {
        hideList()
        textViewMessage.visibility = View.VISIBLE
    }

    override fun hideMessage() {
        showList()
        textViewMessage.visibility = View.GONE
    }

    override fun updateList(items: List<Item>) {
        hideMessage()
        repoAdapter.setLoaded()
        repoAdapter.updateItems(items)
    }

    override fun clearList() {
        repoAdapter.clearItems()
    }

    override fun showList() {
        recyclerView.visibility = View.VISIBLE
    }

    override fun hideList() {
        recyclerView.visibility = View.GONE
    }

    override fun resetFilters() {
        clearOrderByFilters()
        clearSortFilters()
    }

}
