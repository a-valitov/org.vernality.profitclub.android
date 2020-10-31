package org.vernality.profitclub.di

import org.vernality.profitclub.rx.SchedulerProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.view_model.EnterRoleDataViewModel
import org.vernality.profitclub.view_model.RegistrationViewModel
import org.vernality.profitclub.view_model.RoleSelectViewModel

val application = module {


    single {
        SchedulerProvider()
    }

    factory {
        RepositoryImplementation()
    }

}

val viewModelDependency = module {

    viewModel {
        RegistrationViewModel(androidApplication())
    }

    viewModel {
        RoleSelectViewModel(androidApplication())
    }

    viewModel {
        EnterRoleDataViewModel(androidApplication())
    }

}

