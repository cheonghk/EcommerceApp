package com.example.myapplication.Retrofit.Model


import android.os.Parcel
import android.os.Parcelable

class ItemInfo_Parcelable(var name: String? = null, var price: Long? = null,
                          var size: Array<String>, var url: Array<String>) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.createStringArray() as Array<String>,
        parcel.createStringArray() as Array<String>
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(price)
        parcel.writeArray(size)
        parcel.writeArray(url)

    }

    companion object CREATOR : Parcelable.Creator<ItemInfo_Parcelable> {
        override fun createFromParcel(parcel: Parcel): ItemInfo_Parcelable {
            return ItemInfo_Parcelable(parcel)
        }

        override fun newArray(size: Int): Array<ItemInfo_Parcelable?> {
            return arrayOfNulls(size)
        }
    }


}