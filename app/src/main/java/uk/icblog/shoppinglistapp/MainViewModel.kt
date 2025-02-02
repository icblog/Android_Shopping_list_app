package uk.icblog.shoppinglistapp
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.text.isNotBlank
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    data class ShoppingListState(
        val list: List<ShoppingItem> = emptyList(),
        val msg: String = "",
        val editMsg: String = "",
        val pendingItemsCount: Int = 0,
        val trolleyItemsCount: Int = 0,
        val selectedTabIndex: Int = 0,

    )

    private val _initialShoppingListState = mutableStateOf(ShoppingListState())
    val shoppingListState : State<ShoppingListState> = _initialShoppingListState

    private fun checkIfItemNameAlreadyExist(itemName: String, action: String = "add"): Boolean{
            var outCome =  false

        _initialShoppingListState.value.list.map{ it ->
            if(it.name.lowercase() == itemName.lowercase()){
                if(action == "edit"){
                    _initialShoppingListState.value = _initialShoppingListState.value.copy(

                        editMsg =  "*$itemName already in the list"
                    )
                }else{
                    _initialShoppingListState.value = _initialShoppingListState.value.copy(

                        msg =  "*$itemName already in the list"
                    )
                }
               outCome = true
            }// end if
        }// end map

        return outCome

    }// end checkIfItemNameAlreadyExist

    //Handle handleTabClick
    fun handleTabClick(action: String){
        if(action == "pending_items"){
            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                selectedTabIndex =  0
            )
        }else{
            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                selectedTabIndex =  1
            )

        }

    }// end handleTabClick



    //function to handle add button
    fun handleAddBtn(itemName:String, itemQty: String): Boolean {
        //Check for form values error
        var abort = false
        var isAddSuccess = false
        if(itemName.isEmpty()){

            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                msg =  "*Please enter item name"
            )
              abort = true
        }else if(itemQty.isEmpty()){

            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                msg =  "*Please enter item quantity"
            )
             abort = true
        }else{
            // check if the list is empty
            if(_initialShoppingListState.value.list.isNotEmpty()){
                //if not empty, check if the item name is already in the listItem
                abort = checkIfItemNameAlreadyExist(itemName)
            }// end if
        }// end if

        //Take action if no errors in form values
        if(!abort) {
            val newItem = ShoppingItem(
                id = _initialShoppingListState.value.list.size + 1,
                qty = itemQty.toInt(),
                name = itemName
            )

            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                list = _initialShoppingListState.value.list  + newItem,
                msg = "",
                pendingItemsCount = _initialShoppingListState.value.pendingItemsCount + 1
            )
            isAddSuccess = true

      }// end abort is false
        return isAddSuccess

    }// end handleAddBtn

    fun handleEditComplete(item: ShoppingItem, editedName:String, editedQty: String){
        //Check for form values error
        var abort = false

        if(editedName.isEmpty()){
            //focusRequester1.requestFocus()
            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                editMsg =  "*Please enter item name"
            )
            abort = true
        }else if(editedQty.isEmpty()){

            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                editMsg =  "*Please enter item quantity"
            )
            abort = true
        }else{
            //Check if the item name has been edited first
            if(editedName != item.name){
                // check if the list is empty
                if(_initialShoppingListState.value.list.isNotEmpty()){
                    //if not empty, check if the item name is already in the listItem
                    abort = checkIfItemNameAlreadyExist(editedName,"edit")
                }// end if
            }// end if item name has been edited

        }// end if

        //Take action if no errors in form values
        if(!abort) {

            val editedList = _initialShoppingListState.value.list.map{ it ->
                if(it.id == item.id){
                    it.copy(isEditing = false, name = editedName, qty = editedQty.toInt())
                }else{
                    it
                }// end it
              }// end map
            _initialShoppingListState.value = _initialShoppingListState.value.copy(
                list = editedList,
                editMsg = ""
            )
        }// end abort is false
    }// handleEditComplete

    fun removeMsgOnInputFocus(action: String = "add"){
        // Launch a coroutine to clear the error after 3 seconds
        viewModelScope.launch {
            delay(7000)  // Delay for 3 seconds
           if(action == "edit"){
               if(_initialShoppingListState.value.editMsg.isNotBlank()){
                _initialShoppingListState.value = _initialShoppingListState.value.copy(
                    editMsg = ""
                )
               }

           }else{

                    if(_initialShoppingListState.value.msg.isNotBlank()){
                    _initialShoppingListState.value = _initialShoppingListState.value.copy(
                        msg = ""
                    )
                }

          }// end action
       }

    }//end removeMsgOnInputFocus

    fun onEditClick(item: ShoppingItem){

        val newList  = _initialShoppingListState.value.list.map{it.copy(isEditing = it.id == item.id)}
        _initialShoppingListState.value = _initialShoppingListState.value.copy(
            list = newList
        )
    }// end onEditClick

    fun onDeleteClick(item: ShoppingItem){
        var tempTabIndex = 0
        if(_initialShoppingListState.value.pendingItemsCount - 1 == 0 && _initialShoppingListState.value.trolleyItemsCount > 0){
            tempTabIndex = 1
        }
        _initialShoppingListState.value = _initialShoppingListState.value.copy(
            list = _initialShoppingListState.value.list - item,
            pendingItemsCount = _initialShoppingListState.value.pendingItemsCount - 1,
            selectedTabIndex =  tempTabIndex
        )


    }// end onDeleteClick

    fun onLongPressed(item: ShoppingItem){
        //itemStatus value to 1, this will move the item into trolley tab
        var tempTabIndex = 0
        val newList = _initialShoppingListState.value.list.map{ it ->
            if(it.id == item.id){
                it.copy(itemStatus = 1)
            }else{
                it
            }
        }//end map
        if(_initialShoppingListState.value.pendingItemsCount - 1 == 0){
            tempTabIndex = 1
        }

        _initialShoppingListState.value = _initialShoppingListState.value.copy(
            list = newList,
            pendingItemsCount = _initialShoppingListState.value.pendingItemsCount - 1,
            trolleyItemsCount = _initialShoppingListState.value.trolleyItemsCount + 1,
            selectedTabIndex =  tempTabIndex
        )
     }// end onLongPressed

    fun onGreenBtnClick(item: ShoppingItem){
        var tempTabIndex = 1
        val newList = _initialShoppingListState.value.list.map{ it ->
            if(it.id == item.id){
                it.copy(itemStatus = 0)
            }else{
                it
            }
        }//end map

         if(_initialShoppingListState.value.trolleyItemsCount - 1 == 0){
             tempTabIndex = 0
         }

        _initialShoppingListState.value = _initialShoppingListState.value.copy(
            list = newList,
            pendingItemsCount = _initialShoppingListState.value.pendingItemsCount + 1,
            trolleyItemsCount = _initialShoppingListState.value.trolleyItemsCount - 1,
            selectedTabIndex =  tempTabIndex
        )
   }// onGreenBtnClick

}// end class  MainViewModel