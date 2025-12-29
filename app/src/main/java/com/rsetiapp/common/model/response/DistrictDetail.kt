package com.rsetiapp.common.model.response

import java.io.Serializable

data class DistrictDetail(
    val districtCode: String?,
    val districtName: String?,
    val lgdDistrictCode: String?
): Serializable

// "districtList": [
//        {
//            "districtCode": "0541",
//            "districtName": "ARARIA",
//            "lgdDistrictCode": "188"
//        },