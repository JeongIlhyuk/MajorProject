package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Study
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * 스터디 데이터 관리를 위한 리포지토리 인터페이스
 */
interface StudyRepository {
    suspend fun createStudy(study: Study): Study
    suspend fun getStudyById(studyId: String): Study?
    suspend fun updateStudy(study: Study): Study
    suspend fun deleteStudy(studyId: String)
    suspend fun joinStudy(studyId: String, userId: String): Boolean
    suspend fun leaveStudy(studyId: String, userId: String): Boolean
    fun getNearbyStudies(latitude: Double, longitude: Double, radiusKm: Double): Flow<List<Study>>
    fun getStudiesByUser(userId: String): Flow<List<Study>>
    fun getAllStudies(): Flow<List<Study>>
}

/**
 * StudyRepository의 실제 구현체 (현재는 메모리 기반, 나중에 Firebase로 변경 예정)
 */
class StudyRepositoryImpl : StudyRepository {
    // 임시 메모리 저장소
    private val _studies = MutableStateFlow<List<Study>>(emptyList())
    private val studies: StateFlow<List<Study>> = _studies.asStateFlow()

    // 참여자 관리 맵 (스터디 ID -> 참여자 ID 목록)
    private val participants = mutableMapOf<String, MutableList<String>>()

    override suspend fun createStudy(study: Study): Study {
        // 새 스터디 추가
        val currentStudies = _studies.value.toMutableList()
        currentStudies.add(study)
        _studies.value = currentStudies

        // 생성자를 첫 참여자로 등록 (실제 구현에서는 생성자 ID 활용)
        participants[study.id] = mutableListOf("creator_id")

        return study
    }

    override suspend fun getStudyById(studyId: String): Study? {
        return _studies.value.find { it.id == studyId }
    }

    override suspend fun updateStudy(study: Study): Study {
        val currentStudies = _studies.value.toMutableList()
        val index = currentStudies.indexOfFirst { it.id == study.id }

        if (index != -1) {
            currentStudies[index] = study
            _studies.value = currentStudies
            return study
        } else {
            throw IllegalArgumentException("존재하지 않는 스터디입니다")
        }
    }

    override suspend fun deleteStudy(studyId: String) {
        val currentStudies = _studies.value.toMutableList()
        val removed = currentStudies.removeIf { it.id == studyId }

        if (removed) {
            _studies.value = currentStudies
            participants.remove(studyId)
        } else {
            throw IllegalArgumentException("존재하지 않는 스터디입니다")
        }
    }

    override suspend fun joinStudy(studyId: String, userId: String): Boolean {
        // 스터디 조회
        val study = getStudyById(studyId) ?: return false

        // 현재 참여자 수 확인
        if (study.currentParticipants >= study.maxParticipants) {
            return false // 인원 초과
        }

        // 참여자 목록 업데이트
        val studyParticipants = participants[studyId] ?: mutableListOf()
        if (studyParticipants.contains(userId)) {
            return true // 이미 참여 중
        }

        studyParticipants.add(userId)
        participants[studyId] = studyParticipants

        // 스터디 참여자 수 증가
        val updatedStudy = study.copy(currentParticipants = studyParticipants.size)
        updateStudy(updatedStudy)

        return true
    }

    override suspend fun leaveStudy(studyId: String, userId: String): Boolean {
        // 스터디 조회
        val study = getStudyById(studyId) ?: return false

        // 참여자 목록 업데이트
        val studyParticipants = participants[studyId] ?: return false
        if (!studyParticipants.contains(userId)) {
            return false // 참여하지 않은 상태
        }

        studyParticipants.remove(userId)
        participants[studyId] = studyParticipants

        // 스터디 참여자 수 감소
        val updatedStudy = study.copy(currentParticipants = studyParticipants.size)
        updateStudy(updatedStudy)

        return true
    }

    override fun getNearbyStudies(latitude: Double, longitude: Double, radiusKm: Double): Flow<List<Study>> {
        // 실제 구현에서는 위치 기반 필터링 필요
        // 현재는 모든 스터디 반환
        return studies
    }

    override fun getStudiesByUser(userId: String): Flow<List<Study>> {
        return studies.map { studyList ->
            studyList.filter { study ->
                participants[study.id]?.contains(userId) ?: false
            }
        }
    }

    override fun getAllStudies(): Flow<List<Study>> {
        return studies
    }

    // 테스트 데이터 추가 메서드
    fun addSampleData(sampleStudies: List<Study>) {
        _studies.value = sampleStudies

        // 각 스터디에 참여자 초기화
        sampleStudies.forEach { study ->
            participants[study.id] = mutableListOf("creator_id")
        }
    }
}