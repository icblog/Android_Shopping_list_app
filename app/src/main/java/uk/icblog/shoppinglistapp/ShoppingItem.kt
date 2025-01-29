package uk.icblog.shoppinglistapp

data class ShoppingItem(
    val id : Int = 0,
    val qty: Int = 0,
    val name: String = "",
    val isEditing : Boolean = false,
    val itemStatus : Int = 0
)
