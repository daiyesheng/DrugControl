package com.example.drugcontrol.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drugcontrol.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    lateinit var  disposable : Disposable

    @SuppressLint("AutoDispose")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Observable.timer(1000,TimeUnit.MILLISECONDS)
                .subscribe(object : Observer<Long>{
                    override fun onComplete() {
                        MainActivity.start(this@SplashActivity)
                        finish()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable=d
                    }

                    override fun onNext(t: Long) {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    override fun onDestroy() {
        disposable?.let {
            if (!disposable.isDisposed){
                disposable.dispose()
            }
        }
        super.onDestroy()
    }
}

