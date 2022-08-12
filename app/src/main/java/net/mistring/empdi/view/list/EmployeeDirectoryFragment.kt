package net.mistring.empdi.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.mistring.empdi.databinding.FragmentEmployeeDirectoryBinding
import net.mistring.empdi.view.EmployeeUiState
import net.mistring.empdi.view.SharedViewModel
import net.mistring.empdi.util.collectLatestLifecycleFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * The main fragment screen of the app. Shows Employee Directory and loading indicator.
 */
@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment() {

    private var _binding: FragmentEmployeeDirectoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

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

        viewModel.fetchEmployees()
    }

    private fun observeData() {

        // Observe changes to the (StateFlow) signal to show/hide the refreshing indicator
        collectLatestLifecycleFlow(viewModel.loaderStateFlow) { refreshing ->
            binding.employeeWrapper.isRefreshing = refreshing
        }

        // Observe changes to the (StateFlow) employee list:
        collectLatestLifecycleFlow(viewModel.uiState) { uiData ->
            when (uiData) {
                is EmployeeUiState.Success -> {
                    if (uiData.employees.isNotEmpty()) {
                        binding.employeeListEmpty.visibility = View.INVISIBLE
                        binding.employeeWrapper.visibility = View.VISIBLE
                        listAdapter.onAddEmployees(uiData.employees)
                    } else {
                        binding.employeeWrapper.visibility = View.INVISIBLE
                        binding.employeeListEmpty.visibility = View.VISIBLE
                    }
                }
                is EmployeeUiState.Error -> {
                    binding.employeeWrapper.visibility = View.INVISIBLE
                    binding.employeeListEmpty.visibility = View.VISIBLE
                    Timber.e("UI sees Error Condition; ${uiData.exception}")
                    Snackbar.make(requireView(), "Error fetching data", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}