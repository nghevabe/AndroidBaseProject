package com.example.mybaseproject.networks.entities.response

import android.os.Parcelable
import com.example.mybaseproject.networks.entities.restful.ResponseEntity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class DataResp : ResponseEntity<DataTest>()
class DataTest{
    @SerializedName("name") val name:String? = null
}

class CategoryResp : ResponseEntity<ListCategory>()

class ListCategory{
    @SerializedName("list_category")
    val listCategory: ArrayList<CategoryItem>? = null
    @SerializedName("tEtag")
    val tEtag: String? = null
}

@Parcelize
data class CategoryItem(
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("nameVi")
    var nameVi: String? = null,
    @SerializedName("nameEn")
    var nameEn: String? = null,
    @SerializedName("icon")
    var icon: String? = null
) : Parcelable



