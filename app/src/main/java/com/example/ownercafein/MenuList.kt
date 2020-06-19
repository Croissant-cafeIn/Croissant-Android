package com.example.cafein

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ownercafein.LoginPage
import com.example.ownercafein.R
import kotlinx.android.synthetic.main.menu_list.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.URLEncoder


class MenuList : Fragment() {

    companion object {
        lateinit var noDessertView: View
        var storeId: String? = ""
        var storeUrl: String = ""
        var storeName = ""
        var storeLocation = ""
    }

    fun getStoreId() = storeId
    fun getStoreName() = storeName
    fun getStoreUrl() = storeUrl
    fun setStoreUrl(st: String) {
        storeUrl = st
    }

    fun setStoreId(si: String?) {
        storeId = si
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.menu_list, container, false)
        val cafeImage = view.findViewById<ImageView>(R.id.cafeImage)
        val iceFragment = IceBeverageFragment()
        val beverage = view.findViewById<Button>(R.id.beverage)
        val bread = view.findViewById<Button>(R.id.bread)
        val ice = view.findViewById<RadioButton>(R.id.ice)
        val hot = view.findViewById<RadioButton>(R.id.hot)
        noDessertView = view.findViewById<TextView>(R.id.notDessert)
        if(arguments != null)
            setStoreId(arguments?.getString("storeId"))

        Log.e("storeId", storeId.toString())
        Glide.with(requireContext()).load(getStoreUrl()).into(cafeImage)
        Thread().run { this@MenuList.getStore() }
        Thread.sleep(300)
        Thread().run { iceFragment.getMenu() }

        Thread.sleep(300)

        /*var list:List<Address>? = null
        view.findViewById<TextView>(R.id.cafeLocation).setOnClickListener {
            var url = "nmap://route/public?"
            try{
                var geocoder = Geocoder(context)
                list = geocoder.getFromLocationName(storeLocation, 5)
            } catch (e: IOException){
                e.printStackTrace()
            }
            if(list != null){
                if(list!!.size == 0){
                    Toast.makeText(context, "해당하는 주소가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                else{
                    var addr = list!!.get(0)
                    var lat = addr.latitude
                    var lon = addr.longitude
                    Log.e("lat/lon" + lat.toString() + lon.toString())
                    url += "dlat=" + String.format(".%2f", lat) + "&dlng=" + String.format(".%2f", lon) +
                            "&dname=" + URLEncoder.encode(storeName) + "&appname=com.example.cafein"

                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)

                    requireContext().startActivity(intent)



                }
            }

        }*/

        changeFragment(iceFragment)

        beverage.setOnClickListener {
            degree.visibility = View.VISIBLE
            notDessert.visibility = View.GONE
            var iceBeverageFragment = IceBeverageFragment()
            iceBeverageFragment.getMenu()
            Thread.sleep(150)
            changeFragment(IceBeverageFragment())

        }
        bread.setOnClickListener {
            degree.visibility = View.GONE

            var breadFragment = BreadFragment()
            breadFragment.getMenu()
            Thread.sleep(150)

            changeFragment(breadFragment)

        }
        ice.setOnClickListener {
            var iceBeverageFragment = IceBeverageFragment()
            iceBeverageFragment.getMenu()
            Thread.sleep(150)

            changeFragment(IceBeverageFragment())
        }
        hot.setOnClickListener {
            var hotBeverageFragment = HotBeverageFragment()
            hotBeverageFragment.getMenu()
            Thread.sleep(150)

            changeFragment(HotBeverageFragment())
        }

        return view
    }

    // 해당 가게id의 jsonObject 불러오기


    fun changeFragment(fragment: Fragment) {
        var fragmentMager = childFragmentManager
        var fragmentTransaction = fragmentMager.beginTransaction()
        fragmentTransaction.replace(R.id.choose, fragment).commit()
    }

    fun getStore() {

        var okClient = OkHttpClient()
        var request = Request.Builder()
            .url(LoginPage().getServer() + "store/android/list")
            .get()
            .build()

        okClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST", "responseDatae")

                try {
                    var jsonArray = JSONArray(response.body()?.string().toString())
                    var length = jsonArray.length()
                    for (i in 0..length - 1) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        if (storeId == jsonObject.getString("id")) {
                            var handler = Handler(Looper.getMainLooper())
                            handler.post(Runnable {
                                kotlin.run {
                                    cafeName.text =
                                        "NAME             " + jsonObject.getString("name")
                                    storeName = jsonObject.getString("name")
                                    cafeLocation.text =
                                        "LOCATION        " + jsonObject.getString("location")
                                    storeLocation = jsonObject.getString("location")
                                    hours.text = "OPEN/CLOSE    " + jsonObject.getString("hour")
                                    phone.text =
                                        "CONTECT         " + jsonObject.getString("phoneNumber")
                                    scoreBar.rating = jsonObject.getInt("like").toFloat()

                                }
                            })

                            break
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


        })
    }

}
