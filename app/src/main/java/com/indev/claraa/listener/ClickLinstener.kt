package com.agraharisoft.notepad.Listener

interface ClickLinstener {

    fun onClickListner(position: Int)
    fun updateTextInteger(value: Int)
    fun updateTextString(value: String)
    fun callUpdateCart(id: Int, qty: String)

}
