package com.example.myapplication.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val inkAmount: Double = 100.0,  // 기본 잉크량 100%
    val fountainPens: Int = 0       // 보유 만년필 수
)