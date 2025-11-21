package com.rsetiapp.common.model.response
data class EapListResponse(
    val wrappedList: List<EapList>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?,
    val appCode: String?
)
data class EapList(
    val blockCode: String,
    val districtCode: String,
    val districtName: String,
    val stateNme: String,
    val programCode: Int,
    val blockName: String,
    val gpName: String,
    val villageName: String,
    val villageCode: String,
    val eapID: Int,
    val eapName: String,
    val monthYear: String,
    val stateCode: String,
    val eapAddress: String,
    val gpCode: String,
    val status: String
)
