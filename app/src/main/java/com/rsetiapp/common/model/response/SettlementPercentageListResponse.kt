package com.rsetiapp.common.model.response

data class SettlementPercentageListResponse(
    val wrappedList: List<SettlementPercentage>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?,
    val appCode: String?
)
