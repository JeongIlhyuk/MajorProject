package com.example.myapplication.data.model

import java.util.Date
import java.util.UUID

enum class StudyType {
    OFFLINE, // 대면
    ONLINE,  // 비대면
    MEETUP   // 단순 약속
}

data class Study(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val studyType: StudyType = StudyType.OFFLINE,
    val location: String = "",  // 대면 스터디인 경우 위치
    val subject: String = "",  // 스터디 주제/분야
    val maxParticipants: Int = 10,
    val currentParticipants: Int = 0,
    val requiredInkAmount: Double = 0.0, // 참여에 필요한 최소 잉크량
    val penDeposit: Int = 0,   // 필요한 만년필 보증금
    val startDate: Date = Date()
)