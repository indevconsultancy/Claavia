package com.agraharisoft.notepad.Listener

interface ClickLinstener {

    fun onClickListner(position: Int)
    fun updateTextView(amount: Int)
    fun updatePowerRange(power_range: String)
    fun callUpdateCart(id: Int, qty: String)

}
