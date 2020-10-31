package org.vernality.profitclub.view.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_data_processing.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.User
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.view.adapters.RequestRVAdapter
import org.vernality.profitclub.view_model.EnterRoleDataViewModel
import org.vernality.profitclub.view_model.Result


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [RoleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataProcessingFragment : Fragment() {

    val compos = CompositeDisposable()

    private val repo: RepositoryImplementation by inject<RepositoryImplementation>()

    private val viewModel by viewModel<EnterRoleDataViewModel>()

    lateinit var exitIV: ImageView
    lateinit var addIV: ImageView
    lateinit var settingsIV: ImageView
    lateinit var popupMenu:PopupMenu


    private val list = repo.getData(User("1","login"))
    private val adapter: RequestRVAdapter by lazy { RequestRVAdapter(list) }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_data_processing, container, false)



        init(root)

        initResultSendData()

        println("-------viewModel = " +viewModel)

//        setFields(viewModel.getFields())


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun init(root:View){
        addIV = root.iv_add
        settingsIV = root.iv_more
        initPopupMenu(settingsIV)
        root.rv_data_processing.layoutManager = LinearLayoutManager(requireContext())
        root.rv_data_processing.adapter = adapter

        addIV.setOnClickListener {
            Toast.makeText(requireActivity(), "add clicked", Toast.LENGTH_LONG).show()
        }
        settingsIV.setOnClickListener {
            popupMenu.show()
        }

    }


    private fun initPopupMenu(view: View){
        popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.inflate(R.menu.main_toolbar_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_menu_exit -> {
                    Toast.makeText(requireActivity(), "exit clicked", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }


    private fun initResultSendData(){
        viewModel.resultLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it == Result.Success) {
                Toast.makeText(requireActivity(), "Registration is success", Toast.LENGTH_LONG).show()

            }
            else {
                Toast.makeText(requireActivity(), "Registration is error", Toast.LENGTH_LONG).show()
            }
        })
    }







    private fun setListenerExit(exit: View) {
        exit.setOnClickListener {
            Toast.makeText(requireActivity(), "exit is checked", Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        compos.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu)

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataProcessingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}