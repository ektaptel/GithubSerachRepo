package  com.example.githubsearchrepo.repo

import  com.example.githubsearchrepo.api.models.Owner

interface RepoView {

    fun attachContributors(list: List<Owner>)

    fun changeMessage(message: String)

}