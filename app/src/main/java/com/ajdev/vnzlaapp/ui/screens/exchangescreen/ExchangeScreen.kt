package com.ajdev.vnzlaapp.ui.screens.exchangescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ajdev.vnzlaapp.R
import com.ajdev.vnzlaapp.domain.model.Datetime
import com.ajdev.vnzlaapp.domain.model.Exchange
import com.ajdev.vnzlaapp.domain.model.ExchangeCompany
import com.ajdev.vnzlaapp.domain.model.Monitors
import com.ajdev.vnzlaapp.ui.screens.loadingscreen.LoadingScreen
import com.ajdev.vnzlaapp.ui.screens.errorscreen.ErrorScreen
import com.ajdev.vnzlaapp.ui.theme.BackgroundColor
import com.ajdev.vnzlaapp.ui.theme.CardColor
import com.ajdev.vnzlaapp.ui.theme.InputBackground
import com.ajdev.vnzlaapp.ui.theme.PrimaryColor
import com.ajdev.vnzlaapp.ui.theme.TextPrimary
import com.ajdev.vnzlaapp.ui.theme.TextSecondary

@Composable
fun ExchangeScreen(viewModel: ExchangeViewModel) {

    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorState.collectAsState()
    val exchange by viewModel.exchange.collectAsState()
    val exchangeType by viewModel.typeOfExchange.collectAsState()
    val amountToCalculated by viewModel.amountToCalculated.collectAsState()
    val amounts by viewModel.calculatedAmounts.collectAsState()

    when {
        loading -> {
            LoadingScreen()
        }
        error -> {
            ErrorScreen(onRetry = { viewModel.fetchData() })
        }
        else -> {
            ExchangeScreenContent(
                exchange = exchange,
                amountToCalculated = amountToCalculated,
                amounts = amounts,
                exchangeType = exchangeType,
                amountChange = { viewModel.setAmount(it) },
                onCalculateClick = { viewModel.calculateAmount() },
                changeExchangeType = { viewModel.setExchange(it) }
            )
        }
    }
}

@Composable
fun ExchangeScreenContent(
    exchange: Exchange?,
    amountToCalculated: String,
    amounts: CalculatedAmounts,
    exchangeType: ChangeType,
    amountChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    changeExchangeType: (ChangeType) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp),
    ) {

        Text(
            text = stringResource(R.string.exchange_rates),
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ExchangeItem(
                imageUrl = exchange?.monitors?.bcv?.image,
                price = exchange?.monitors?.bcv?.price,
                label = stringResource(R.string.bcv),
                modifier = Modifier.weight(1f),
            )

            ExchangeItem(
                imageUrl = exchange?.monitors?.enparalelovzla?.image,
                price = exchange?.monitors?.enparalelovzla?.price,
                label = stringResource(R.string.parallel),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.calculate_your_amount),
            color = TextPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = amountToCalculated,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                        amountChange(newValue)
                    }
                },
                label = { Text(stringResource(R.string.calculate_your_amount_label)) },
                modifier = Modifier
                    .background(InputBackground, shape = RoundedCornerShape(8.dp))
                    .weight(0.5f)
                    .padding(end = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            EnumDropdown(exchangeType) {
                changeExchangeType(it)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onCalculateClick() },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(R.string.calculate), color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            ExchangeCalculatedText(
                label = stringResource(R.string.bcv),
                amount = amounts.bcv
            )
            ExchangeCalculatedText(
                label = stringResource(R.string.parallel),
                amount = amounts.parallel
            )
            ExchangeCalculatedText(
                label = stringResource(R.string.average),
                amount = amounts.average
            )
        }
    }
}

@Composable
fun ExchangeItem(imageUrl: String?, price: Any?, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Logo de $label",
                modifier = Modifier
                    .aspectRatio(1f)
                    .size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$label: $price",
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EnumDropdown(
    selectedOption: ChangeType,
    onOptionSelected: (ChangeType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = stringResource(selectedOption.type))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            enumValues<ChangeType>().forEach { option ->
                DropdownMenuItem(
                    text = { Text(stringResource(option.type)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ExchangeCalculatedText(label: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary)
        Text(text = amount, color = TextPrimary)
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeScreenPreview() {
    val exchange = Exchange(
        datetime = Datetime("", ""),
        monitors = Monitors(
            bcv = ExchangeCompany(image = "", 80.00),
            enparalelovzla = ExchangeCompany(image = "", price = 100.00)
        )
    )
    ExchangeScreenContent(
        exchange = exchange,
        amountToCalculated = "422",
        amounts = CalculatedAmounts(),
        exchangeType = ChangeType.DOLLARTOBS,
        amountChange = {},
        onCalculateClick = {},
        changeExchangeType = {}
    )
}

