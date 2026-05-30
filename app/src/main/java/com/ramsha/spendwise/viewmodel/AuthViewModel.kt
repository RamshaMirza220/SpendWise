package com.ramsha.spendwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: FirebaseUser? get() = auth.currentUser

    val isLoggedIn: Boolean get() = auth.currentUser != null

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = e.message ?: "Signup failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = e.message ?: "Login failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun clearError() {
        _uiState.value = AuthUiState()
    }
}
