package jp.co.yumemi.android.codecheck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.adapter.RepositoryListAdapter
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryListBinding
import jp.co.yumemi.android.codecheck.model.domain.RepositoryItem
import jp.co.yumemi.android.codecheck.state.SearchState
import jp.co.yumemi.android.codecheck.viewmodel.RepositoryListViewModel
import kotlinx.coroutines.launch

/** リポジトリーリスト画面 */
@AndroidEntryPoint
class RepositoryListFragment : Fragment() {
    private var _binding: FragmentRepositoryListBinding? = null
    private val binding get() = _binding!!
    private val args: RepositoryListFragmentArgs by navArgs()
    private val viewModel: RepositoryListViewModel by viewModels()

    /** リポジトリーリストのアダプター */
    private val adapter by lazy {
        RepositoryListAdapter(repositoryItemClickListener)
    }

    /** リポジトリーリストをクリックした時の処理 */
    private val repositoryItemClickListener = { it: RepositoryItem ->
        navigateToRepositoryDetailFragment(it)
    }

    /**
     * ビュー生成時の処理
     * @param inflater レイアウトのインフレーター
     * @param container ルートビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     * @return 生成されたビュー
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoryListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    /**
     * ビュー生成後の処理
     * @param view 生成されたビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeErrorState()
                }
                launch {
                    observeResultList()
                }
            }
        }
        setUpResultListRecyclerView()
        viewModel.searchRepositories(args.inputText)
    }

    /** searchStateの変化を監視する */
    private suspend fun observeErrorState() {
        viewModel.searchState.collect {
            switchErrorState(it)
        }
    }

    /**
     * searchStateによって表示を変える
     * @param state 検索状態
     */
    private fun switchErrorState(state: SearchState) {
        when (state) {
            SearchState.EMPTY_RESULT -> showError(state, R.string.empty_result_message)
            SearchState.NETWORK_ERROR -> showError(state, R.string.network_error_message)
            SearchState.SUCCESS -> invisibleError()
            SearchState.LOADING -> showLoading()
        }
    }

    /**
     * エラー表示を行う
     * @param state 検索状態
     * @param messageResId エラーメッセージのリソースID
     */
    private fun showError(state: SearchState, messageResId: Int) {
        binding.repositoryRecyclerView.isVisible = false
        binding.loadingProgressBar.isVisible = false
        binding.errorTextView.text = getString(messageResId)
        binding.errorTextView.isVisible = true
        binding.retryButton.isVisible = (state == SearchState.NETWORK_ERROR)
    }

    /** エラー非表示を行う */
    private fun invisibleError() {
        binding.repositoryRecyclerView.isVisible = true
        binding.errorTextView.isVisible = false
        binding.retryButton.isVisible = false
    }

    /** ローディング表示を行う */
    private fun showLoading() {
        invisibleError()
        binding.loadingProgressBar.isVisible = true
    }

    /** 検索結果のリストを監視する */
    private suspend fun observeResultList() {
        viewModel.repositoryItems.collect {
            if (it.isNotEmpty()) {
                binding.loadingProgressBar.isVisible = false
                adapter.submitList(it)
            }
        }
    }

    /** リポジトリーリストのRecyclerViewを設定する */
    private fun setUpResultListRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.repositoryRecyclerView.layoutManager = layoutManager
        binding.repositoryRecyclerView.adapter = adapter
    }

    /** リポジトリー詳細画面に遷移する
     * @param repositoryItem リポジトリーデータ
     */
    private fun navigateToRepositoryDetailFragment(repositoryItem: RepositoryItem) {
        val date = viewModel.lastSearchDate.value
        val action =
            RepositoryListFragmentDirections.actionToRepositoryDetailFragment(
                repositoryItem,
                date.toString()
            )
        findNavController().navigate(action)
    }

    /** ビュー破棄時の処理 */
    override fun onDestroyView() {
        binding.repositoryRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}