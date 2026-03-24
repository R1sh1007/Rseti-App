import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rsetiapp.common.fragments.HomeFragmentDirections
import com.rsetiapp.common.model.response.Form
import com.rsetiapp.databinding.ItemChildBinding

class ChildAdapter(
    private val formList: List<Form>
) : RecyclerView.Adapter<ChildAdapter.FormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val binding = ItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val form = formList[position]
        holder.bind(form)
    }

    override fun getItemCount(): Int = formList.size

    inner class FormViewHolder(private val binding: ItemChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(form: Form) {
            binding.tvFormName.text = form.formName

            // Click to navigate to another fragment
            binding.root.setOnClickListener {
                when (form.formCd) {





                    "ADD_EAP" -> {
                        val action = HomeFragmentDirections.actionHomeFrahmentToEapListFragment(
//                        val action = HomeFragmentDirections.actionHomeFrahmentToSettlementFragment(
                            form.formName
                        )
                        binding.root.findNavController().navigate(action)
                    }

                    "FOLLOW_UP" -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToFollowUpBatchFragment(
                                formName = form.formName
                            )
                        binding.root.findNavController().navigate(action)
                    }

                    "CANDIDATEATTENDANCE" -> {

                        val action = HomeFragmentDirections.actionHomeFrahmentToAttendanceBatchFragment(
                            form.formName
                        )
                        binding.root.findNavController().navigate(action)

                    }
                    "SDR_VISIT" -> {

                        val action = HomeFragmentDirections.actionHomeFrahmentToSdrListFragment(
                            form.formName
                        )
                        binding.root.findNavController().navigate(action)

                    }
                    "SETTLEMENT_VERIFICATION" -> {
                        val action = HomeFragmentDirections.actionSettlementVeryficationBatchFragment(
                            form.formName
                        )
                        binding.root.findNavController().navigate(action)




//                        val action = HomeFragmentDirections.(
//                            form.formName
//                        )
//                        binding.root.findNavController().navigate(action)

                    }





                }
            }
        }
    }
}































