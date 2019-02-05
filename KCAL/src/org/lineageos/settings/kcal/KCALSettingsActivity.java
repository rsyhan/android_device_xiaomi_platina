/*
 * Copyright (C) 2018 The Xiaomi-SDM660 Project
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
 * limitations under the License
 */

package org.lineageos.settings.kcal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class KCALSettingsActivity extends Activity {

    private KCALSettings mKCALSettingsFragment;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getApplicationContext();

        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment == null) {
            mKCALSettingsFragment = new KCALSettings();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, mKCALSettingsFragment)
                    .commit();
        } else {
            mKCALSettingsFragment = (KCALSettings) fragment;
        }
    }

    static Context getContext() {
        return mContext;
    }

    private void reset() {
        mKCALSettingsFragment.mSetOnBoot.setChecked(Utils.SETONBOOT_DEFAULT);
        mKCALSettingsFragment.mRed.setValue(Utils.RED_DEFAULT);
        mKCALSettingsFragment.mRed.refresh(Utils.RED_DEFAULT);
        mKCALSettingsFragment.mGreen.setValue(Utils.GREEN_DEFAULT);
        mKCALSettingsFragment.mGreen.refresh(Utils.GREEN_DEFAULT);
        mKCALSettingsFragment.mBlue.setValue(Utils.BLUE_DEFAULT);
        mKCALSettingsFragment.mBlue.refresh(Utils.BLUE_DEFAULT);
        mKCALSettingsFragment.mMin.setValue(Utils.MINIMUM_DEFAULT);
        mKCALSettingsFragment.mMin.refresh(Utils.MINIMUM_DEFAULT);
        mKCALSettingsFragment.mSaturation.setValue(Utils.SATURATION_DEFAULT);
        mKCALSettingsFragment.mSaturation.refresh(Utils.SATURATION_DEFAULT);
        mKCALSettingsFragment.mValue.setValue(Utils.VALUE_DEFAULT);
        mKCALSettingsFragment.mValue.refresh(Utils.VALUE_DEFAULT);
        mKCALSettingsFragment.mContrast.setValue(Utils.CONTRAST_DEFAULT);
        mKCALSettingsFragment.mContrast.refresh(Utils.CONTRAST_DEFAULT);
        mKCALSettingsFragment.mHue.setValue(Utils.HUE_DEFAULT);
        mKCALSettingsFragment.mHue.refresh(Utils.HUE_DEFAULT);

        if (!mKCALSettingsFragment.mEnabled.isChecked()) {
            mKCALSettingsFragment.mEnabled.setTitle(R.string.kcal_disabled);
            mKCALSettingsFragment.prefState(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
		            onBackPressed();
                return true;

            case R.id.action_reset:
                reset();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_reset, menu);
        return true;
    }
}
