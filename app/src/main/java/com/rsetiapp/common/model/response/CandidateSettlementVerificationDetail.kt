package com.rsetiapp.common.model.response

import java.io.Serializable

data class CandidateSettlementVerificationDetail(
    val candidateName: String?,
    val emailId: String?,
    val mobileNo: String?,
    val guardianName: String?,
    val guardianMobileNo: String?,
    val aadharBlockName: String?,
    val aadharPinCode: String?,
    val settlementId: Int?,
    val followUpId: Int?,
    val batchId: Int?,
    val candidateId: String?,
    val ifscCode: String?,
    val loanAccountNo: String?,

    val creditFromBank: Int?,
    val selfInvestment: Int?,
    val totalInvestment: Int?,
    val rollNo: Int?,
    val latitude: String?,
    val longitutde: String?,
    val quarterOne: Int?,
    val quarterTwo: Int?,
    val quarterThree: Int?,
    val quarterFour: Int?,
    val quarterFive: Int?,
    val quarterSix: Int?,
    val quarterSeven: Int?,
    val quarterEight: Int?,


    val passbookCopy: String?,
    val appointmentLetter: String?,
    val settlementPhoto: String?,
    val updatedBy: String?,
    val longitute: String?,
    val latitute: String?
): Serializable

//latitute = latitude.toString(),
//                            longitute