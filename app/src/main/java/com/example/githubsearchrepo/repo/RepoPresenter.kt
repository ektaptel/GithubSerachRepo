package  com.example.githubsearchrepo.repo

interface RepoPresenter {

    fun getContributors(ownerName: String, repoName: String)

}