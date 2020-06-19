package com.example.ownercafein

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.HotBeverageFragment
import com.example.cafein.IceBeverageFragment
import com.example.cafein.MenuList
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.store_info.view.*
import kotlinx.android.synthetic.main.store_info.view.storeCongestion
import kotlinx.android.synthetic.main.store_info.view.storeLocation
import kotlinx.android.synthetic.main.store_info.view.storeName
import kotlinx.android.synthetic.main.store_info.view.storeTheme
import kotlinx.android.synthetic.main.store_info2.view.*
import kotlinx.android.synthetic.main.stores.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.security.acl.Owner
import javax.xml.datatype.DatatypeFactory.newInstance

class StoresFragment : Fragment() {

    companion object {
        var storeList = arrayListOf<JSONObject>()
        var setStoreId = ""
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
                        list.add(jsonObject)
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

        var view = inflater.inflate(R.layout.stores, container, false)

        var storeListRecyclerView = view.findViewById<RecyclerView>(R.id.storeListRecyclerView)
        storeListRecyclerView.layoutManager = LinearLayoutManager(context)
        storeListRecyclerView.adapter = StoreListAdapter()
        storeListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        Thread.sleep(150)
        return view
    }


    fun newInstance(): Fragment {
        var fragment = StoresFragment()
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

            view = LayoutInflater.from(context).inflate(R.layout.store_info2, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return storeList.size
        }

        override fun onBindViewHolder(
            holder: StoreListAdapter.ViewHolder,
            position: Int
        ) {

            var store = storeList[position]
            holder.storeName.text = store.getString("name")
            holder.location.text = store.getString("location")
            val items = arrayOf("여유","보통","혼잡")
            val sAdapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
            val congestionSpinner = holder.congestion
            congestionSpinner.adapter = sAdapter
            congestionSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {
                    //TODO("Not yet implemented")
                }

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    val storeId = store.getString("id")
                    setStoreId = storeId

                    var client = OkHttpClient()
                    var jsonInput = JSONObject()

                    var request = Request.Builder().url(LoginPage().getServer() + "store/android/${storeId}/storeList/${congestionSpinner.selectedItemPosition}").get().build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("TEST", "ERROR Message : " + e.message)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            //Toast.makeText(context, "http://10.0.2.2:9090/store/android/$storeId/storeList/${holder.congestion}", Toast.LENGTH_LONG).show()
                            //Log.d("getcongestion", storeId + holder.congestion.text.toString())
                            try {

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }

                    })

                }
            }

            holder.theme.text = store.getString("theme")

            holder.storeName.setOnClickListener(
                RecyclerViewOnClickListener(
                    store.getString("id"),
                    store.getString("storeUrl")
                )
            )


        }


        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var storeName = v.storeName
            var location = v.storeLocation
            var congestion: Spinner = v.storeCongestion2
            var theme = v.storeTheme
        }

        inner class RecyclerViewOnClickListener(storeId: String, storeUrl: String) :
            View.OnClickListener {

            var storeId = storeId
            var storeUrl = storeUrl
            override fun onClick(v: View?) {
                Owners().changeToMenuList(setStoreId)

            }
        }
    }


}