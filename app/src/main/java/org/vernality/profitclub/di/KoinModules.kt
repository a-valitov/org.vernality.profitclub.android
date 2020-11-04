package org.vernality.profitclub.di

import org.vernality.profitclub.rx.SchedulerProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.vernality.profitclub.model.datasource.ParseImplementation
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.MyPreferences
import org.vernality.profitclub.view_model.EnterRoleActivityViewModel
import org.vernality.profitclub.view_model.EnterRoleDataViewModel
import org.vernality.profitclub.view_model.RegistrationViewModel
import org.vernality.profitclub.view_model.RoleSelectViewModel

val application = module {


    single {
        SchedulerProvider()
    }

    single {
        ParseImplementation()
    }

    factory {
        RepositoryImplementation(dataSource = get<ParseImplementation>())
    }

    single {
        MyPreferences(androidApplication())
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

    viewModel {
        EnterRoleActivityViewModel(androidApplication())
    }

}

