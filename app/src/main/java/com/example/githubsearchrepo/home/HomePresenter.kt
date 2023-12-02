package  com.example.githubsearchrepo.home

interface HomePresenter {

    fun searchRepos(searchText: String, sortBy: String, orderBy: String);

    fun onDialogCancel()

    fun onFilterApply()

    fun onFilterClear()

    fun onLoadMore()
}