package uk.icblog.shoppinglistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ListDisplay(mainViewModel: MainViewModel){
   // var selectedTabIndex by remember { mutableIntStateOf(mainViewModel.shoppingListState.value.selectedTabIndex) }


    Column(modifier = Modifier.fillMaxSize()) {
        // TabRow for the tabs
        TabRow(
            selectedTabIndex = mainViewModel.shoppingListState.value.selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions -> // Customize tab indicator if needed
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[mainViewModel.shoppingListState.value.selectedTabIndex]),
                )
            }
        ) {
            // Tab items
            Tab(
                selected = mainViewModel.shoppingListState.value.selectedTabIndex == 0,
                onClick = {
                    //selectedTabIndex = 0
                    mainViewModel.handleTabClick("pending_items")
                          },
                text = {
                    val pendingItemsCountColor = if (mainViewModel.shoppingListState.value.pendingItemsCount > 0) Color.Red else Color.Black
                    Row{
                        Text(
                            text = "${mainViewModel.shoppingListState.value.pendingItemsCount}",
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
                selected = mainViewModel.shoppingListState.value.selectedTabIndex == 1,
                onClick = {
                    //selectedTabIndex = 1
                    mainViewModel.handleTabClick("trolley_items")
                          },

                text = {
                    val trolleyItemsCountColor = if (mainViewModel.shoppingListState.value.trolleyItemsCount > 0) Color(0xFF4CAF50) else Color.Black
                    Row{
                        Text(
                            text = "${mainViewModel.shoppingListState.value.trolleyItemsCount}",
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
        when (mainViewModel.shoppingListState.value.selectedTabIndex) {

            0 -> {

                //Pending items
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ){
                    //items belongs to LazyColumn
                    items(mainViewModel.shoppingListState.value.list){
                        //(item) represent each item in the list
                        //itemStatus Zero(0) means the list is a pending
                            item ->
                        if(item.itemStatus == 0){
                            if(item.isEditing){
                                ListDisplayEditForm(item,mainViewModel)

                            }else{
                               ListDisplayDefault(item,mainViewModel)
                            }// end if item isEditing

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
                    items(mainViewModel.shoppingListState.value.list){
                        //(item) represent each item in the list
                        //itemStatus One(1) means the list is in a trolley
                            item ->
                        if(item.itemStatus == 1){

                            ListDisplayDefault(item,mainViewModel)
                       }

                    }
                }// end lazyColumn

            }

        }


}// end column


}// end list display