package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.koin.core.KoinComponent

abstract class BaseViewModel(appContext: Application) : AndroidViewModel(appContext), KoinComponent