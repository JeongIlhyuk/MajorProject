package com.example.myapplication.ui.create

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.Study
import com.example.myapplication.data.model.StudyType
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    onCreateClick: (Study) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // 스터디 정보 상태 관리
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedStudyType by remember { mutableStateOf(StudyType.OFFLINE) }
    var location by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var maxParticipants by remember { mutableIntStateOf(10) }
    var requiredInkAmount by remember { mutableStateOf(50.0) }
    var penDeposit by remember { mutableIntStateOf(1) }
    var startDate by remember { mutableStateOf(Date()) }

    // 날짜 선택을 위한 DatePicker 설정
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            startDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // 날짜 포맷
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("스터디 만들기") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 스터디 제목
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("스터디 제목") },
                modifier = Modifier.fillMaxWidth()
            )

            // 스터디 설명
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("스터디 설명") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // 스터디 유형 선택 드롭다운
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when(selectedStudyType) {
                        StudyType.OFFLINE -> "대면 스터디"
                        StudyType.ONLINE -> "비대면 스터디"
                        StudyType.MEETUP -> "약속/단순 모임"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("스터디 유형") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("대면 스터디") },
                        onClick = {
                            selectedStudyType = StudyType.OFFLINE
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("비대면 스터디") },
                        onClick = {
                            selectedStudyType = StudyType.ONLINE
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("약속/단순 모임") },
                        onClick = {
                            selectedStudyType = StudyType.MEETUP
                            expanded = false
                        }
                    )
                }
            }

            // 대면 스터디일 경우 위치 입력 필드 표시
            if (selectedStudyType == StudyType.OFFLINE || selectedStudyType == StudyType.MEETUP) {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("위치") },
                    leadingIcon = {
                        Icon(Icons.Filled.LocationOn, contentDescription = "위치")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 스터디 주제/분야
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("스터디 주제/분야") },
                modifier = Modifier.fillMaxWidth()
            )

            // 최대 참가자 수
            Text("최대 참가자 수: $maxParticipants 명")
            Slider(
                value = maxParticipants.toFloat(),
                onValueChange = { maxParticipants = it.toInt() },
                valueRange = 2f..20f,
                steps = 18
            )

            // 필요한 잉크량
            Text("참여에 필요한 최소 잉크량: ${"%.1f".format(requiredInkAmount)}%")
            Slider(
                value = requiredInkAmount.toFloat(),
                onValueChange = { requiredInkAmount = it.toDouble() },
                valueRange = 0f..100f
            )

            // 만년필 보증금
            OutlinedTextField(
                value = penDeposit.toString(),
                onValueChange = {
                    penDeposit = it.toIntOrNull() ?: 0
                },
                label = { Text("만년필 보증금 (개)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 시작 날짜
            OutlinedTextField(
                value = dateFormat.format(startDate),
                onValueChange = { },
                readOnly = true,
                label = { Text("시작 날짜") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "날짜 선택")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 스터디 생성 버튼
            Button(
                onClick = {
                    val newStudy = Study(
                        title = title,
                        description = description,
                        studyType = selectedStudyType,
                        location = location,
                        subject = subject,
                        maxParticipants = maxParticipants,
                        currentParticipants = 1, // 스터디 생성자 포함
                        requiredInkAmount = requiredInkAmount,
                        penDeposit = penDeposit,
                        startDate = startDate
                    )
                    onCreateClick(newStudy)
                },
                enabled = title.isNotEmpty() &&
                        (selectedStudyType != StudyType.OFFLINE || location.isNotEmpty()) &&
                        subject.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("스터디 생성하기")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateStudyScreenPreview() {
    MyApplicationTheme {
        CreateStudyScreen()
    }
}