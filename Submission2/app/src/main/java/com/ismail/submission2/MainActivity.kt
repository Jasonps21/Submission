package com.ismail.submission2

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showLoading(true)
        selectDefaultUserToShow()
        showRecyclerList(list)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    showLoading(true)
                    getSearchUser(query)
                    showRecyclerList(list)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        return true
    }


    private fun showRecyclerList(list: ArrayList<User>) {
        rv_users.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rv_users.adapter = listUserAdapter

    }


    private fun getSearchUser(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${username}"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val avatar_url = item.getString("avatar_url")
                        val username = item.getString("login")
                        val user = User(
                            avatar_url,
                            "",
                            username,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        list.add(user)
                        showLoading(false)

                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
            }

        })
    }

    private fun showDefaultUserList(name: String) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${name}"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val items = JSONObject(result)
                    val avatar_url = items.getString("avatar_url")
                    val username = items.getString("login")
                    val company = items.getString("company")
                    val location = items.getString("location")

                    val user = User(
                        avatar_url,
                        "",
                        username,
                        company,
                        location,
                        "",
                        "",
                        ""
                    )
                    list.add(user)
                    showLoading(false)


                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
            }

        })


    }

    private fun selectDefaultUserToShow() {
        val dataUsername = resources.getStringArray(R.array.username)
        for (name in dataUsername) {
            showDefaultUserList(name)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
