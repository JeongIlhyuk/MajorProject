package com.example.myapplication.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SignupViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Initial)
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading

            try {
                // 실제 구현에서는 이메일 중복 체크 등의 유효성 검사를 추가해야 함
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    inkAmount = 100.0,  // 기본 잉크량 100%
                    fountainPens = 0    // 초기 만년필 개수
                )

                userRepository.createUser(newUser, password)
                _uiState.value = SignupUiState.Success(newUser)
            } catch (e: Exception) {
                _uiState.value = SignupUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
            }
        }
    }
}

sealed class SignupUiState {
    object Initial : SignupUiState()
    object Loading : SignupUiState()
    data class Success(val user: User) : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}