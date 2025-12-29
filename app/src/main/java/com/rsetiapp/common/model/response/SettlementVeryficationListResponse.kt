package com.rsetiapp.common.model.response

data class SettlementVeryficationListResponse(
    val appCode: Any?,
    val responseCode: Int?,
    val responseDesc: String,
    val responseMsg: Any?,
    val wrappedList: List<CandidateSettlementVerificationDetail>
)