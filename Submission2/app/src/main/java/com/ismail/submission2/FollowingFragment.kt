package com.ismail.submission2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {

    companion object {
        val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_FOLLOWING = "extra_following"
    }

    private var listFollowing: ArrayList<Following> = ArrayList()
    private lateinit var adapter: ListFollowingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListFollowingAdapter(listFollowing)




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    fun getListFollowing(selectedUser: String) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${selectedUser}/following"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val avatar_url = item.getString("avatar_url")
                        val username = item.getString("login")
                        val following = Following(
                            avatar_url,
                            "",
                            username,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        listFollowing.add(following)

                    }


                } catch (e: Exception) {

                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }


        })

    }
}