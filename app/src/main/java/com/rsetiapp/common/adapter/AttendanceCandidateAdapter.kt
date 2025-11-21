package com.rsetiapp.common.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsetiapp.R
import com.rsetiapp.common.fragments.AttendanceCandidateFragmentDirections
import com.rsetiapp.common.model.response.Candidate
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.databinding.AttendanceCandidateListAyoutBinding

class AttendanceCandidateAdapter(
    private val candidateList: List<Candidate>
) : RecyclerView.Adapter<AttendanceCandidateAdapter.CandidateViewHolder>() {

    var candidateId=""
    var rollNo=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding = AttendanceCandidateListAyoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidateList[position]
        holder.bind(candidate)
    }

    override fun getItemCount(): Int = candidateList.size

    inner class CandidateViewHolder(private val binding: AttendanceCandidateListAyoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(candidate: Candidate) {

            val context = binding.root.context

            // Handle Profile Picture
            val profilePic = candidate.candidateProfilePic ?: "NA"

            if (profilePic == "NA" || profilePic.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.person)
                    .into(binding.candidateImage)
            }  else {
                try {
                    val decodedString: ByteArray = Base64.decode(profilePic, Base64.DEFAULT)
                    val profileBitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                    Glide.with(context)
                        .load(profileBitmap)
                        .into(binding.candidateImage)
                } catch (e: Exception) {
                    Glide.with(context)
                        .load(R.drawable.person)
                        .into(binding.candidateImage)
                }
            }

            // Ensure non-null values for UI fields
            binding.tvCandidateName.text = candidate.candidateName ?: "Unknown"
            binding.tvRollNumberValue.text = candidate.rollNo.toString() ?: "N/A"
            binding.tvContactNumber.text = candidate.mobileNo ?: "N/A"
            binding.tvDate.text= AppUtil.getCurrentDateForAttendance()
            candidateId= candidate.candidateId
            rollNo= candidate.rollNo.toString()
            var aadhhaarNo= candidate.adhaarNo

            // Handle Click Navigation (Ensure safe `adapterPosition`)
            binding.btnMarkAttendance.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < candidateList.size) {
                    val profilePicSafe = candidate.candidateProfilePic ?: ""
                    val action = AttendanceCandidateFragmentDirections
                        .actionAttendanceCandidateFragmentToAttendanceFragment(candidate.candidateId,candidate.candidateName,candidate.mobileNo,candidate.emailId
                        ,candidate.gender,candidate.dateOfBirth,profilePicSafe,
                            candidate.batchId.toString(),candidate.rollNo.toString(),aadhhaarNo
                        )
                    binding.root.findNavController().navigate(action)
                }
            }
        }
    }
}
