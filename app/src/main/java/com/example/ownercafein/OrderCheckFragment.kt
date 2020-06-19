package com.example.ownercafein

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.store_info.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

class OrderCheckFragment : Fragment(){
    companion object {
        var storeList = arrayListOf<JSONObject>()
       lateinit var getStoreId : String
    }

    fun getStores() {
        var client = OkHttpClient()

        var request = Request.Builder()
            .url(LoginPage().getServer() + "store/android/" + Owners().getOwnerId() + "/storeList")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST", "SUCCESS!")
                var list = arrayListOf<JSONObject>()
                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        var StoreId = jsonObject.getString("id")
                        list.add(jsonObject)
                        getStoreId = StoreId
                    }
                    storeList = list
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getStores()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getStores()

        var view = inflater.inflate(R.layout.order_check, container, false)

        var storeListRecyclerView = view.findViewById<RecyclerView>(R.id.menuRecyclerView)
        storeListRecyclerView.layoutManager = LinearLayoutManager(context)
        storeListRecyclerView.adapter = StoreListAdapter()
        storeListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        Thread.sleep(300)
        return view
    }

    public fun newInstance(): Fragment {
        var fragment = OrderCheckFragment()
        var args = Bundle()
        fragment.arguments = args
        return fragment
    }

    inner class StoreListAdapter : RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
        lateinit var view: View


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {

            view = LayoutInflater.from(context).inflate(R.layout.store_info, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return StoresFragment.storeList.size
        }

        override fun onBindViewHolder(
            holder: StoreListAdapter.ViewHolder,
            position: Int
        ) {

            var store = StoresFragment.storeList[position]
            holder.storeName.text = store.getString("name")
            holder.location.text = store.getString("location")
            holder.congestion.text = "상세보기"
            holder.theme.text = store.getString("theme")

            holder.storeName.setOnClickListener(
                RecyclerViewOnClickListener(
                    store.getString("id"),
                    store.getString("storeUrl")
                )
            )
            holder.congestion.setOnClickListener (
                RecyclerViewOnClickListener(
                    store.getString("id") ,
                    store.getString("storeUrl")
                )
            )

        }


        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var storeName = v.storeName
            var location = v.storeLocation
            var congestion = v.storeCongestion
            var theme = v.storeTheme
        }

        inner class RecyclerViewOnClickListener(storeId: String, storeUrl: String) :
            View.OnClickListener {

            var storeId = storeId
            var storeUrl = storeUrl
            override fun onClick(v: View?) {
                (activity as Owners?)?.replaceOrderListFragment(storeId)

            }
        }
    }
}