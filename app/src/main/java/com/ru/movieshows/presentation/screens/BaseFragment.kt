package com.ru.movieshows.presentation.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ru.movieshows.presentation.viewmodel.BaseViewModel

open class BaseFragment: Fragment() {

    protected open val viewModel by viewModels<BaseViewModel>()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {
                viewModel.updateLanguageTag()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        requireActivity().unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }

    fun setSoftInputMode(mode: Int) = requireActivity().window.setSoftInputMode(mode)
}