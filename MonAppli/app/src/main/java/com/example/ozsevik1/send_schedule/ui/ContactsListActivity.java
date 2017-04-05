/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ozsevik1.send_schedule.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.ozsevik1.send_schedule.R;

/**
 * FragmentActivity to hold the main {@link ContactsListFragment}.
 */
public class ContactsListActivity extends FragmentActivity implements
        ContactsListFragment.OnContactsInteractionListener {

    // Defines a tag for identifying log entries
    private static final String TAG = "ContactsListActivity";


    private static ContactsListFragment mContactsListFragment;


    private String numeroTelephone ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // Set main content view. On smaller screen devices this is a single pane view with one
        // fragment. One larger screen devices this is a two pane view with two fragments.
        setContentView(R.layout.activity_contact_selection);


        // Check if this activity instance has been triggered as a result of a search query. This
        // will only happen on pre-HC OS versions as from HC onward search is carried out using
        // an ActionBar SearchView which carries out the search in-line without loading a new
        // Activity.
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            mContactsListFragment = (ContactsListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list);

        }

    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri, String name) {

        retrieveContactNumber(contactUri);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("num", numeroTelephone);
        returnIntent.putExtra("name", name);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onNumberInputOK(String num){
        Intent returnIntent = getIntent();
        returnIntent.putExtra("num", num);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void retrieveContactNumber(Uri uriContact) {

        String contactNumber = null;
        String contactID = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        numeroTelephone = contactNumber;
    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact is no longer selected.
     */
    @Override
    public void onSelectionCleared() {

    }

}
