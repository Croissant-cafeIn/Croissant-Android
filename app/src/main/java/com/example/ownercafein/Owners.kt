package com.example.ownercafein

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.cafein.MenuList
import com.example.ownercafein.R.id
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.owners.*

class Owners : AppCompatActivity(){
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var orderCheckFragment: OrderCheckFragment
    lateinit var storesFragment: StoresFragment
    lateinit var alarmFragment: AlarmFragment

    companion object{
        lateinit var ownerId : String
    }
    fun getOwnerId() = ownerId
    fun setOwnerId(si : String){
        ownerId = si
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owners)

        setOwnerId(intent.getStringExtra("id"))

        if(intent.hasExtra("status")){
            var status = intent.getExtras()!!.get("status")
            when(status){
                0 -> replaceFragment(orderCheckFragment)
                1 -> replaceFragment(storesFragment)
                2 -> replaceFragment(alarmFragment)
            }
        }

        orderCheckFragment = OrderCheckFragment()
        storesFragment = StoresFragment()
        alarmFragment = AlarmFragment()

        orderCheckFragment.getStores()
        supportFragmentManager.beginTransaction().replace(id.fragment, orderCheckFragment).commit()

        bottomNavigationView = findViewById(id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigation())

    }

    fun replaceFragment(fragment: Fragment){
        var fragmentMager = supportFragmentManager
        var fragmentTransaction = fragmentMager.beginTransaction()
        fragmentTransaction.replace(id.fragment, fragment).commit()
    }
    fun replaceOrderListFragment(storeId : String) {
        val bundle = Bundle(1)
        val orderlistfragment =  OrderListFragment()
        bundle.putString("id", storeId)
        Toast.makeText(this, storeId, Toast.LENGTH_LONG).show()
        supportFragmentManager.beginTransaction().replace(id.fragment, orderlistfragment).addToBackStack(null).commit()
        orderlistfragment.arguments = bundle
    }

    fun changeToMenuList(storeId: String){
        var menuList = MenuList()
        var bundle = Bundle()
        bundle.putString("storeId", storeId)
        supportFragmentManager.beginTransaction().replace(id.fragment, menuList).addToBackStack(null).commit()
        menuList.arguments = bundle
    }

    inner class BottomNavigation : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                id.order_check -> {
                    replaceFragment(orderCheckFragment)
                    return true
                }

                id.stores -> {
                    replaceFragment(storesFragment)
                    return true
                }

                id.alarm -> {
                    replaceFragment(alarmFragment)
                    return true
                }
                else -> return false
            }
        }
    }

}