package com.rsetiapp.common.model.response

import java.io.Serializable

data class SettlementPercentage(
    val batchId: String?,
    val candidateId: String?,
    val instituteId: String?,
    val settledCandidates: String?,
    val totalCandidates: String?,
    val batchRegNo: String?,
    val batchName: String?,
    val instituteName: String?,
    val settledPercentage: String
): Serializable

