package org.vernality.profitclub.view.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_of_creating_action_or_offer.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment


class ActionOrOfferCreatingFragment : Fragment() {


    private lateinit var createOfferBTN: MaterialButton
    private lateinit var createActionBTN: MaterialButton
    private lateinit var successResultDialog: SuccessResultDialogFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_of_creating_action_or_offer, container, false)

        initViews(view)

        return view
    }


    fun initViews(view: View){
        createOfferBTN = view.btn_create_offer
        createActionBTN = view.btn_create_action

        createOfferBTN.setOnClickListener{ navigateToFragmentOnCreatingOffer() }
        createActionBTN.setOnClickListener{ navigateToFragmentOnCreatingAction() }

        if(checkingFirstLoginToAccountOfThisOrganization()) showSuccessDialog()

    }


    private fun showSuccessDialog(){
        successResultDialog =
            SuccessResultDialogFragment.newInstance(
                TypeDialogFragment.RequestApprovedAdmin
            ) {}

        successResultDialog.show(parentFragmentManager, this.toString())
    }



    private fun navigateToFragmentOnCreatingOffer(){
        Toast.makeText(requireActivity(), "the button for creating a commercial offer is clicked", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_creating_action_or_offer_to_creating_offer)
    }

    private fun navigateToFragmentOnCreatingAction(){

        Toast.makeText(requireActivity(), "the action creation button is clicked", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_creating_action_or_offer_to_creating_action)
    }


    private fun checkingFirstLoginToAccountOfThisOrganization():Boolean{
        //same code


        return true
    }


    companion object {
        fun newInstance(): ActionOrOfferCreatingFragment {
            val fragment = ActionOrOfferCreatingFragment()
            return fragment
        }
    }
}
