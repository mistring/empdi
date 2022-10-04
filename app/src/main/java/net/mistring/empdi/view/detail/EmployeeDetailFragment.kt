package net.mistring.empdi.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.mistring.empdi.databinding.FragmentEmployeeDetailBinding
import net.mistring.empdi.util.collectLatestLifecycleFlow
import net.mistring.empdi.view.EmployeeUiState
import net.mistring.empdi.view.SharedViewModel
import timber.log.Timber

/**
 * The detail fragment screen of the app. Shows Employee Directory and loading indicator.
 */
@AndroidEntryPoint
class EmployeeDetailFragment : Fragment() {

    private var _binding: FragmentEmployeeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    val args: EmployeeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = args.userId

        Timber.d("userId=$userId")

        // Observe changes to the (StateFlow) employee list:
        collectLatestLifecycleFlow(viewModel.uiState) { uiData ->
            when (uiData) {
                is EmployeeUiState.UserSuccess -> {
                    val user = uiData.employee

                    Timber.d("user=$user")

                    binding.empName.text = user.fullName

                    Glide.with(this)
                        .load(user.photoUrlLarge)
                        .into(binding.empImageLarge)

                }
                else -> {
                    Timber.e("UI sees Error Condition")
                    Snackbar.make(requireView(), "No user found", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.fetchEmployee(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}