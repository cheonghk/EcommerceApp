package com.example.myapplication.FireBase

import com.google.firebase.database.*


class FireBaseCollector {

    private var database = FirebaseDatabase.getInstance()
    private var iteminfoRef = database.getReference().child("product_info")
    private var name : String? = null
    private var price : Long? = null
    private var unicode :  String? = null
    private var url_forRecyclerview : String? = null
    private var itemList = mutableListOf<ItemInfo_Firebase_Model>()


    fun readData_userShoppingCart(status: ShoppingCartDataStatus){
        iteminfoRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                "The read failed: " + p0.getMessage()
            }

            override fun onDataChange(p0: DataSnapshot) {

                var newItemListWrapper2  = mutableListOf<MutableList<ItemInfo_Firebase_Model>>()
                var newItemListWrapper2Index = 0

                for (i in 1 until p0.childrenCount + 1) {
                    var newItemListWrapper = mutableListOf<ItemInfo_Firebase_Model>()
                    var theChild = p0.child("${i}")

                    var newItemListWrapperIndex = 0
                    for (j in 1 until theChild.childrenCount + 1) {
                        var newItemList : ItemInfo_Firebase_Model? = null
                        unicode = null
                        name = null
                        price = null
                        var size = mutableListOf<String>()
                        var url = mutableListOf<String>()
                        var theChild2 = theChild.child("${j}")
                        name = theChild2.child("name").getValue().toString()
                        price = theChild2.child("price").getValue() as Long
                        unicode = theChild2.child("unicode").getValue().toString()

                        for (data in theChild2.child("size").children) {
                            size.add(data.getValue().toString())
                        }

                        for (data in theChild2.child("url").children) {
                            url.add(data.getValue().toString())
                        }

                        url_forRecyclerview =
                            theChild2.child("url").child("1").getValue().toString()
                        newItemList =  ItemInfo_Firebase_Model(
                                unicode,
                                name,
                                price,
                                size,
                                url,
                                url_forRecyclerview
                            )

                        newItemListWrapper.add(newItemListWrapperIndex, newItemList)
                        newItemListWrapperIndex++
                    }
                    newItemListWrapper2 .add(newItemListWrapper2Index,newItemListWrapper)
                    newItemListWrapper2Index++
                }
                status.ShoppingCartData(newItemListWrapper2 )
            }
        })
    }



    fun readData_CategoryContoller(status: DataStatus, category:String) {
            iteminfoRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    "The read failed: " + p0.getMessage()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    itemList.clear()
                        var theChild = p0.child(category)
                        for (j in 1 until theChild.childrenCount + 1) {
                            name = null
                            price = null
                            unicode = null
                            var size = mutableListOf<String>()
                            var url = mutableListOf<String>()

                            var theChild2 = theChild.child("${j}")
                            name = theChild2.child("name").getValue().toString()
                            price = theChild2.child("price").getValue() as Long
                            unicode = theChild2.child("unicode").getValue().toString()

                            for (data in theChild2.child("size").children) {
                                size.add(data.getValue().toString())
                            }

                            for (data in theChild2.child("url").children) {
                                url.add(data.getValue().toString())
                            }

                            url_forRecyclerview =
                                theChild2.child("url").child("1").getValue().toString()
                            itemList.add(
                                ItemInfo_Firebase_Model(
                                    unicode,
                                    name,
                                    price,
                                    size,
                                    url,
                                    url_forRecyclerview
                                )
                            )
                        }
                    status.DataIsLoaded(itemList)
                }
            })
    }

    fun readAllData(status: DataStatus) {
            iteminfoRef.addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    "The read failed: " + p0.getMessage()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    itemList.clear()
                    for (i in 1 until p0.childrenCount + 1) {
                        var theChild = p0.child("${i}")

                        for (j in 1 until theChild.childrenCount + 1) {
                            unicode = null
                            name = null
                            price = null
                            var size = mutableListOf<String>()
                            var url = mutableListOf<String>()
                            var theChild2 = theChild.child("${j}")
                            name = theChild2.child("name").getValue().toString()
                            price = theChild2.child("price").getValue() as Long
                            unicode = theChild2.child("unicode").getValue().toString()

                            for (data in theChild2.child("size").children) {
                                size.add(data.getValue().toString())
                            }

                            for (data in theChild2.child("url").children) {
                                url.add(data.getValue().toString())
                            }

                            url_forRecyclerview =
                                theChild2.child("url").child("1").getValue().toString()
                            itemList.add(
                                ItemInfo_Firebase_Model(
                                    unicode,
                                    name,
                                    price,
                                    size,
                                    url,
                                    url_forRecyclerview
                                )
                            )
                        }
                    }
                    status.DataIsLoaded(itemList)
                }
            })
    }

    interface ShoppingCartDataStatus {
        fun ShoppingCartData(retriveListByCategoryPosition : MutableList<MutableList<ItemInfo_Firebase_Model>>)
    }

    interface DataStatus {
        fun DataIsLoaded(userShoppingCartInfo:  MutableList<ItemInfo_Firebase_Model>)
    }
}