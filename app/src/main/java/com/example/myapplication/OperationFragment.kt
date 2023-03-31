package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class OperationFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_left, container, false)
        val args = this.arguments

        operations = if (args == null) operations else args.getStringArrayList("operations") as ArrayList<String>

        renderView(view)

        return view
    }

    var operations= arrayListOf<String>("+", "-", "*", "/")

    fun renderView(view: View) {
        val listOptions = view.findViewById<ListView>(R.id.operationsList)
        listOptions.adapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_list_item_1, operations)
        listOptions.setOnItemClickListener { parent, view, position, id ->
            val bundle = Bundle()
            bundle.putString("operation", operations[position])

            val taskFragment = TaskFragment()
            taskFragment.arguments = bundle

            fragmentManager?.beginTransaction()?.replace(R.id.fragment_right, taskFragment)?.commit()
        }
    }
}