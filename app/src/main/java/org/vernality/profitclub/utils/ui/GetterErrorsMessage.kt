package org.vernality.profitclub.utils.ui

object GetterErrorsMessage: GetterErrorsMessageI {

    override fun getErrorsMessage(code: Int): String {
        return ErrorsUtils.getErrorsMessage(code)
    }

}