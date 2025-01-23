package uk.icblog.shoppinglistapp



import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uk.icblog.shoppinglistapp.ShoppingItem


data class ShoppingItem(
    val id : Int = 0,
    var qty: Int = 0,
    var name: String = "",
    var isEditing : Boolean = false,
    var itemStatus : Int = 0
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by  remember{mutableStateOf(listOf<ShoppingItem>())}
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    var pendingItemsCount by remember { mutableIntStateOf(0) }
    var trolleyItemsCount by remember { mutableIntStateOf(0) }
    var msg by remember { mutableStateOf("") }
    // Create a FocusManager instance
    val focusManager = LocalFocusManager.current
    val context: Context = LocalContext.current

    fun removeMsgOnIputFocus(){
        if(msg.isNotBlank()){
            msg = ""
        }
    }//end removeMsgOnIputFocus

  //function to handle add button
    fun handleAddBtn(){
       var abort: Boolean = false
        if(itemName == ""){
            focusRequester1.requestFocus()
            msg = "*Please enter item name"
            abort = true
         }else if(itemQty == ""){
            focusRequester2.requestFocus()
            msg = "*Please enter item quantity"
            abort = true
        }else{
            // check if the list is empty
             if(sItems.isNotEmpty()){
                 //if not empty, check if the item name is already in the listItem
                 sItems.map{ it ->
                     if(it.name.lowercase() == itemName.lowercase()){
                         msg = "*$itemName already in the list"
                         abort = true
                     }
                 }
             }
        }// end if

        if(!abort) {
            val newItem = ShoppingItem(
                id = sItems.size + 1,
                qty = itemQty.toInt(),
                name = itemName
            )
            sItems = sItems + newItem
            pendingItemsCount = pendingItemsCount + 1
            // Request focus on the item name input field after adding item
            focusRequester1.requestFocus()
            Toast.makeText(
                context,
                "${itemName} qty: ${itemQty} added to pending list ",
                Toast.LENGTH_SHORT
            ).show()
            itemName = ""
            itemQty = ""
            msg = ""
        }// end abort is false
    }// end handleAddBtn




   Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 100.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            "My shopping list",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value= itemName,
            onValueChange = {itemName = it.trim()},
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp).focusRequester(focusRequester1).onFocusEvent{removeMsgOnIputFocus()},
            label ={(Text("Item name"))},
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    handleAddBtn()
                }
            )
        )
        Row{

            Box{
                OutlinedTextField(
                    value = itemQty,
                    onValueChange = { itemQty = it.trim() },
                    singleLine = true,
                    modifier = Modifier.padding(vertical = 8.dp).focusRequester(focusRequester2).onFocusEvent{removeMsgOnIputFocus()},
                    label = { (Text("Quantity")) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                           handleAddBtn()
                        }
                    )

                )
            }// end box
            Box(
                modifier = Modifier.padding(start = 50.dp, top= 25.dp)
                 ){
                Button(
                    onClick = {handleAddBtn()},

                    ) {
                    Text("Add")
                }// end button
            }// end box
        }// end row
        //Show msg if is not empty
         if(msg.isNotBlank()){
             Text(
                 text = msg,
                 style = TextStyle(
                     fontSize = 15.sp,
                     color = Color.Red)
                 )
         }
       //Horizontal rule
       HrDivider()

        Column(modifier = Modifier.fillMaxSize()) {
            // TabRow for the tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions -> // Customize tab indicator if needed
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
//                        COMPILED_CODE, COMPILED_CODE
                    )
                }
            ) {
                // Tab items
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        val pendingItemsCountColor = if (pendingItemsCount > 0) Color.Red else Color.Black
                        Row{
                            Text(
                                text = "${pendingItemsCount}",
                                modifier = Modifier.padding(end = 4.dp),
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    color = pendingItemsCountColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Pending Items",
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                    }

                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },

                    text = {
                        val trolleyItemsCountColor = if (trolleyItemsCount > 0) Color(0xFF4CAF50) else Color.Black
                          Row{
                            Text(
                                text = "${trolleyItemsCount}",
                                modifier = Modifier.padding(end = 4.dp),
                                style = TextStyle(
                                fontSize = 22.sp,
                                color = trolleyItemsCountColor,
                                fontWeight = FontWeight.Bold
                                )
                             )
                            Text(
                                 text = "Trolley",
                                 modifier = Modifier.padding(top = 2.dp),
                                )
                         }
                    }
                )

            }

            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> {
                    //Pending items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        //items belongs to LazyColumn
                        items(sItems){
                            //(item) represent each item in the list
                            //itemStatus Zero(0) means the list is a pending
                            item ->
                            if(item.itemStatus == 0){
                                if(item.isEditing){
                                    ShoppingListItemEditing(item = item, onEditComplete = {
                                     editedName, editedQty ->
                                      sItems = sItems.map{ it ->
                                          if(it.id == item.id){
                                              it.copy(isEditing = false, name = editedName, qty = editedQty)
                                               }else{
                                              it
                                          }// end it

                                      }// end map
                                    })
                                }else{
                                    ShoppingListItem(item,{
                                        //onEditClick
                                        //Find out which edit btn has been clciked and change the the current item
                                        //isEditing value to true.
                                        sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                                    },{
                                        //onDelete click
                                        //Remove/Delete the current item from list
                                        sItems = sItems - item
                                        pendingItemsCount = pendingItemsCount - 1
                                    },{
                                        //onDonePressed
                                        //itemStatus value to 1, this will move the item into trolley tab

                                        sItems = sItems.map{ it ->
                                            if(it.id == item.id){
                                                it.copy(itemStatus = 1)
                                              }else{
                                                it
                                            }
                                         }//end map
                                        pendingItemsCount = pendingItemsCount - 1
                                        trolleyItemsCount = trolleyItemsCount + 1
                                        // If pendingItemsCount is 0 auto switch to Tab 1 content
                                        if(pendingItemsCount == 0){
                                            selectedTabIndex = 1
                                        }
                                    },{})
                                }// end if isEditing

                             }// end itemStatus
                        }
                    }// end lazyColumn
                }
                1 -> {
                    //Trolley items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),

                    ){
                        //items belongs to LazyColumn
                        items(sItems){
                            //(item) represent each item in the list
                            //itemStatus One(1) means the list is in a trolley
                            item ->
                         if(item.itemStatus == 1){
                             ShoppingListItem(item,{},{},{},{
                                 // ongreen btn click
                                 sItems = sItems.map{ it ->
                                     if(it.id == item.id){
                                         it.copy(itemStatus = 0)
                                     }else{
                                         it
                                     }
                                 }//end map
                                 pendingItemsCount = pendingItemsCount + 1
                                 trolleyItemsCount = trolleyItemsCount - 1
                                 // If trolleyItemsCount is 0 auto switch to Tab 0 content
                                 if(trolleyItemsCount == 0){
                                     selectedTabIndex = 0
                                 }
                             })
                         }
                        }
                    }// end lazyColumn
                }

            }
        }

   }// end column

}//end ShoppingListApp

