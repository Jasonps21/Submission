package com.ismail.submission2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val user = intent.getParcelableExtra(EXTRA_USER) as User
        getUserData(user.username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f


    }

    private fun getUserData(username: String) {

        progressBar2.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                progressBar2.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val item = JSONObject(result)
                    Glide.with(this@DetailActivity)
                        .load(item.getString("avatar_url"))
                        .apply(RequestOptions())
                        .into(img_detail_avatar)
                    tv_detail_username.text = "@${item.getString("login")}"
                    tv_detail_name.text = item.getString("name")
                    tv_detail_following.text = item.getString("following")
                    tv_detail_followers.text = item.getString("followers")
                    tv_detail_company.text = item.getString("company")
                    tv_detail_location.text = item.getString("location")
                    tv_detail_repository.text = item.getString("public_repos")
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                TODO("Not yet implemented")
            }
        })
    }
}