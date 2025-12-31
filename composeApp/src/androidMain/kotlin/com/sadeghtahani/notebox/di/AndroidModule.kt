package com.sadeghtahani.notebox.di

import com.sadeghtahani.notebox.features.notes.data.service.AndroidFileSaver
import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import org.koin.dsl.module

val androidModule = module {
    single<FileSaver> { AndroidFileSaver(get()) }
}
