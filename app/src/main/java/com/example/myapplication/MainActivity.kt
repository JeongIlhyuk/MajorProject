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
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.signup.SignupScreen
import com.example.myapplication.ui.signup.SignupViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

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

                when (val screen = currentScreen) {
                    is Screen.Login -> {
                        LoginScreen(
                            onLoginClick = {
                                // 실제 로그인 로직 (나중에 구현)
                                Toast.makeText(this, "로그인 기능은 아직 구현되지 않았습니다", Toast.LENGTH_SHORT).show()
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
                }
            }
        }
    }
}

// 앱의 화면을 나타내는 sealed class
sealed class Screen {
    object Login : Screen()
    object Signup : Screen()
    // 나중에 다른 화면들 추가 예정
}