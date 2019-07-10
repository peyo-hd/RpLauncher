package com.peyo.rplauncher

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.MessageQueue
import java.util.LinkedList

class DeferredHandler {
    internal var mQueue = LinkedList<Runnable>()
    private val mMessageQueue = Looper.myQueue()
    private val mHandler = Impl()

    @SuppressLint("HandlerLeak")
    internal inner class Impl : Handler(), MessageQueue.IdleHandler {
        override fun handleMessage(msg: Message) {
            var r: Runnable
            synchronized(mQueue) {
                if (mQueue.size == 0) {
                    return
                }
                r = mQueue.removeFirst()
            }
            r.run()
            synchronized(mQueue) {
                scheduleNextLocked()
            }
        }

        override fun queueIdle(): Boolean {
            return false
        }
    }

    private inner class IdleRunnable internal constructor(internal var mRunnable: Runnable) : Runnable {

        override fun run() {
            mRunnable.run()
        }
    }

    fun post(runnable: Runnable) {
        synchronized(mQueue) {
            mQueue.add(runnable)
            if (mQueue.size == 1) {
                scheduleNextLocked()
            }
        }
    }

    internal fun scheduleNextLocked() {
        if (mQueue.size > 0) {
            val peek = mQueue.first
            if (peek is IdleRunnable) {
                mMessageQueue.addIdleHandler(mHandler)
            } else {
                mHandler.sendEmptyMessage(1)
            }
        }
    }
}

