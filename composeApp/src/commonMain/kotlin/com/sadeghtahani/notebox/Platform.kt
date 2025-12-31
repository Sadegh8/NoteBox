package com.sadeghtahani.notebox

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun openFile(path: String)