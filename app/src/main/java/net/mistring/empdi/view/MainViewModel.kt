package net.mistring.empdi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivint.coroutines_sample.model.EmployeeEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import net.mistring.empdi.view.data.EmployeeRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * The ViewModel:
 * - handles requests from the UI (View: Activity, Fragment, etc)
 * - prepares data from the Repository for UI (View) consumption
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    private val _loaderStateFlow = MutableStateFlow(false)
    val loaderStateFlow = _loaderStateFlow.asStateFlow()

    private val _employeeListStateFlow =
        MutableStateFlow<List<EmployeeEntry>>(emptyList())
    val employeeListStateFlow = _employeeListStateFlow.asStateFlow()

    init {
        fetchEmployees()
    }

    fun fetchEmployees() {

        // Signal that the Loading should be visible
        _loaderStateFlow.value = true

        viewModelScope.launch {
            repository.getEmployeeDirectory()
                .onCompletion {
                    // Signal that the Loading should disappear now
                    _loaderStateFlow.value = false
                }
                .collectLatest { result ->
                    if (result.isSuccess) {
                        result.getOrNull()?.let { employees ->
                            _employeeListStateFlow.value = employees
                        }
                    } else {
                        //handle failure in UI as necessary
                        Timber.e("Failed to fetch employee list")
                        _employeeListStateFlow.value = emptyList()
                    }
                }
        }
    }

}