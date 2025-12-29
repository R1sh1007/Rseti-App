package com.rsetiapp.common.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsetiapp.R
import com.rsetiapp.common.model.response.CandidateSettlementVerificationDetail
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.databinding.ItemCandidateDetailsBinding
import com.rsetiapp.databinding.ItemSettletedCandidateDetailsBinding
import kotlin.collections.mutableListOf

//List<CandidateSettlementVerificationDetail>
class SettlementVeryficationDetailsAdapter(
    private val candidateList: List<CandidateSettlementVerificationDetail>
//    mutableListOf<CandidateSettlementVerificationDetail>()
) : RecyclerView.Adapter<SettlementVeryficationDetailsAdapter.CandidateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding =
            ItemSettletedCandidateDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidateList[position]
        holder.bind(candidate)
    }

    override fun getItemCount(): Int = candidateList.size

    inner class CandidateViewHolder(private val binding: ItemSettletedCandidateDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        fun bind(candidate: CandidateSettlementVerificationDetail?) {
            fun bind(candidate: CandidateSettlementVerificationDetail?) {
                if (candidate == null) {
                    return // Prevents NullPointerException
                }

                val context = binding.root.context

                // Handle Profile Picture
//                val profilePic = candidate.candidateProfilePic
//                if (profilePic.isNullOrEmpty() || profilePic == "NA") {
//                    Glide.with(context)
//                        .load(R.drawable.person)
//                        .into(binding.candidateImage)
//                } else {
//                    try {
//                        val decodedString: ByteArray = Base64.decode(profilePic, Base64.DEFAULT)
//                        val profileBitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//
//                        Glide.with(context)
//                            .load(profileBitmap)
//                            .into(binding.candidateImage)
//                    } catch (e: Exception) {
//                        Glide.with(context)
//                            .load(R.drawable.person) // Load default image if decoding fails
//                            .into(binding.candidateImage)
//                    }
//                }

    Glide.with(context)
        .load(R.drawable.person) // Load default image if decoding fails
        .into(binding.candidateImage)
                binding.tvCandidateName.text = candidate.candidateName
                binding.tvRollNumberValue.text = candidate.rollNo.toString()
                binding.tvContactNumber.text = candidate.mobileNo
//                binding.tvSettlementStatus.text = decodeFollowUpStatus(candidate.sattleStatus ?: "1")
                displayStatus(binding.followUp1Status, (candidate.quarterOne ?: "1").toString())
                displayStatus(binding.followUp2Status, (candidate.quarterTwo ?: "1").toString())
                displayStatus(binding.followUp3Status, (candidate.quarterThree ?: "1").toString())
                displayStatus(binding.followUp4Status, (candidate.quarterFour ?: "1").toString())
                displayStatus(binding.followUp5Status, (candidate.quarterFive ?: "1").toString())
                displayStatus(binding.followUp6Status, (candidate.quarterSix ?: "1").toString())
                displayStatus(binding.followUp7Status, (candidate.quarterSeven ?: "1").toString())
                displayStatus(binding.followUp8Status, (candidate.quarterEight ?: "1").toString())


                // Handle Click Navigation (Ensure safe `adapterPosition`)
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    val quat1 = candidateList[position].quarterOne
                    val quat2 = candidateList[position].quarterTwo
                    val quat3 = candidateList[position].quarterThree
                    val quat4= candidateList[position].quarterFour
                    val quat5 = candidateList[position].quarterFive
                    val quat6 = candidateList[position].quarterSix
                    val quat7 = candidateList[position].quarterSeven
                    val quat8= candidateList[position].quarterEight
                    if (quat1.toString()=="2" || quat2.toString()=="2" || quat3.toString()=="2" || quat4.toString()=="2" || quat5.toString()=="2" || quat6.toString()=="2" || quat7.toString()=="2" || quat8.toString()=="2")
                    {

//                        if (position != RecyclerView.NO_POSITION && position < candidateList.size) {
//                            val data = candidateList[position]

//                            val action = FollowUpCandidateFragmentDirections
//                                .actionFollowUpCandidateFragmentToFollowUpFormFragment(data)
//
//                            binding.root.findNavController().navigate(action)

//                            if (position != RecyclerView.NO_POSITION && position < candidateList.size) {
//                                val data = candidateList[position]
////SettlementVeryficationFormFragment
//                                val action = FollowUpBatchFragmentDirections.actionSettlementFollowUpFormFragment(data)
//                                binding.root.findNavController().navigate(action)
////                            }
//
//                        }

                    }

                    else{

                        AppUtil.showAlertDialog(context,"Alert","Followup has been already done. Please wait until next quarter. ")
                    }

                }
            }

            private fun decodeFollowUpStatus(status: String): String = when (status) {
                "1" -> "Settlement In Progress"
                "2" -> "Settled"
                "3" -> "Unsettled"
                else -> "N/A"
            }

            private fun displayStatus(statusView: ImageView, status: String) {
                statusView.setColorFilter(
                    ContextCompat.getColor(
                        statusView.context, when (status) {
                            "2" -> R.color.yellow
                            "3" -> R.color.color_follow_up_status
                            "4" -> R.color.color_red
                            else -> R.color.color_grey
                        }
                    )
                )
            }
        }
    }
