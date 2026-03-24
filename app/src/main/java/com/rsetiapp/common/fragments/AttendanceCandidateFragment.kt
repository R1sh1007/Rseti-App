package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.adapter.AttendanceCandidateAdapter
import com.rsetiapp.common.model.response.Candidate
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.AttendanceCandidateFragmentBinding
import kotlinx.coroutines.launch

class AttendanceCandidateFragment  :
    BaseFragment<AttendanceCandidateFragmentBinding>(AttendanceCandidateFragmentBinding::inflate)   {

    private lateinit var candidateListAdapter: AttendanceCandidateAdapter
    private var AttendanceCandidateList = mutableListOf<Candidate>()
    private val commonViewModel: CommonViewModel by activityViewModels()
    private var batchId = ""
    private var batchName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        batchId = arguments?.getString("batchId").toString()
        batchName = arguments?.getString("batchName").toString()

        commonViewModel.getAttendanceCandidate(AppUtil.getSavedTokenPreference(requireContext()),BuildConfig.VERSION_NAME,batchId,AppUtil.getAndroidId(requireContext()),userPreferences.getUseID())
        collectAttendanceBatchResponse()

        init()
        setupRecyclerView()
    }
    private fun init(){
        listener()
    }

//hjfjd ghhhr

    private fun listener(){

        binding.backButton.setOnClickListener {

            findNavController().navigateUp()

        }
    }
    private fun setupRecyclerView() {

        candidateListAdapter = AttendanceCandidateAdapter(AttendanceCandidateList)
        binding.recyclerViewCandidates.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCandidates.adapter = candidateListAdapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun collectAttendanceBatchResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getAttendanceCandidate) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error111")
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getAttendanceBatchAPI ->
                            if (getAttendanceBatchAPI.responseCode == 200) {
                                AttendanceCandidateList.clear()
                                AttendanceCandidateList.addAll(getAttendanceBatchAPI.wrappedList)
                                candidateListAdapter.notifyDataSetChanged()
                            } else if (getAttendanceBatchAPI.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }
                            else if (getAttendanceBatchAPI.responseCode==401){
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }
                            else {
                                toastLong(getAttendanceBatchAPI.responseDesc)
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }
}