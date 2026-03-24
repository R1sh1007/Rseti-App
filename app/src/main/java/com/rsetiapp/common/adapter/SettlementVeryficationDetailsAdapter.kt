package com.rsetiapp.common.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsetiapp.R
import com.rsetiapp.common.MySattelementBottomSheet
import com.rsetiapp.common.model.response.CandidateSettlementVerificationDetail
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.AppUtil.getCandidateListPreference
import com.rsetiapp.core.util.AppUtil.saveCandidateListPreference
import com.rsetiapp.databinding.ItemCandidateDetailsBinding
import com.rsetiapp.databinding.ItemSettletedCandidateDetailsBinding
import kotlin.collections.mutableListOf



class SettlementVeryficationDetailsAdapter(
    private val candidateList: List<CandidateSettlementVerificationDetail>,
    private val onItemClick: (CandidateSettlementVerificationDetail) -> Unit
) : RecyclerView.Adapter<SettlementVeryficationDetailsAdapter.CandidateViewHolder>() {

    inner class CandidateViewHolder(val binding: ItemSettletedCandidateDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate:
                 CandidateSettlementVerificationDetail) {
//            binding.tvCandidateName.text = candidate.candidateName ?: "N/A"

            val context = binding.root.context

            Glide.with(context)
                .load(R.drawable.person) // Load default image if decoding fails
                .into(binding.candidateImage)

            binding.tvCandidateName.text = candidate.candidateName
            binding.tvRollNumberValue.text = candidate.rollNo.toString()
            binding.tvContactNumber.text = candidate.mobileNo
            binding.root.setOnClickListener {
                onItemClick(candidate)
            }



        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding = ItemSettletedCandidateDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.bind(candidateList[position])
    }

    override fun getItemCount() = candidateList.size
}


