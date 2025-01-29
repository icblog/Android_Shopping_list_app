package uk.icblog.shoppinglistapp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun ListDisplayEditForm(item: ShoppingItem, mainViewModel: MainViewModel){

    var editedName by remember {mutableStateOf(item.name)}
    var editedQty by remember {mutableStateOf(item.qty.toString())}
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between elements
    verticalAlignment = Alignment.CenterVertically // Align items vertically
    ){

        OutlinedTextField(
            value = editedName,
            onValueChange = {editedName = it.trim()},
            singleLine = true,
            modifier = Modifier.weight(1f).focusRequester(focusRequester1).onFocusEvent{mainViewModel.removeMsgOnInputFocus(action = "edit")}, // weight(1f) takes up available space
            label ={(Text("Item name"))},
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    mainViewModel.handleEditComplete(item,editedName,editedQty)
                }
            )
        )

        OutlinedTextField(
            value = editedQty,
            onValueChange = {editedQty = it.trim()},
            singleLine = true,
            modifier = Modifier.weight(1f).focusRequester(focusRequester2).onFocusEvent{mainViewModel.removeMsgOnInputFocus(action = "edit")}, // weight(1f) takes up available space, // Take up available space
            label ={(Text("Qty"))},
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    mainViewModel.handleEditComplete(item,editedName,editedQty)
                }
            )
        )

        Button(
            onClick = {
                mainViewModel.handleEditComplete(item,editedName,editedQty)
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon( Icons.Default.Done, "Done icon")
        }// end button 1


    }// end row 1

    if(mainViewModel.shoppingListState.value.editMsg.isNotBlank()){
        Text(
            text = mainViewModel.shoppingListState.value.editMsg,
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Red
            )
        )
    }

      //Auto focus on itemName input
        LaunchedEffect(Unit) {
            focusRequester1.requestFocus()
        }

    HrDivider()


}// end  ListDisplayEditForm