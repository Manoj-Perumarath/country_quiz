package com.manoj.countryquiz.data.model

data class Question(
    val answer_id: Int,
    val countries: List<Country>,
    val country_code: String
)
