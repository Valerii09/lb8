package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.pow

class TaskFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_right, container, false)
        val args = this.arguments

        val bundle = Bundle()
        bundle.putStringArrayList("operations", ArrayList(operations.keys))

        val operationsFragment = OperationFragment()
        operationsFragment.arguments = bundle

        fragmentManager?.beginTransaction()?.replace(R.id.fragment_left, operationsFragment)?.commit()

        task = if (args == null) task else generateTask(args.getString("operation", taskType))

        renderView(view)

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
        task = generateTask(taskType)
    }

    private lateinit var mainContext: Context

    val operations: Map<String, (Int, Int) -> Int> = mapOf(
        "+" to { num1, num2 -> num1 + num2 },
        "-" to { num1, num2 -> num1 - num2 },
        "*" to { num1, num2 -> num1 * num2 },
        "/" to { num1, num2 -> num1 / num2 }
    )

    private var taskType: String = operations.keys.random()
    private lateinit var task: Triple<String, MutableList<Int>, Int>

    fun renderView(parentView: View) {
        val variants = task.second

        val titleText: TextView = parentView.findViewById(R.id.titleContainer)
        titleText.textSize = 24f
        titleText.text = task.first

        val listView: ListView = parentView.findViewById(R.id.variantsList)
        val adapter= ArrayAdapter<Int>(this.requireContext(), android.R.layout.simple_list_item_1, variants)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            validateTask(task.second[position])
            task = generateTask(taskType)
            renderView(parentView)
        }
    }

    fun generateTask(operationType: String): Triple<String, MutableList<Int>, Int> {
        var (correct, incorrect) = (mainContext as DataListener).sendData()

        val difficulty: Int = if (correct - incorrect <= 0) 1 else ((correct - incorrect) / 10 + 1)
        var variants: MutableList<Int> = mutableListOf()

        val num1 = ((10.0.pow(difficulty - 1).toInt())..(10.0.pow(difficulty).toInt())).random()
        val num2 = ((10.0.pow(difficulty - 1).toInt())..(10.0.pow(difficulty).toInt())).random()
        val operation = if (operations.keys.contains(operationType)) operationType else operations.keys.random()

        val result = operations.getValue(operation)(num1, num2)

        taskType = operationType

        for (i in 0..2) {
            variants.add(((result-10)..(result+10)).random())
        }

        variants.add(result)
        variants.shuffle()

        val expression: String = "$num1 $operation $num2 = "

        return Triple(expression, variants, result)
    }

    fun validateTask(answer: Int): Boolean {
        val isCorrect = (task.third == answer)
        (mainContext as DataListener).getData(isCorrect)

        return isCorrect
    }
}