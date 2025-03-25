package com.example.myapplication.data.repository

import com.example.myapplication.data.model.User

/**
 * 사용자 데이터 관리를 위한 리포지토리 인터페이스
 */
interface UserRepository {
    suspend fun createUser(user: User, password: String): User
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User): User
    suspend fun deleteUser(userId: String)
    suspend fun login(email: String, password: String): User?
}

/**
 * UserRepository의 실제 구현체 (Firebase 또는 Room DB 등으로 구현 예정)
 * 현재는 임시 구현으로 메모리에 저장
 */
class UserRepositoryImpl : UserRepository {
    // 임시 메모리 저장소 (실제 구현에서는 Firebase 또는 Room DB로 대체)
    private val users = mutableMapOf<String, User>()
    private val passwords = mutableMapOf<String, String>() // 실제 구현에서는 해싱된 비밀번호 저장

    override suspend fun createUser(user: User, password: String): User {
        // 이메일 중복 체크
        val existingUser = getUserByEmail(user.email)
        if (existingUser != null) {
            throw IllegalArgumentException("이미 등록된 이메일입니다")
        }

        // 사용자 저장
        users[user.id] = user
        passwords[user.id] = password // 실제 구현에서는 비밀번호 해싱 필요

        return user
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.values.find { it.email == email }
    }

    override suspend fun updateUser(user: User): User {
        if (!users.containsKey(user.id)) {
            throw IllegalArgumentException("존재하지 않는 사용자입니다")
        }

        users[user.id] = user
        return user
    }

    override suspend fun deleteUser(userId: String) {
        users.remove(userId)
        passwords.remove(userId)
    }

    override suspend fun login(email: String, password: String): User? {
        val user = getUserByEmail(email) ?: return null

        val storedPassword = passwords[user.id] ?: return null

        return if (storedPassword == password) { // 실제 구현에서는 해시 비교
            user
        } else {
            null
        }
    }
}