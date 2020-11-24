package org.vernality.profitclub.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_list_org_for_member_recyclerview.view.*
import kotlinx.android.synthetic.main.item_processing_list_recyclerview.view.*
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.view_model.MyOrganizationsListFragmentViewModel.*

class MyRolesListDataAdapter(
    private val inflater: LayoutInflater,
    private val rootLayoutOrganizations: LinearLayout,
    private val idItemOrganization: Int,
    private val rootLayoutSuppliers: LinearLayout,
    private val idItemSupplier: Int,
    private val rootLayoutMembers: LinearLayout,
    private val idItemMember: Int,
    private val data: MyOrganizationsData,
    private val listenerForOrganization: OnListItemClickListener<Organization>,
    private val listenerForSupplier: OnListItemClickListener<Supplier>,
    private val listenerForMember: OnListItemClickListener<Organization>
) {


    fun inflateViews(){
        inflateListOfOrganizations()
        inflateListOfSuppliers()
        inflateListOfMembers()

    }

    private fun inflateListOfOrganizations(){
        val isEmptyList = (data.dataOrg.isEmpty())
        if(isEmptyList){
            rootLayoutOrganizations.visibility = View.GONE
        }else {
            data.dataOrg.forEach {
                val view = inflater.inflate(idItemOrganization, rootLayoutOrganizations, false)
                setDataInItemOrganization(view, it)
            }
        }
    }

    private fun setDataInItemOrganization(view: View, organization: Organization){
            view.tv_name_of_organization.setText(organization.name)
            view.tv_INN_value.setText(organization.inn)
            view.tv_contact_person_value.setText(organization.contactName)
            view.tv_phone_value.setText(organization.phone)
            view.tv_status.setText(organization.statusString)
            view.setOnClickListener { openMyOrganization(organization) }
            rootLayoutOrganizations.addView(view)

    }

    private fun inflateListOfSuppliers(){
        val isEmptyList = (data.dataSup.isEmpty())
        if(isEmptyList){
            rootLayoutSuppliers.visibility = View.GONE
        }else {
            data.dataSup.forEach {
                val view = inflater.inflate(idItemSupplier, rootLayoutOrganizations, false)
                setDataInItemSupplier(view, it)
            }
        }
    }

    private fun setDataInItemSupplier(view: View, supplier: Supplier){
            view.tv_name_of_organization.setText(supplier.name)
            view.tv_INN_value.setText(supplier.inn)
            view.tv_contact_person_value.setText(supplier.contactName)
            view.tv_phone_value.setText(supplier.phone)
            view.tv_status.setText(supplier.statusString)
            view.setOnClickListener { openMySupplier(supplier) }
            rootLayoutSuppliers.addView(view)
    }

    private fun inflateListOfMembers(){
        val isEmptyList = (data.dataMem.isEmpty())
        if(isEmptyList){
            rootLayoutMembers.visibility = View.GONE
        }else {
            data.dataMem.forEach {
                val view = inflater.inflate(idItemMember, rootLayoutMembers, false)
                setDataInItemMember(view, it)
            }
        }
    }

    private fun setDataInItemMember(view: View, organization: Organization){
        view.tv_name_of_organization_member.setText(organization.name)
        view.tv_phone_value_member.setText(organization.phone)
        view.tv_contact_person_value_member.setText(organization.contactName)
        view.setOnClickListener { openMyMember(organization) }
        rootLayoutSuppliers.addView(view)
    }

    private fun openMyOrganization(organization: Organization) {
        listenerForOrganization.onItemClick(organization)
    }

    private fun openMySupplier(supplier: Supplier) {
        listenerForSupplier.onItemClick(supplier)
    }

    private fun openMyMember(organization: Organization) {
        listenerForMember.onItemClick(organization)
    }

    interface OnListItemClickListener<T> {
        fun onItemClick(unit: T)
    }


}