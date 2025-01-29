package uk.icblog.shoppinglistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun App(mainViewModel : MainViewModel){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 80.dp, horizontal = 20.dp),

    ) {
        AddItemForm(mainViewModel)
        ListDisplay(mainViewModel)

    }//end column


}// end app

