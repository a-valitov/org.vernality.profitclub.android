package org.vernality.profitclub.model.repository

import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.User


class RepositoryImplementation() :
    Repository<List<Organization>> {

    override fun getData(user: User): ArrayList<Organization> {
        return arrayListOf(
            Organization("1", 0, "ООО \" Газпромефть\" ", "123456445", "Ivanov A.E.", "+79168653890", "Поставщик"),
            Organization("2", 1, "ЗАО \" НефтьГазАлмаз\" ", "765345768", "Dudko S.A.", "+79456653890", "Организация"),
            Organization("3", 2, "ПАО \" Газпром\" ", "3534545768", "Miller A.B.", "+7945198890", "Участник организации")

        )
    }
}
