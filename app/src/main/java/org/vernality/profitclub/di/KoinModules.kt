package org.vernality.profitclub.di

import org.vernality.profitclub.rx.SchedulerProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.vernality.profitclub.model.datasource.ParseImplementation
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.MyPreferences
import org.vernality.profitclub.interactors.MainInteractor
import org.vernality.profitclub.view.member.actions.ActionsForMemberPageViewModel
import org.vernality.profitclub.view.organization.members.MembersPageViewModel
import org.vernality.profitclub.view.organization.stocks.StocksPageViewModel
import org.vernality.profitclub.view.organization.supplies.SuppliesFragmentViewModel
import org.vernality.profitclub.view.supplier.ActionCreatingFragmentViewModel
import org.vernality.profitclub.view_model.*

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

    factory {
        MainInteractor(repository = get<RepositoryImplementation>())
    }

}

val viewModelDependency = module {

    viewModel {
        RegistrationFragmentViewModel(androidApplication())
    }

    viewModel {
        LoginFragmentViewModel(androidApplication())
    }

    viewModel {
        ResetPasswordFragmentViewModel(androidApplication())
    }

    viewModel {
        RoleSelectFragmentViewModel(androidApplication())
    }

    viewModel {
        EnterOrganizationDataFragmentViewModel(androidApplication())
    }

    viewModel {
        EnterMemberDataFragmentViewModel(androidApplication())
    }

    viewModel {
        SelectOrgForMemberFragmentViewModel(androidApplication())
    }

    viewModel {
        MembersPageViewModel(androidApplication())
    }

    viewModel {
        StocksPageViewModel(androidApplication())
    }

    viewModel {
        SuppliesFragmentViewModel(androidApplication())
    }

    viewModel {
        MyOrganizationsListFragmentViewModel(androidApplication())
    }

    viewModel {
        ActionsForMemberPageViewModel(androidApplication())
    }

    viewModel {
        ActionCreatingFragmentViewModel(androidApplication())
    }

}

