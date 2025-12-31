package com.sadeghtahani.notebox.di

import com.sadeghtahani.notebox.features.notes.data.service.IosFileSaver
import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import org.koin.dsl.module

val iOSModule = module {
    single<FileSaver> { IosFileSaver() }
}
