/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        /** 前回検索日付 */
        lateinit var lastSearchDate: Date
    }
}
