package com.rsetiapp.common.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rsetiapp.common.fragments.AttendanceBatchFragmentDirections
import com.rsetiapp.common.model.response.AttendanceBatch
import com.rsetiapp.databinding.AttendanceBatchLayoutBinding

class AttendanceBatchAdapter ( private val batchList: List<AttendanceBatch>
) : RecyclerView.Adapter<AttendanceBatchAdapter.BatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding = AttendanceBatchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        val batch = batchList[position]
        holder.bind(batch)
    }

    override fun getItemCount(): Int = batchList.size

    inner class BatchViewHolder(private val binding: AttendanceBatchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(batch: AttendanceBatch) {
            binding.tvBatchIdName.text = batch.batchRegNumber
            binding.tvBatchName.text = batch.batchName  // Show Batch Name


        /*    // Handle Click
            binding.root.setOnClickListener {
                val data = batchList[adapterPosition]

                val action =
                    AttendanceBatchFragmentDirections.actionAttendanceBatchFragmentToAttendanceCandidateFragment(
                        (data.batchCode ?: "0").toString(), data.batchName ?: "Batch Name"
                    )
                binding.root.findNavController().navigate(action)
            }
*/
            binding.root.setOnClickListener {
                val data = batchList[adapterPosition]

                val options = arrayOf("Mark Attendance of Candidate", "Self Attendance")

                AlertDialog.Builder(binding.root.context)
                    .setTitle("Choose an option")
                    .setItems(options) { _, which ->
                        when (which) {
                            0 -> {
                                // Option 1: Navigate to candidate attendance screen
                                val action =
                                    AttendanceBatchFragmentDirections.actionAttendanceBatchFragmentToAttendanceCandidateFragment(
                                        (data.batchCode ?: "0").toString(), data.batchName ?: "Batch Name"
                                    )
                                binding.root.findNavController().navigate(action)
                            }
                            1 -> {
                                // Option 2: Navigate to self attendance screen (create this action in nav_graph)
                                val action =
                                    AttendanceBatchFragmentDirections.actionAttendanceBatchFragmentToFacultyAttendance(
                                        (data.batchCode ?: "0").toString(), data.batchName ?: "Batch Name"
                                    )
                                binding.root.findNavController().navigate(action)
                            }
                        }
                    }
                    .show()
            }

        }
    }
}
