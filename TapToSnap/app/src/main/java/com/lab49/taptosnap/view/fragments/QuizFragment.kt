package com.lab49.taptosnap.view.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lab49.taptosnap.R
import com.lab49.taptosnap.customview.QuizProgressDialog
import com.lab49.taptosnap.databinding.FragmentQuizBinding
import com.lab49.taptosnap.network.States
import com.lab49.taptosnap.network.models.ImageRequest
import com.lab49.taptosnap.network.models.QuizQuestionResponseItem
import com.lab49.taptosnap.util.*
import com.lab49.taptosnap.util.Constants.REQUEST_IMAGE_CAPTURE
import com.lab49.taptosnap.util.Constants.TIMER_INTERVAL
import com.lab49.taptosnap.util.Constants.TIMER_TASK
import com.lab49.taptosnap.view.adapter.QuizAdapter
import com.lab49.taptosnap.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(), ImageClickCallback {
    private var binding: FragmentQuizBinding? = null
    private lateinit var quizProgressDialog: QuizProgressDialog
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var adapter: QuizAdapter
    private var position: Int = 0
    private var rightCount: Int = 0
    private var wrongCount: Int = 0
    private lateinit var imgName: String
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        quizProgressDialog = QuizProgressDialog(requireContext())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeQuizData()
    }

    private fun setUpRecyclerView() {
        adapter = QuizAdapter(arrayListOf(), this)
        binding?.run {
            imageRV.adapter = adapter
            imageRV.addItemDecoration(
                SpacesItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.tile_inner_spacing)
                )
            )
        }
    }

    private fun observeQuizData() {
        viewModel.getQuizQuestions().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                States.SUCCESS -> {
                    hideProgressDialog()
                    binding?.imageRV?.visibility = VISIBLE
                    it.data?.let { it1 ->
                        retrieveList(it1)
                    }
                    showCountDownTimer()
                }
                States.ERROR -> {
                    hideProgressDialog()
                    showAlertDialog(requireContext(), it.message, "Error!!", getString(R.string.close))
                }
                States.LOADING -> {
                    showProgressDialog()
                }
            }
        })
    }

    private fun retrieveList(questions: List<QuizQuestionResponseItem>) {
        adapter.apply {
            addQuestions(questions)
            notifyDataSetChanged()
        }
    }

    private fun showProgressDialog(isCancelable: Boolean = false) {
        if (quizProgressDialog.isShowing.not()) {
            quizProgressDialog.apply {
                setCancelable(isCancelable)
                show()
            }
        }
    }

    private fun hideProgressDialog() {
        quizProgressDialog.dismiss()
    }

    private fun showCountDownTimer() {
        countDownTimer = object : CountDownTimer(TIMER_TASK, TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding?.timerTV?.text = formatTimeForTimer(millisUntilFinished)
            }

            override fun onFinish() {
                showAlertDialog(
                    requireContext(), getString(R.string.time_over), getString(
                        R.string.time_over_title
                    ), getString(R.string.try_again)
                )
            }
        }.start()
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && resultCode != RESULT_CANCELED) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = getImageUri(requireContext(), imageBitmap)
            adapter.addImageToView(position, uri.toString(), imgName, null)
            observeAnswers(uri)
        }
    }

    private fun observeAnswers(uri: Uri?) {
        showProgressDialog(false)
        viewModel.checkAnswer(ImageRequest(imgName, convertToBase64(requireContext(), uri)))
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    States.SUCCESS -> {
                        hideProgressDialog()
                        it.data?.let { it1 ->
                            adapter.addImageToView(position, uri.toString(), imgName, it1.matched)
                            if (it1.matched == true)
                                rightCount++
                            else
                                wrongCount++
                        }
                        if (rightCount == 4) {
                            countDownTimer.cancel()
                            showAlertDialog(
                                requireContext(),
                                "You have won!!",
                                "Yippe!!",
                                getString(R.string.play_again)
                            )
                        }
                        if (wrongCount == 4) {
                            countDownTimer.cancel()
                            showAlertDialog(
                                requireContext(),
                                "You have lost!!",
                                "Better luck next time!!",
                                getString(R.string.play_again)
                            )
                        }
                    }
                    States.ERROR -> {
                        hideProgressDialog()
                        showAlertDialog(requireContext(), it.message, "Error!!", getString(R.string.close))
                    }
                    States.LOADING -> {
                        showProgressDialog()
                    }
                }
            })
    }

    override fun onImageClick(adapterPosition: Int, name: String) {
        position = adapterPosition
        imgName = name
        launchCamera()
    }

    fun showAlertDialog(context: Context, msg: String? = null, title: String, buttonText: String) {
        AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(title) //set message
            .setMessage(msg) //set positive button
            .setPositiveButton(
                buttonText,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    countDownTimer.cancel()
                    binding?.imageRV?.visibility = GONE
                    observeQuizData()
                    dialogInterface.dismiss()
                })
            .show()
    }
}