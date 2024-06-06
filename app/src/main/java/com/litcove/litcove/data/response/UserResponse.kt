package com.litcove.litcove.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(

	@field:SerializedName("results")
	val results: List<ResultsItem>
) : Parcelable

@Parcelize
data class Picture(

	@field:SerializedName("large")
	val large: String
) : Parcelable

@Parcelize
data class Name(

	@field:SerializedName("last")
	val last: String,

	@field:SerializedName("first")
	val first: String
) : Parcelable

@Parcelize
data class Login(

	@field:SerializedName("password")
	val password: String
) : Parcelable

@Parcelize
data class Dob(

	@field:SerializedName("age")
	val age: Int
) : Parcelable

@Parcelize
data class ResultsItem(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("dob")
	val dob: Dob,

	@field:SerializedName("name")
	val name: Name,

	@field:SerializedName("login")
	val login: Login,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("picture")
	val picture: Picture
) : Parcelable
