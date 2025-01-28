package com.puzzle.profile.graph.register.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class RegisterProfileState(
    val nickName: String = "",
    val description: String = "",
    val birthdate: String = "",
    val location: String = "",
    val height: String = "",
    val weight: String = "",
    val job: String = "",
    val isSmoke: Boolean? = null,
    val isSnsActive: Boolean? = null,
    val contacts: List<Contact> = listOf(
        Contact(
            snsPlatForm = SnsPlatform.KAKAO,
            content = "",
        ),
    ),
) : MavericksState {}

enum class Location(val displayName: String) {
    SEOUL("서울특별시"),
    GYEONGGI("경기도"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    INCHEON("인천광역시"),
    GWANGJU("광주광역시"),
    DAEJEON("대전광역시"),
    ULSAN("울산광역시"),
    SEJONG("세종특별자치시"),
    GANGWON("강원도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEJU("제주특별자치도"),
    OTHER("기타");
}

enum class Job(val displayName: String) {
    STUDENT("학생"),
    EMPLOYEE("직장인"),
    EXPERT("전문직"),
    CIVIL_SERVANT("공무원"),
    BUSINESSMAN("사업가"),
    FREELANCER("프리랜서"),
    OTHER("기타");
}
