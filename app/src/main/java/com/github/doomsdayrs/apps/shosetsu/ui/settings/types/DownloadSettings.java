package com.github.doomsdayrs.apps.shosetsu.ui.settings.types;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 * Shosetsu
 * 13 / 07 / 2019
 *
 * @author github.com/doomsdayrs
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.doomsdayrs.apps.shosetsu.R;

import java.util.Objects;

import static com.github.doomsdayrs.apps.shosetsu.backend.Utilities.download;
import static com.github.doomsdayrs.apps.shosetsu.backend.Utilities.shoDir;

public class DownloadSettings extends Fragment {

    private TextView textView;

    public DownloadSettings() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("OnCreateView", "DownloadSettings");
        View view = inflater.inflate(R.layout.settings_download, container, false);
        textView = view.findViewById(R.id.settings_download_dir);
        textView.setText(shoDir);
        textView.setOnClickListener(view1 -> performFileSearch());
        return view;
    }

    private void setDir(String dir) {
        download.edit().putString("dir", dir).apply();
        shoDir = dir;
        textView.setText(dir);
    }

    private void performFileSearch() {
        Toast.makeText(getContext(), "Please make sure this is on the main storage, SD card storage is not functional yet", Toast.LENGTH_LONG).show();
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 42);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String path = Objects.requireNonNull(data.getData()).getPath();
                Log.i("Selected Folder", "Uri: " + path);
                if (path != null)
                    setDir(path.substring(Objects.requireNonNull(path).indexOf(":") + 1));
                else Toast.makeText(getContext(), "Path is null", Toast.LENGTH_SHORT).show();
            }
        }

    }
}