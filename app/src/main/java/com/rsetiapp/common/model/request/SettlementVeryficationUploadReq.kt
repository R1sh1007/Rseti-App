package com.rsetiapp.common.model.request


data class SettlementVeryficationUploadReq(

    val candidateId: String?,
    val candidateName: String?,
    val instituteId: String?,
    val guardianName: String?,
    val settlementId: String?,
    val followUpId: String?,
    val ifscCode: String?,
    val loanAccountNo: String?,
    val remark: String?,
    val imageBase64: String?,
    val latitude: Double?,
    val longitude: Double?,
    val batchid: String?,
    val bankname: String?,
    val cityname: String?,
    val creditFromBank: String?,
    val selfInvestment: String?,
    val totalInvestment: String?,
    val updatedBy: String?,
    val rollNo: String?,
    val salaryRange: String?,
    val employmentGiven: String?,
    val familyMemberPartTime: String?,
    val loginId: String?,
    val appVersion: String?

)



