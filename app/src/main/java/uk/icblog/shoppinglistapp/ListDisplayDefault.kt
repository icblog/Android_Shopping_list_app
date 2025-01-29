package uk.icblog.shoppinglistapp


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp



@Composable
fun ListDisplayDefault(item: ShoppingItem, mainViewModel: MainViewModel){

    val spacerDefaultValue = 16.dp


    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // Action to perform on long press

                        if(item.itemStatus == 0) {
                           mainViewModel.onLongPressed(item)
                         }

                    }
                )
            },
    ){
        Text(
            text= item.name.lowercase().replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
            )
          )
        Text(
            text = "Qty: ${item.qty}",
            modifier = Modifier.padding(8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
             )
           )
        Row(
            horizontalArrangement = Arrangement.Absolute.Right,
            modifier = Modifier
                .fillMaxWidth()
        ){
            if(item.itemStatus == 0){
                //tbd
                Button(onClick = {mainViewModel.onEditClick(item)}) {
                    Icon( Icons.Default.Edit, "Edit icon")
                }// end button 1

                Spacer(
                    modifier = Modifier.width(spacerDefaultValue)
                )

                Button(onClick = {

                    mainViewModel.onDeleteClick(item)

                }) {
                    Icon( Icons.Default.Delete, "Delete icon")
                }// end button 2
            }else{
                Button(
                    onClick = {

                        mainViewModel.onGreenBtnClick(item)

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

}// end  ListDisplayDefault