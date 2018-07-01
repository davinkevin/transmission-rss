package com.github.davinkevin.transmissionrss.batch.cleanup


data class MatchingRule(
        val matcher: String,
        val exclude: String?
)