package com.rsetiapp.common.model.response


data class InstituteResponse(
    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: MutableList<Institutes>
)

data class Institutes(
    val instituteId: String,
    val instituteName: String
)