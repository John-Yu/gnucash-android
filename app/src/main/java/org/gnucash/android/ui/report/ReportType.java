/*
 * Copyright (c) 2015 Ngewi Fet <ngewif@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gnucash.android.ui.report;

import android.content.Context;

import org.gnucash.android.R;
import org.gnucash.android.app.GnuCashApplication;
import org.gnucash.android.ui.report.barchart.StackedBarChartFragment;
import org.gnucash.android.ui.report.linechart.CashFlowLineChartFragment;
import org.gnucash.android.ui.report.piechart.PieChartFragment;
import org.gnucash.android.ui.report.sheet.BalanceSheetFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.ColorRes;

/**
 * Different types of reports
 * <p>This class also contains mappings for the reports of the different types which are available
 * in the system. When adding a new report, make sure to add a mapping in the constructor</p>
 */
public enum ReportType {
    PIE_CHART(0), BAR_CHART(1), LINE_CHART(2), TEXT(3), NONE(4);

    // #872 Use a list to be sure of the sort order which is not guaranted with a hashmap keys
    List<String> mReportNames = null;

    Map<String, Class> mReportTypeMap = new HashMap<>();

    int mValue = 4;

    /**
     * Constructor
     *
     * @param index
     */
    ReportType(int index){

        mValue = index;

        Context context = GnuCashApplication.getAppContext();

        // #872 Fill the map with all the items, in order to fill the Report Toolbar Spinner
        mReportTypeMap.put(context.getString(R.string.title_pie_chart),
                           PieChartFragment.class);

        mReportTypeMap.put(context.getString(R.string.title_bar_chart),
                           StackedBarChartFragment.class);

        mReportTypeMap.put(context.getString(R.string.title_cash_flow_report),
                           CashFlowLineChartFragment.class);

        mReportTypeMap.put(context.getString(R.string.title_balance_sheet_report),
                           BalanceSheetFragment.class);
    }

    /**
     * Returns the toolbar color to be used for this report type
     * @return Color resource
     */
    public @ColorRes
    int getTitleColor(){
        switch (mValue){
            case 0:
                return R.color.account_green;
            case 1:
                return R.color.account_red;
            case 2:
                return R.color.account_blue;
            case 3:
                return R.color.account_purple;
            case 4:
            default:
                return R.color.theme_primary;
        }
    }

    public static ReportType getReportType(final String name) {

        Context context = GnuCashApplication.getAppContext();

        if (name.equals(context.getString(R.string.title_pie_chart))) {

            return PIE_CHART;

        } else if (name.equals(context.getString(R.string.title_bar_chart))) {

            return BAR_CHART;

        } else if (name.equals(context.getString(R.string.title_cash_flow_report))) {

            return LINE_CHART;

        } else if (name.equals(context.getString(R.string.title_balance_sheet_report))) {

            return TEXT;

        } else {

            return NONE;
        }
    }

    public List<String> getReportNames(){

        Context context = GnuCashApplication.getAppContext();

        if (mReportNames == null) {
            //

            //
            mReportNames = new ArrayList<>();

            mReportNames.add(context.getString(R.string.title_pie_chart));
            mReportNames.add(context.getString(R.string.title_bar_chart));
            mReportNames.add(context.getString(R.string.title_cash_flow_report));
            mReportNames.add(context.getString(R.string.title_balance_sheet_report));

        } else {
            //  n' pas

            // RAF
        }

        return mReportNames;
    }

    public BaseReportFragment getFragment(String name){
        BaseReportFragment fragment = null;
        try {
            fragment = (BaseReportFragment) mReportTypeMap.get(name).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
