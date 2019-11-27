package com.example.drugcontrol.interfaces

interface IModel {
    /**
     * 在框架中 [BasePresenter.onDestroy] 时会默认调用 [IModel.onDestroy]
     */
    abstract fun onDestroy()
}