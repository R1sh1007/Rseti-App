package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.response.AttendanceBatch
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.basecomponent.BaseRecyclerAdapter
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.NoDataHelper
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.databinding.AttendanceBatchFragmentBinding
import com.rsetiapp.databinding.AttendanceBatchLayoutBinding
import kotlinx.coroutines.launch

//Code Commit in use 24.12.2025.18.40PM

class AttendanceBatchFragment :
    BaseFragment<AttendanceBatchFragmentBinding>(AttendanceBatchFragmentBinding::inflate) {

    private lateinit var batchAdapter: BaseRecyclerAdapter<AttendanceBatch, AttendanceBatchLayoutBinding>
    private var AttendanceBatchList = mutableListOf<AttendanceBatch>()
    private val commonViewModel: CommonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        init()
        setupRecyclerView()

        if (AttendanceBatchList.isEmpty()) {
            NoDataHelper.showNoData(binding.container, "No Data Found")
        } else {
            NoDataHelper.hideNoData(binding.container)
        }
    }

    private fun init() {
        commonViewModel.getAttendanceBatchAPI(
            AppUtil.getSavedTokenPreference(requireContext()),
            BuildConfig.VERSION_NAME,
            AppUtil.getAndroidId(requireContext()),
            userPreferences.getUseID()
        )

        collectAttendanceBatchResponse()
        listener()
    }

    private fun listener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {

        batchAdapter = BaseRecyclerAdapter(
            items = AttendanceBatchList,
            bindingInflater = AttendanceBatchLayoutBinding::inflate,

            onBind = { batch, binding, position ->

                binding.tvBatchIdName.text = ": "+batch.batchRegNumber
                binding.tvBatchName.text = ": "+batch.batchName

                // VIEW-WISE CLICK (call base adapter trigger)
                binding.root.setOnClickListener {
                    batchAdapter.triggerViewClick(it, batch, position)
                }
            },

            onViewClick = { _, data, pos ->

                val options = arrayOf("Mark Attendance of Candidate", "Self Attendance")

                AlertDialog.Builder(requireContext())
                    .setTitle("Choose an option")
                    .setItems(options) { _, which ->
                        when (which) {
                            0 -> {
                                val action =
                                    AttendanceBatchFragmentDirections
                                        .actionAttendanceBatchFragmentToAttendanceCandidateFragment(
                                            (data.batchCode ?: "0").toString(),
                                            data.batchName ?: "Batch Name"
                                        )
                                binding.recyclerViewBatches.findNavController().navigate(action)
                            }

                            1 -> {
                                val action =
                                    AttendanceBatchFragmentDirections
                                        .actionAttendanceBatchFragmentToFacultyAttendance(
                                            (data.batchCode ?: "0").toString(),
                                            data.batchName ?: "Batch Name"
                                        )
                                binding.recyclerViewBatches.findNavController().navigate(action)
                            }
                        }
                    }
                    .show()
            }
        )

        binding.recyclerViewBatches.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBatches.adapter = batchAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectAttendanceBatchResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getAttendanceBatchAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()

                    is Resource.Error -> {
                        hideProgressBar()
                        showSnackBar("Internal Server Error111")
                        NoDataHelper.showNoData(
                            parent = binding.container,
                            title ="Internal Server Error111",
                            iconRes = R.drawable.no_data,
                        )
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        it.data?.let { apiData ->
                            if (apiData.responseCode == 200) {
                                NoDataHelper.hideNoData(binding.container)
                                AttendanceBatchList.clear()
                                AttendanceBatchList.addAll(apiData.wrappedList)
                                batchAdapter.update(apiData.wrappedList)
                                // refresh UI
                                batchAdapter.notifyDataSetChanged()

                            } else if (apiData.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else {
                                toastLong(apiData.responseDesc)
                                NoDataHelper.showNoData(
                                    parent = binding.container,
                                    title =apiData.responseDesc,
                                    iconRes = R.drawable.no_data,
                                )
                            }

                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }
}
