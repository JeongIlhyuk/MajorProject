package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.data.model.Study
import com.example.myapplication.data.model.StudyType
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.signup.SignupScreen
import com.example.myapplication.ui.signup.SignupViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    // 임시로 레포지토리 직접 생성 (실제 구현에서는 DI 사용)
    private val userRepository = UserRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 임시로 ViewModel 직접 생성 (실제 구현에서는 ViewModelProvider 사용)
        val signupViewModel = SignupViewModel(userRepository)

        setContent {
            MyApplicationTheme {
                // 화면 상태 관리
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

                // 임시 사용자 (로그인 구현 전까지 사용)
                val dummyUser = User(
                    id = "user1",
                    name = "김홍래",
                    email = "ghdfo0711@naver.com",
                    inkAmount = 87.5,
                    fountainPens = 3
                )

                // 임시 스터디 목록 (스터디 리포지토리 구현 전까지 사용)
                val dummyStudies = listOf(
                    Study(
                        title = "알고리즘 스터디",
                        description = "코딩 테스트 대비 스터디",
                        studyType = StudyType.OFFLINE,
                        location = "서울대입구역 카페",
                        subject = "컴퓨터 공학",
                        maxParticipants = 6,
                        currentParticipants = 4,
                        requiredInkAmount = 70.0,
                        penDeposit = 2,
                        startDate = Date()
                    ),
                    Study(
                        title = "토익 스터디",
                        description = "토익 800점 목표",
                        studyType = StudyType.ONLINE,
                        subject = "어학",
                        maxParticipants = 10,
                        currentParticipants = 7,
                        requiredInkAmount = 60.0,
                        penDeposit = 1,
                        startDate = Date()
                    )
                )

                when (val screen = currentScreen) {
                    is Screen.Login -> {
                        LoginScreen(
                            onLoginClick = {
                                // 실제 로그인 로직 (나중에 구현)
                                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                                // 성공 시 홈 화면으로 이동
                                currentScreen = Screen.Home
                            },
                            onSignupClick = {
                                // 회원가입 화면으로 이동
                                currentScreen = Screen.Signup
                            }
                        )
                    }

                    is Screen.Signup -> {
                        SignupScreen(
                            onSignupClick = { name, email, password ->
                                // 회원가입 로직 구현
                                signupViewModel.signUp(name, email, password)
                                Toast.makeText(this, "회원가입 성공! ${name}님 환영합니다", Toast.LENGTH_SHORT).show()
                                // 로그인 화면으로 돌아가기
                                currentScreen = Screen.Login
                            },
                            onLoginClick = {
                                // 로그인 화면으로 돌아가기
                                currentScreen = Screen.Login
                            }
                        )
                    }

                    is Screen.Home -> {
                        HomeScreen(
                            currentUser = dummyUser,
                            nearbyStudies = dummyStudies,
                            onStudyClick = { study ->
                                // 스터디 상세 화면 이동 (구현 예정)
                                Toast.makeText(this, "${study.title} 선택됨", Toast.LENGTH_SHORT).show()
                            },
                            onCreateStudyClick = {
                                // 스터디 생성 화면 이동 (구현 예정)
                                Toast.makeText(this, "스터디 생성 화면 (준비 중)", Toast.LENGTH_SHORT).show()
                            },
                            onProfileClick = {
                                // 프로필 화면 이동 (구현 예정)
                                Toast.makeText(this, "프로필 화면 (준비 중)", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

// 앱의 화면을 나타내는 sealed class
sealed class Screen {
    object Login : Screen()
    object Signup : Screen()
    object Home : Screen()
    // 나중에 다른 화면들 추가 예정
}