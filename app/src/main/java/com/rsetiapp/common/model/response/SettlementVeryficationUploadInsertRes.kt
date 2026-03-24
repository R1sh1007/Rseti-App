package com.rsetiapp.common.model.response

data class SettlementVeryficationUploadInsertRes(

    val wrappedList: List<UploadInsert>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?,
)

data class UploadInsert(
    val message: Boolean,
    val status: String
)
