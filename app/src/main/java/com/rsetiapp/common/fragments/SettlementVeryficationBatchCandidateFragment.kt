package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.adapter.SettlementVeryficationDetailsAdapter
import com.rsetiapp.common.model.request.SettlementVeryficationReq
import com.rsetiapp.common.model.response.CandidateSettlementVerificationDetail
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.databinding.FragmentSettlementVeryficationBatchCandidateBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class SettlementVeryficationBatchCandidateFragment :   BaseFragment<FragmentSettlementVeryficationBatchCandidateBinding>(FragmentSettlementVeryficationBatchCandidateBinding::inflate) {

    private var state = ""
    private var status = ""


    private lateinit var settlementVeryfiationAdapter: SettlementVeryficationDetailsAdapter


    private val SettlementVeryficationBatch = mutableListOf<CandidateSettlementVerificationDetail>()
    private val commonViewModel: CommonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

//        batchId = arguments?.getString("batchId").toString()
//        batchName = arguments?.getString("batchName").toString()

        init()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        collectCandidatesData()
    }

    private fun init() {
        binding.tvTitleName.text = "Settlement Veryfication"
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        settlementVeryfiationAdapter = SettlementVeryficationDetailsAdapter(SettlementVeryficationBatch)
        binding.rvCandidate.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCandidate.adapter = settlementVeryfiationAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectCandidatesData() {
        commonViewModel.getSettlementsLoginAPI(SettlementVeryficationReq("30", "2")
//        val value = AppUtil.getSavedEntityPreference(requireContext())
//        val lastTwoDigits = value.takeLast(2)
//        commonViewModel.getSettlementsLoginAPI(SettlementVeryficationReq(lastTwoDigits, "2")

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
                            SettlementVeryficationBatch.clear()
                            SettlementVeryficationBatch.addAll(list)
                            settlementVeryfiationAdapter.notifyDataSetChanged()
                        } else {
                            showSnackBar("No settlement data available")
                        }
                    }
                }
            }
        }


    }
}