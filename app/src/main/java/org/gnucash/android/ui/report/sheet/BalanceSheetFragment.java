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
package org.gnucash.android.ui.report.sheet;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import org.gnucash.android.R;
import org.gnucash.android.db.DatabaseSchema;
import org.gnucash.android.db.adapter.AccountsDbAdapter;
import org.gnucash.android.model.AccountType;
import org.gnucash.android.model.Money;
import org.gnucash.android.ui.report.BaseReportFragment;
import org.gnucash.android.ui.report.ReportType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import static org.gnucash.android.model.AccountType.ASSET_ACCOUNT_TYPES;
import static org.gnucash.android.model.AccountType.EQUITY_ACCOUNT_TYPES;
import static org.gnucash.android.model.AccountType.LIABLITY_ACCOUNT_TYPES;

/**
 * Balance sheet report fragment
 * @author Ngewi Fet <ngewif@gmail.com>
 */
public class BalanceSheetFragment extends BaseReportFragment {

    @BindView(R.id.table_assets) TableLayout mAssetsTableLayout;
    @BindView(R.id.table_liabilities) TableLayout mLiabilitiesTableLayout;
    @BindView(R.id.table_equity) TableLayout mEquityTableLayout;

    @BindView(R.id.total_liability_and_equity) TextView mNetWorth;

    AccountsDbAdapter mAccountsDbAdapter = AccountsDbAdapter.getInstance();

    private Money mAssetsBalance;
    private Money mLiabilitiesBalance;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_text_report;
    }

    @Override
    public int getTitle() {
        return R.string.title_balance_sheet_report;
    }

    @Override
    public ReportType getReportType() {
        return ReportType.TEXT;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean requiresAccountTypeOptions() {
        return false;
    }

    @Override
    public boolean requiresTimeRangeOptions() {
        return false;
    }

    @Override
    protected void generateReport() {
        mAssetsBalance = mAccountsDbAdapter.getAccountBalance(ASSET_ACCOUNT_TYPES, -1, System.currentTimeMillis());
        mLiabilitiesBalance = mAccountsDbAdapter.getAccountBalance(LIABLITY_ACCOUNT_TYPES, -1, System.currentTimeMillis());
    }

    @Override
    protected void displayReport() {

        loadAccountViews(ASSET_ACCOUNT_TYPES, mAssetsTableLayout);
        loadAccountViews(LIABLITY_ACCOUNT_TYPES, mLiabilitiesTableLayout);
        loadAccountViews(EQUITY_ACCOUNT_TYPES, mEquityTableLayout);

        AccountType.ASSET.displayBalance(mNetWorth,
                                         // #8xx
                                         mAssetsBalance.add(mLiabilitiesBalance));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_group_reports_by).setVisible(false);
    }

    /**
     * Loads rows for the individual accounts and adds them to the report
     * @param accountTypes Account types for which to load balances
     * @param tableLayout Table layout into which to load the rows
     */
    private void loadAccountViews(List<AccountType> accountTypes, TableLayout tableLayout){

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        Cursor cursor = mAccountsDbAdapter.fetchAccounts(DatabaseSchema.AccountEntry.COLUMN_TYPE
                        + " IN ( '" + TextUtils.join("' , '", accountTypes) + "' ) AND "
                        + DatabaseSchema.AccountEntry.COLUMN_PLACEHOLDER + " = 0",
                null, DatabaseSchema.AccountEntry.COLUMN_FULL_NAME + " ASC");

        AccountType accountType = null;

        while (cursor.moveToNext()){
            String accountUID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_UID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_NAME));
            Money balance = mAccountsDbAdapter.getAccountBalance(accountUID);
            View view = inflater.inflate(R.layout.row_balance_sheet, tableLayout, false);
            ((TextView)view.findViewById(R.id.account_name)).setText(name);
            TextView    balanceTextView = view.findViewById(R.id.account_balance);
            accountType     = AccountType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.AccountEntry.COLUMN_TYPE)));
            accountType.displayBalance(balanceTextView,
                                       balance);
            tableLayout.addView(view);
        }

        View totalView = inflater.inflate(R.layout.row_balance_sheet, tableLayout, false);
        TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) totalView.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, 20, layoutParams.rightMargin, layoutParams.bottomMargin);
        totalView.setLayoutParams(layoutParams);

        TextView accountName = totalView.findViewById(R.id.account_name);
        accountName.setTextSize(16);
        accountName.setText(R.string.label_balance_sheet_total);
        TextView accountBalance = totalView.findViewById(R.id.account_balance);
        accountBalance.setTextSize(16);
        accountBalance.setTypeface(null, Typeface.BOLD);
        accountType.displayBalance(accountBalance,
                                   mAccountsDbAdapter.getAccountBalance(accountTypes,
                                                                        -1,
                                                                        System.currentTimeMillis()));

        tableLayout.addView(totalView);
    }

}