@Composable
fun HrDivider(){
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        //color = COMPILED_CODE
    )
}// end hr
@Composable
fun ShoppingListItemEditing (item: ShoppingItem, onEditComplete: (String, Int) -> Unit,) {

    var editedName by remember {mutableStateOf(item.name)}
    var editedQty by remember {mutableStateOf(item.qty.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    var msg by remember { mutableStateOf("") }
    // Create a FocusManager instance
    val focusManager = LocalFocusManager.current

    fun removeMsgOnIputFocus(){
        if(msg.isNotBlank()){
            msg = ""
        }
    }//end removeMsgOnIputFocus

    val spacerDefaultValue = 16.dp

    fun handleCompleteEdit(){
        var abort: Boolean = false
        if(editedName == ""){
            focusRequester1.requestFocus()
            msg = "*Please enter item name"
            abort = true
        }else if(editedQty == ""){
            focusRequester2.requestFocus()
            msg = "*Please enter item quantity"
            abort = true
        }// end if

        if(!abort){
        isEditing = false
        onEditComplete(editedName,editedQty.toInt())
        }// end if about is false

    }// end handleCompleteEdite


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
                modifier = Modifier.weight(1f).focusRequester(focusRequester1).onFocusEvent{removeMsgOnIputFocus()}, // weight(1f) takes up available space
                label ={(Text("Item name"))},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleCompleteEdit()
                    }
                )
            )

            OutlinedTextField(
                value = editedQty,
                onValueChange = {editedQty = it.trim()},
                singleLine = true,
                modifier = Modifier.weight(1f).focusRequester(focusRequester2).onFocusEvent{removeMsgOnIputFocus()}, // weight(1f) takes up available space, // Take up available space
                label ={(Text("Qty"))},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, // We use "Done" to trigger action when enter is pressed
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleCompleteEdit()
                    }
                )
            )

                Button(
                    onClick = {
                    handleCompleteEdit()
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon( Icons.Default.Done, "Done icon")
                }// end button 1


        }// end row 1
    if(msg.isNotBlank()){
        Text(
            text = msg,
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Red
            )
        )
    }
    HrDivider()
}// end ShoppingListItemEditing

@Composable
fun ShoppingListItem (
    item: ShoppingItem,
    onEditClick: () ->Unit,
    onDeleteClick: () -> Unit,
    onDonePressed: () -> Unit,
    onGreenBtnClick: () -> Unit
){
    val spacerDefaultValue = 16.dp
    val context: Context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // Action to perform on long press

                        if(item.itemStatus == 0) {
                            onDonePressed()
                            Toast.makeText(
                                context,
                                "${item.name} qty: ${item.qty} moved to trolley ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                )
            },
        ){
        Text(text= item.name.lowercase().replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.qty}", modifier = Modifier.padding(8.dp))
        Row(
            horizontalArrangement = Arrangement.Absolute.Right,
            modifier = Modifier
                .fillMaxWidth()
        ){
            if(item.itemStatus == 0){
                Button(onClick = {onEditClick()}) {
                    Icon( Icons.Default.Edit, "Edit icon")
                }// end button 1

                Spacer(
                    modifier = Modifier.width(spacerDefaultValue)
                )

                Button(onClick = {
                    onDeleteClick()
                    Toast.makeText(context, "${item.name} qty: ${item.qty} deleted ", Toast.LENGTH_SHORT).show()
                }) {
                    Icon( Icons.Default.Delete, "Delete icon")
                }// end button 2
            }else{
                Button(
                    onClick = {
                        onGreenBtnClick()
                        Toast.makeText(
                            context,
                            "${item.name} qty: ${item.qty} moved to pending list ",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green ,
                        contentColor = Color.Black,
                    )
                   ) {
                    Icon( Icons.Default.Done, "Done icon")

                }// end button 3

            }// end if

        }// end row 2
     }// end row 1

    HrDivider()
}// end ShoppingListItem
