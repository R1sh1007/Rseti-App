package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.MySattelementBottomSheet
import com.rsetiapp.common.VeryficationSattelementBottomSheet
import com.rsetiapp.common.adapter.SettlementVeryficationDetailsAdapter
import com.rsetiapp.common.model.request.SettlementPrefModel
import com.rsetiapp.common.model.request.SettlementVeryficationReq
import com.rsetiapp.common.model.response.CandidateSettlementVerificationDetail
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.AppUtil.getCandidateListPreference
import com.rsetiapp.core.util.AppUtil.saveCandidateListPreference
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.databinding.FragmentSettlementVeryficationBatchCandidateBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.slf4j.helpers.Util
import kotlin.getValue

class SettlementVeryficationBatchCandidateFragment :   BaseFragment<FragmentSettlementVeryficationBatchCandidateBinding>(FragmentSettlementVeryficationBatchCandidateBinding::inflate) {

    private var state = ""
    private var status = ""
    private var candidateId = ""
    private var batchName = ""


    private lateinit var settlementVeryfiationAdapter: SettlementVeryficationDetailsAdapter


    private val SettlementVeryficationBatch = mutableListOf<CandidateSettlementVerificationDetail>()
    private val commonViewModel: CommonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        init()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        collectCandidatesData()
    }

    private fun init() {
        binding.tvTitleName.text = getString(R.string.settlement_veryfication)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        settlementVeryfiationAdapter = SettlementVeryficationDetailsAdapter(SettlementVeryficationBatch) { candidate ->

            val model = SettlementPrefModel(
                instituteId = AppUtil.getSavedinstituteIdPreference(requireContext()) ?: "",
                candidateId = candidate.candidateId ?: "",
                candidateName = candidate.candidateName ?: "",
                mobileNo = candidate.mobileNo ?: "",
                guardianName = candidate.guardianName ?: "",
                guardianMobileNo = candidate.guardianMobileNo ?: "",
                aadharBlockName = candidate.aadharBlockName ?: "",
                aadharPinCode = candidate.aadharPinCode ?: "",
                settlementId = candidate.settlementId?.toString() ?: "",
                followUpId = candidate.followUpId?.toString() ?: "",
                batchId = candidate.batchId?.toString() ?: "",
                ifscCode = candidate.ifscCode ?: "",
                loanAccountNo = candidate.loanAccountNo ?: "",
                creditFromBank = candidate.creditFromBank ?: "",
                selfInvestment = candidate.selfInvestment ?: "",
                totalInvestment = candidate.totalInvestment ?: "",
                passbookCopy = candidate.passbookCopy ?: "",
                appointmentLetter = candidate.appointmentLetter ?: "",
                settlementPhoto = candidate.settlementPhoto ?: "",
                updatedBy = candidate.updatedBy ?: "",
                latitude = candidate.latitude ?: "0.0",
                longitude = candidate.longitude ?: "0.0",
                rollNo = candidate.rollNo?.toString() ?: "",
                cityName = candidate.cityName ?: "",
                settlementReason = candidate.settlementReason ?: "",
                accountStatus = candidate.accountStatus ?: "",
                salaryRange = candidate.salaryRange ?: "",
                employmentGiven = candidate.employmentGiven ?: "",
                familyMemberPartTime = candidate.familyMemberPartTime ?: "",
                bankName = candidate.bankName ?: "",
                branchName = candidate.branchName ?: "",
                followupType = candidate.followupType ?: "",
                statusName = candidate.statusName ?: "",
                salaryRangeId = candidate.salaryRangeId ?: ""
            )


            val gson = GsonBuilder().setPrettyPrinting().create()
            val requestJson = gson.toJson(model)

            Log.d("ReverificationAPI", "📤 REQUEST JSON:\n$requestJson")
            AppUtil.saveItem(requireContext(), model)



            val bottomSheet = VeryficationSattelementBottomSheet()
            bottomSheet.show(parentFragmentManager, "MySattelementBottomSheet")

        }


        binding.rvCandidate.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCandidate.adapter = settlementVeryfiationAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectCandidatesData() {
        commonViewModel.getSettlementsLoginAPI(SettlementVeryficationReq(BuildConfig.VERSION_NAME, AppUtil.getSavedCandidatePreference(requireContext()))

        )






        lifecycleScope.launch {
            commonViewModel.getsettlementVeryfication.collectLatest { resource ->
                when (resource) {

                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar(resource.error?.message ?: "Internal Server Error")
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        val list = resource.data?.wrappedList

                        if (!list.isNullOrEmpty()) {

                            // 🔹 Filter SYSTEM updated candidates
                            val filteredList = list.filter {
                                it.updatedBy == null || !it.updatedBy.equals("SYSTEM", ignoreCase = true)
                            }

                            if (filteredList.isNotEmpty()) {
                                SettlementVeryficationBatch.clear()
                                SettlementVeryficationBatch.addAll(filteredList)
                                settlementVeryfiationAdapter.notifyDataSetChanged()
                            } else {
                                // ✅ THIS IS WHAT YOU WANT
                                SettlementVeryficationBatch.clear()
                                settlementVeryfiationAdapter.notifyDataSetChanged()
                                showSnackBar("No settlement candidate available")
                            }

                        } else {
                            showSnackBar("No settlement data available")
                        }
                    }
                }
            }
        }


//    Please donnot delete code

//        lifecycleScope.launch {
//            commonViewModel.getsettlementVeryfication.collectLatest { resource ->
//                when (resource) {
//
//                    is Resource.Loading -> showProgressBar()
//
//                    is Resource.Error -> {
//                        hideProgressBar()
//                        showSnackBar(resource.error?.message ?: "Internal Server Error")
//                    }
//
//                    is Resource.Success -> {
//                        hideProgressBar()
//
//                        val list = resource.data?.wrappedList
//                        if (!list.isNullOrEmpty()) {
//                            SettlementVeryficationBatch.clear()
//                            SettlementVeryficationBatch.addAll(list)
//                            settlementVeryfiationAdapter.notifyDataSetChanged()
//                        } else {
//                            showSnackBar("No settlement data available")
//                        }
//                    }
//                }
//            }
//        }
    }
}