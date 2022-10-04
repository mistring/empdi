package net.mistring.empdi.view.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.mistring.empdi.R
import net.mistring.empdi.databinding.ListItemEmployeeBinding
import net.mistring.empdi.model.EmployeeEntry
import net.mistring.empdi.model.EmployeeType

class EmployeeDirectoryListAdapter(
    private val listener: (EmployeeEntry) -> Unit
) :
    RecyclerView.Adapter<EmployeeDirectoryListAdapter.EmployeeViewHolder>() {

    private val employeeList = arrayListOf<EmployeeEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = EmployeeViewHolder(
        ListItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = employeeList.size

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = employeeList[position]

        holder.imageView.loadImage(item.photoUrlSmall)
        holder.name.text = item.fullName
        holder.team.text = item.team
        val backgroundColor = when (item.employeeType) {
            EmployeeType.FULL_TIME -> R.color.fulltimeBkg
            EmployeeType.PART_TIME -> R.color.parttimeBkg
            EmployeeType.CONTRACTOR -> R.color.contractorBkg
            else -> R.color.white
        }
        holder.view.setBackgroundResource(backgroundColor)
        holder.rootView.setOnClickListener {
            listener(item)
        }
    }

    private fun ImageView.loadImage(uri: String?) {
        val options = RequestOptions()
            .placeholder(R.drawable.photo_placeholder)
        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onAddEmployees(employees: List<EmployeeEntry>) {
        employeeList.clear()
        employeeList.addAll(employees)
        notifyDataSetChanged()
    }

    inner class EmployeeViewHolder(binding: ListItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.employeeImage
        val name = binding.employeeName
        val team = binding.employeeTeam
        val view = binding.employeeItemRoot
        val rootView = binding.employeeItemRoot
    }
}