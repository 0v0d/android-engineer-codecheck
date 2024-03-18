/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentInputKeyWordBinding
import jp.co.yumemi.android.codecheck.viewmodel.InputKeyWordViewModel

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
        binding.searchInputText.setOnKeyListener { _, keyCode, event ->
            val isEnterKey =
                keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (isEnterKey) {
                val inputText = binding.searchInputText.text.toString()
                viewModel.validateKeyword(inputText)
                return@setOnKeyListener true
            }
            false
        }
    }

    /** ViewModelを監視する */
    private fun observeViewModel() {
        viewModel.keyword.observe(viewLifecycleOwner) { keyword ->
            if (viewModel.isInputValid(keyword)) {
                navigateToResultListFragment(keyword)
            }
        }

        viewModel.showError.observe(viewLifecycleOwner) { isShowError ->
            if (isShowError) {
                showErrorText()
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

    /** エラーテキストを表示する */
    private fun showErrorText() {
        val errorText = getString(R.string.keyword_empty_error_message)
        binding.searchInputText.error = errorText
    }

    /** ビュー破棄時の処理 */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}