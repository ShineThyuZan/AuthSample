package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.databinding.FragmentCreateGroupBinding
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.CreateGroupScreen
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.ChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroupFragment : Fragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateRoomViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.createGroup.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                setContent {

                    ChatTheme {
                        Surface {
                            CreateGroupScreen(
                                vm = viewModel,
                                goBack = {
                                    findNavController().popBackStack()
                                }
                            )
                        }
                    }
                }

            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

