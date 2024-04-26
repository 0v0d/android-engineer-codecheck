/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.databinding.FragmentInputKeyWordBinding
import jp.co.yumemi.android.codecheck.viewmodel.InputKeyWordViewModel
import kotlinx.coroutines.launch

/** リポジトリー検索画面 */
@AndroidEntryPoint
class InputKeyWordFragment : Fragment() {
    private var _binding: FragmentInputKeyWordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InputKeyWordViewModel by viewModels()

    /** ビュー生成時の処理
     * @param inflater レイアウトインフレーター
     * @param container コンテナ
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputKeyWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchInputClick()
        observeViewModel()
    }

    /**
     * 検索ボタンを押した時の処理
     */
    private fun setupSearchInputClick() {
        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setKeyword(editText.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    /** ViewModelを監視する */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeKeywordFlow()
                }
            }
        }
    }

    private suspend fun observeKeywordFlow() {
        viewModel.keyword.collect { keyword ->
            if (keyword.isNotBlank() && keyword.isNotEmpty()) {
                navigateToResultListFragment(keyword)
            }
        }
    }

    /** リポジトリーリスト画面に遷移する */
    private fun navigateToResultListFragment(inputText: String) {
        val action = InputKeyWordFragmentDirections.actionToRepositoryListFragment(inputText)
        findNavController().navigate(action)
        viewModel.onNavigateComplete()
        clearKeywordInputText()
    }

    /** キーワード入力テキストをクリアする */
    private fun clearKeywordInputText() {
        viewModel.onNavigateComplete()
        binding.searchInputText.text?.clear()
    }

    /** ビュー破棄時の処理 */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}