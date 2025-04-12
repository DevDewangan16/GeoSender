package com.example.geosender.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geosender.ui.data.LocationData

@Composable
fun LocationButton(
    text: String,
    icon: ImageVector = Icons.Default.LocationOn,
    onClick: () -> Unit,  // Regular function type, not composable
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00BB77),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 15.sp)
        }
    }
}

@Composable
fun LocationCard(
    location: LocationData?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            //.padding(16.dp),
        ,shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(2.dp,Color(0xFF00BB77)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Selected Location",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF00BB77)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (location != null) {
                location.address?.let { address ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Address: $address",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = "Latitude: ${location.latitude}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
                Text(
                    text = "Longitude: ${location.longitude}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
            } else {
                Text(
                    text = "No location selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}