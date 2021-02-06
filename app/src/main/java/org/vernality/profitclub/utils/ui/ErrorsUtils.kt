package org.vernality.profitclub.utils.ui

import android.content.Context
import org.koin.core.KoinComponent
import org.koin.core.get
import org.vernality.profitclub.R


class ErrorsUtils{
    companion object: KoinComponent, GetterErrorsMessageI {

        override fun getErrorsMessage(code: Int):String{

            val context: Context = get()

            when(code){

                101 -> return context.resources.getString(R.string._101)
                122 -> return context.resources.getString(R.string._122)
                129 -> return context.resources.getString(R.string._129)
                130 -> return context.resources.getString(R.string._130)
                153 -> return context.resources.getString(R.string._153)
                161 -> return context.resources.getString(R.string._161)
                200 -> return context.resources.getString(R.string._200)
                201 -> return context.resources.getString(R.string._201)
                202 -> return context.resources.getString(R.string._202)
                203 -> return context.resources.getString(R.string._203)
                204 -> return context.resources.getString(R.string._204)
                205 -> return context.resources.getString(R.string._205)
                206 -> return context.resources.getString(R.string._206)
                207 -> return context.resources.getString(R.string._207)
                208 -> return context.resources.getString(R.string._208)
                209 -> return context.resources.getString(R.string._209)
                100 -> return context.resources.getString(R.string._100)
                124 -> return context.resources.getString(R.string._124)
                -1 -> return context.resources.getString(R.string._minus1)
                1 -> return context.resources.getString(R.string._1)
                2 -> return context.resources.getString(R.string._2)
                3 -> return context.resources.getString(R.string._3)
                70 -> return context.resources.getString(R.string._70)
                else -> return ""

            }

            return ""

        }

    }


}





