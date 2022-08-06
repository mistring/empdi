package net.mistring.empdi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import net.mistring.empdi.databinding.FragmentEmployeeDirectoryBinding

/**
 * The main fragment screen of the app. Shows Employee Directory
 */
@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment() {

    private var _binding: FragmentEmployeeDirectoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //coming soon
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}