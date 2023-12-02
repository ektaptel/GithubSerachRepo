package  com.example.githubsearchrepo.owner

import  com.example.githubsearchrepo.api.models.Item

interface OwnerView {

    fun attachRepos(items: List<Item>)

}