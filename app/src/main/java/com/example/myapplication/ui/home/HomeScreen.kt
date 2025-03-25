package com.example.myapplication.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.Study
import com.example.myapplication.data.model.StudyType
import com.example.myapplication.data.model.User
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User = User(name = "사용자", inkAmount = 87.5, fountainPens = 3),
    nearbyStudies: List<Study> = emptyList(),
    onStudyClick: (Study) -> Unit = {},
    onCreateStudyClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smartee") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* 홈으로 이동 (현재 화면) */ },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "홈") },
                    label = { Text("홈") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* 스터디 탐색 화면으로 이동 */ },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "탐색") },
                    label = { Text("탐색") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onProfileClick,
                    icon = { Icon(Icons.Filled.Person, contentDescription = "프로필") },
                    label = { Text("프로필") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateStudyClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "스터디 생성",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // 사용자 프로필 요약 (예정)
                UserProfileSummary(user = currentUser)

                Spacer(modifier = Modifier.height(16.dp))

                // 주변 스터디 섹션 제목
                Text(
                    text = "주변 스터디",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // 주변 스터디 목록
            if (nearbyStudies.isEmpty()) {
                item {
                    Text(
                        text = "주변에 스터디가 없습니다.\n새로운 스터디를 만들어보세요!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )
                }
            } else {
                items(nearbyStudies) { study ->
                    StudyItem(study = study, onClick = { onStudyClick(study) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun UserProfileSummary(user: User) {
    // 간단한 사용자 정보 표시
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "${user.name}님, 안녕하세요!",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "잉크량: ${user.inkAmount}%, 만년필: ${user.fountainPens}개",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun StudyItem(study: Study, onClick: () -> Unit) {
    // 간단한 스터디 정보 표시
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = study.title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = when(study.studyType) {
                StudyType.OFFLINE -> "대면"
                StudyType.ONLINE -> "비대면"
                StudyType.MEETUP -> "약속"
            } + ", ${study.location}, ${study.currentParticipants}/${study.maxParticipants}명",
            style = MaterialTheme.typography.bodyMedium
        )

        // 날짜 표시
        val dateFormat = SimpleDateFormat("MM월 dd일", Locale.KOREA)
        Text(
            text = "시작일: ${dateFormat.format(study.startDate)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleUser = User(
        name = "김홍래",
        email = "ghdfo0711@naver.com",
        inkAmount = 87.5,
        fountainPens = 3
    )

    val sampleStudies = listOf(
        Study(
            title = "알고리즘 스터디",
            studyType = StudyType.OFFLINE,
            location = "서울대입구역 카페",
            maxParticipants = 6,
            currentParticipants = 4,
        ),
        Study(
            title = "토익 스터디",
            studyType = StudyType.ONLINE,
            maxParticipants = 10,
            currentParticipants = 7,
        )
    )

    MyApplicationTheme {
        HomeScreen(
            currentUser = sampleUser,
            nearbyStudies = sampleStudies
        )
    }
}