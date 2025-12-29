package com.rsetiapp.common.model.response

data class DistrictListResponse(
    val appCode: Any?,
    val responseCode: Int?,
    val responseDesc: String,
    val responseMsg: Any?,
    val districtList: List<DistrictList>
)