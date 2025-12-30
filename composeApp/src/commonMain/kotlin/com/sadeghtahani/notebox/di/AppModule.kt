package com.sadeghtahani.notebox.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sadeghtahani.notebox.features.notes.data.local.database.AppDatabase
import com.sadeghtahani.notebox.features.notes.data.local.database.getDatabaseBuilder
import com.sadeghtahani.notebox.features.notes.data.repo.NoteRepositoryImpl
import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository
import com.sadeghtahani.notebox.features.notes.domain.usecase.DeleteNoteUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNoteByIdUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNotesUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.SaveNoteUseCase
import com.sadeghtahani.notebox.features.notes.presentation.detail.NoteDetailViewModel
import com.sadeghtahani.notebox.features.notes.presentation.list.NoteListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database Instance
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    // DAO
    single { get<AppDatabase>().noteDao() }

    // Repository
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    // UseCases
    factory { GetNotesUseCase(get()) }
    factory { GetNoteByIdUseCase(get()) }
    factory { SaveNoteUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }

    // ViewModels
    viewModel { NoteListViewModel(get()) }
    viewModel { (noteId: Long?) ->
        NoteDetailViewModel(noteId, get(), get(), get())
    }
}