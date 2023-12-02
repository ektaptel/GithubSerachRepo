package  com.example.githubsearchrepo.webview

 import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
 import androidx.appcompat.app.AppCompatActivity
 import  com.example.githubsearchrepo.R
import  com.example.githubsearchrepo.api.Constants

class WebViewActivity : AppCompatActivity() {
 lateinit var webView : WebView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView);

        val repoUrl = intent.getStringExtra(Constants.REPO_URL)
        val repoName = intent.getStringExtra(Constants.REPO_NAME)

        supportActionBar?.title = repoName



        webView.webViewClient = MyWebClient()
        webView.settings.setSupportZoom(true)
        webView.settings.supportMultipleWindows()

        webView.loadUrl(repoUrl.toString())

    }

    class MyWebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            view.loadUrl(url)
            return true

        }
    }
}
