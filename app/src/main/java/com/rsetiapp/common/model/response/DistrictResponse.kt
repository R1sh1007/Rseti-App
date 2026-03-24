package com.rsetiapp.common.model.response


data class DistrictResponse(
    val responseCode: Int,
    val responseDesc: String,
    val districtList: MutableList<DistrictList>
)

data class DistrictList(
    val districtName: String,
    val districtCode: String
)
//data class DistrictList(
//    val districtName: String,
//    val districtCode: String,
//    val lgdDistrictCode:String,
//)