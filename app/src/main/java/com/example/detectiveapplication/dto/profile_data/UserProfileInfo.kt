package com.example.detectiveapplication.dto.profile_data



import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileInfo(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("status")
    val status: String?
): Parcelable