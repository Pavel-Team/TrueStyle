/**Модель для получения одежды пользователя по заданному сезону*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class WardrobeResponse(@SerializedName("shopsStuffs") val shopsStuffs : List<Stuff>,
                            @SerializedName("usersStuffs") val usersStuffs : List<Stuff>) {
}