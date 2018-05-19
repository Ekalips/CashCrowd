package com.ekalips.cahscrowd.stuff.utils

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract


object ContactUtils {

    data class Contact(val id: Long, val name: String, val number: String?, val email: String?)

    fun resolveContactFromPicker(context: Context, data: Intent?): Contact? {
        return data?.let {
            val contactUri = it.data

            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Email.ADDRESS)
            val cursor = context.contentResolver.query(contactUri, projection, null, null, null)
            cursor?.use {
                return if (it.moveToFirst()) {
                    // Phone.NUMBER and Email.ADDRESS directs to same columns, so initial request will make difference
                    val columnId = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    val columnName = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val columnNumber = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val columnEmail = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                    Contact(
                            it.getLong(columnId),
                            it.getString(columnName) ?: "",
                            it.getString(columnNumber),
                            it.getString(columnEmail)
                    )
                } else null
            }
        }
    }

}