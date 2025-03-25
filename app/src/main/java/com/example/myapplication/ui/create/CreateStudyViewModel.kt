package com.example.myapplication.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Study
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.StudyRepository
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateStudyViewModel(
    private val studyRepository: StudyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateStudyUiState>(CreateStudyUiState.Initial)
    val uiState: StateFlow<CreateStudyUiState> = _uiState.asStateFlow()

    fun createStudy(study: Study, currentUser: User) {
        viewModelScope.launch {
            _uiState.value = CreateStudyUiState.Loading

            try {
                // 필요한 검증 수행
                validateStudyCreation(study, currentUser)

                // 스터디 생성
                val createdStudy = studyRepository.createStudy(study)

                // 잉크량 감소 또는 만년필 사용 처리 (선택적)
                // 예: 스터디 개설 시 만년필 1개 사용
                if (currentUser.fountainPens > 0) {
                    val updatedUser = currentUser.copy(
                        fountainPens = currentUser.fountainPens - 1
                    )
                    userRepository.updateUser(updatedUser)
                }

                _uiState.value = CreateStudyUiState.Success(createdStudy)
            } catch (e: Exception) {
                _uiState.value = CreateStudyUiState.Error(e.message ?: "스터디 생성 중 오류가 발생했습니다")
            }
        }
    }

    private fun validateStudyCreation(study: Study, currentUser: User) {
        // 만년필 보유 여부 확인
        if (currentUser.fountainPens < 1) {
            throw IllegalStateException("스터디 개설에 필요한 만년필이 부족합니다")
        }

        // 잉크량 확인
        if (currentUser.inkAmount < 60.0) {
            throw IllegalStateException("스터디 개설에 필요한 잉크량(60%)이 부족합니다")
        }

        // 기타 필요한 검증 로직
        if (study.title.isBlank()) {
            throw IllegalArgumentException("스터디 제목을 입력해주세요")
        }

        if (study.maxParticipants < 2) {
            throw IllegalArgumentException("최소 2명 이상의 참가자를 설정해주세요")
        }
    }

    // UI 상태 리셋
    fun resetState() {
        _uiState.value = CreateStudyUiState.Initial
    }
}

sealed class CreateStudyUiState {
    object Initial : CreateStudyUiState()
    object Loading : CreateStudyUiState()
    data class Success(val study: Study) : CreateStudyUiState()
    data class Error(val message: String) : CreateStudyUiState()
}