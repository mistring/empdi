package net.mistring.empdi.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivint.coroutines_sample.util.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import net.mistring.empdi.databinding.FragmentEmployeeDirectoryBinding
import net.mistring.empdi.view.MainViewModel
import javax.inject.Inject

/**
 * The main fragment screen of the app. Shows Employee Directory and loading indicator.
 */
@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment() {

    private var _binding: FragmentEmployeeDirectoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var listAdapter: EmployeeDirectoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.employeeList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        binding.employeeWrapper.setOnRefreshListener {
            viewModel.fetchEmployees()
        }

        observeData()
    }

    private fun observeData() {

        // Observe changes to the (StateFlow) signal to show/hide the refreshing indicator
        collectLifecycleFlow(viewModel.loaderStateFlow) { refreshing ->
            binding.employeeWrapper.isRefreshing = refreshing
        }

        // Observe changes to the (StateFlow) employee list:
        collectLifecycleFlow(viewModel.employeeListStateFlow) { employees ->
            if (employees.isEmpty()) {
                binding.employeeWrapper.visibility = View.INVISIBLE
                binding.employeeListEmpty.visibility = View.VISIBLE
            } else {
                binding.employeeListEmpty.visibility = View.INVISIBLE
                binding.employeeWrapper.visibility = View.VISIBLE
                listAdapter.onAddEmployees(employees)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}