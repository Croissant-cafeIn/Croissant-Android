package com.example.ownercafein

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ownercafein.store.OrderList
import com.example.ownercafein.store.OrderListAdapter
import kotlinx.android.synthetic.main.item_orderlist.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class OrderListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.orderlist_page, container, false)
        var getStoreId = arguments?.getString("id")?.toInt()
        var getOrderId = 0
        //var getStoreUrl = arguments?.getString("storeUrl")
        Toast.makeText(this.context, LoginPage().getServer() + "order/android/storeOrderList/${getStoreId}", Toast.LENGTH_LONG).show()
        val orderlists = arrayListOf<OrderList>()

        var orderlistRecyclerView = view.findViewById<RecyclerView>(R.id.orderlist_recyclerView)
        orderlistRecyclerView.layoutManager = LinearLayoutManager(this.context)
        orderlistRecyclerView.adapter = OrderListAdapter(requireContext(), orderlists) { OrderList ->
            var client = OkHttpClient()
            var request = Request.Builder()
                .url(LoginPage().getServer() + "order/android/status/${OrderList.order_id}")
                .get()
                .build()
            Log.d("getorderid", OrderList.order_id.toString())

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("TEST", "ERROR Message : " + e.message);
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("TEST", "responseData")

                }
            })

        }
        val mLayoutManager = LinearLayoutManager(this.context)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        orderlistRecyclerView.layoutManager = mLayoutManager


        var client = OkHttpClient()

        var request = Request.Builder()
            .url(LoginPage().getServer() + "order/android/storeOrderList/${getStoreId}")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST", "responseData")

                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        var orderId = jsonObject.getInt("orderId")
                        var clientId = jsonObject.getInt("clientId")
                        var storeId = jsonObject.getInt("storeId")
                        var storeName = jsonObject.getString("storeName")
                        var orderList = jsonObject.getString("orderList")
                        var orderDate = "주문일시: " + jsonObject.getString("orderDate")
                        var orderStatus = jsonObject.getInt("orderStatus")
                        var point = "총 "+jsonObject.getInt("point").toString() + "원"

                        var getOrderStatus = "준비중"
                        if(orderStatus == 1)
                            getOrderStatus = "준비완료"
                        orderlists.add(
                            OrderList(orderId, clientId,storeId,storeName,orderList,orderDate,orderStatus,getOrderStatus,point)
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Thread.sleep(300)

            }
        })
        Thread.sleep(300)

        return view
    }
}