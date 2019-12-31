package com.colorseekbardemo

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.divyanshu.colorseekbar.ColorSeekBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null
    private var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        color_seek_bar.setOnColorChangeListener(object: ColorSeekBar.OnColorChangeListener{
            override fun onRelease() {
                Handler().postDelayed({
                     blinkEye()
                }, 1000)
            }

            override fun onColorChangeListener(color: Int) {
                view.setBackgroundColor(color)

            }

            override fun onPositionChangeListener(position: Int) {
                textView.setText(position.toString())

            }

        })
    }
    private fun blinkEye(){
        mHandler = Handler()
        mRunnable = Runnable {
            color_seek_bar.animateEye()
            count++
            if (count==3){
                mHandler?.removeCallbacks(mRunnable)
                count=0
            }else{
                mHandler?.postDelayed(mRunnable, 100)
            }

        }
        mHandler?.postDelayed(mRunnable, 100)
    }
}
