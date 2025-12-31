package com.sadeghtahani.notebox.di

import com.sadeghtahani.notebox.features.notes.data.service.DesktopFileSaver
import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import org.koin.dsl.module

val desktopModule = module {
    single<FileSaver> { DesktopFileSaver() }
}
