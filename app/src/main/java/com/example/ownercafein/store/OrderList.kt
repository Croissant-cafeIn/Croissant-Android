package com.example.ownercafein.store

data class OrderList(

    val order_id: Int,
    val client_id: Int,
    val store_id: Int,
    val store_name: String,
    val order_list: String,
    val order_date: String,
    val order_status: Int,
    val get_order_status: String,
    val point: String

)
