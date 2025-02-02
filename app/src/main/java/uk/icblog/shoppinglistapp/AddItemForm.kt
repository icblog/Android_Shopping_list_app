package uk.icblog.shoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun AddItemForm(mainViewModel: MainViewModel){
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("") }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun actionAfterAdd(){
        // return boolean
        val result =   mainViewModel.handleAddBtn(itemName,itemQty)
        if(result){
            // Request focus on the item name input field after adding item
            focusRequester1.requestFocus()
            itemName = ""
            itemQty = ""

        }else{
            if("name" in mainViewModel.shoppingListState.value.msg ||
                "already" in mainViewModel.shoppingListState.value.msg
                ){
                focusRequester1.requestFocus()
            }else{
                focusRequester2.requestFocus()
            }
         }// end if
    }// end actionAfterAddSuccess


    Column{
        Text(
            "My shopping list",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
          // Item name input field
        OutlinedTextField(
            value= itemName,
            onValueChange = {itemName = it.trim()},
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp).focusRequester(focusRequester1).onFocusEvent{mainViewModel.removeMsgOnInputFocus()},
            label ={(Text("Item name"))},
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    actionAfterAdd()
                }
            )
        )
        //Show  name error msg if is not empty
        if(mainViewModel.shoppingListState.value.msg.isNotBlank() &&
            "name" in mainViewModel.shoppingListState.value.msg ||
            "already" in mainViewModel.shoppingListState.value.msg
            ){
            Text(
                text = mainViewModel.shoppingListState.value.msg,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red)
            )
        }
        Row( verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)){
            // Item quantity input field
                   OutlinedTextField(
                    value = itemQty,
                    onValueChange = { itemQty = it.trim() },
                    singleLine = true,
                    modifier = Modifier.weight(1f).padding(vertical = 8.dp).focusRequester(focusRequester2).onFocusEvent{mainViewModel.removeMsgOnInputFocus()},
                    label = { (Text("Quantity")) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            actionAfterAdd()
                        }
                    )

                )
                Button(
                    onClick = {
                        actionAfterAdd()
                              },

                    ) {
                    Text("Add")
                }// end button

        }// end row

        //Show quantity error msg if is not empty
        if(mainViewModel.shoppingListState.value.msg.isNotBlank() &&
            "quantity" in mainViewModel.shoppingListState.value.msg
        ){
            Text(
                text = mainViewModel.shoppingListState.value.msg,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red)
            )
        }

        //Horizontal rule
        HrDivider()
    }// end column

}//end add Item form