package com.example.dancognitionapp.participants.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dancognitionapp.R
import com.example.dancognitionapp.ui.theme.DanCognitionAppTheme

class AddEditParticipantsFragment: Fragment() {
    private val args: AddEditParticipantsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_compose_host, container, false)
        view.findViewById<ComposeView>(R.id.compose_root).setContent {
            DanCognitionAppTheme {
                AddEditParticipantsFullScreen(
                    screenType = args.screenType,
                    participantInternalId = args.participantInternalId
                ) {
                    findNavController().popBackStack(R.id.participants_view_dest, false)
                }
            }
        }
        return view
    }
}