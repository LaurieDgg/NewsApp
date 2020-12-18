package com.ecm.newsapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // -- inflate the layout for this fragment
        val myInflatedView: View =
            inflater.inflate(R.layout.fragment_first, container, false)

        // Set the Text to try this out
        val title = myInflatedView.findViewById<TextView>(R.id.title) as TextView
        val author = myInflatedView.findViewById<TextView>(R.id.author) as TextView
        val date = myInflatedView.findViewById<TextView>(R.id.date) as TextView
        val sourceName = myInflatedView.findViewById<TextView>(R.id.source) as TextView
        val description = myInflatedView.findViewById<TextView>(R.id.description) as TextView
        val url = myInflatedView.findViewById<ImageView>(R.id.bigPicture) as ImageView
//        val lien = myInflatedView.findViewById<TextView>(R.id.title) as TextView



        title.text = "Text to Display"

        return myInflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

        }
    }
}