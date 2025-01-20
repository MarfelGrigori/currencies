package com.example.currencyapplication.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyapplication.R
import com.example.currencyapplication.common.model.CurrencyItem
import com.example.currencyapplication.common.ui.theme.Card
import com.example.currencyapplication.common.ui.theme.TextDefault
import com.example.currencyapplication.common.utils.displayRate

@Composable
fun CurrencyRow(currency: CurrencyItem, onFavoriteClick: (CurrencyItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Card, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(currency.code, color = TextDefault , fontWeight = FontWeight.Medium, fontSize = 14.sp)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = currency.rate.displayRate(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextDefault,
            modifier = Modifier.padding(end = 8.dp)
        )

        Image(
            painter = painterResource(
                id = if (currency.isFavorite) R.drawable.favorites else R.drawable.favorites_off
            ),
            contentDescription = "Favorite",
            modifier = Modifier
                .size(24.dp)
                .clickable { onFavoriteClick(currency) }
        )
    }
}