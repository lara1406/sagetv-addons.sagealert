/*
 *      Copyright 2009 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.client;

import com.google.code.gwtsrwc.client.ValidatedIntegerTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Widget for configuring user settings
 * @author dbattams
 * @version $Id$
 */
final class UserSettingsPanel extends VerticalPanel {
	static private final UserSettingsPanel INSTANCE = new UserSettingsPanel();
	static final UserSettingsPanel getInstance() { return INSTANCE; }
	
	private FlexTable tbl;
	private HorizontalPanel toolbar;
	private ValidatedMinutesTextBox recMonSleep, uiMonSleep, conflictMonSleep, sysMsgMonSleep, lowSpaceMonSleep;
	private ValidatedIntegerTextBox lowSpaceThreshold;
	private SettingsServiceAsync rpc;
	
	private UserSettingsPanel() {
		rpc = GWT.create(SettingsService.class);
		tbl = new FlexTable();
		toolbar = new HorizontalPanel();
		
		recMonSleep = new ValidatedMinutesTextBox(1, 15);
		uiMonSleep = new ValidatedMinutesTextBox(1, 120);
		conflictMonSleep = new ValidatedMinutesTextBox(1, 120);
		sysMsgMonSleep = new ValidatedMinutesTextBox(1, 5);
		lowSpaceMonSleep = new ValidatedMinutesTextBox(1, 120);
		lowSpaceThreshold = new ValidatedIntegerTextBox(0, 1024);
		
		recMonSleep.setName(UserSettings.REC_MONITOR_SLEEP);
		tbl.setText(0, 0, "Amount of time between each run of the recording monitor thread (minutes):");
		tbl.setWidget(0, 1, recMonSleep);
		
		uiMonSleep.setName(UserSettings.UI_MONITOR_SLEEP);
		tbl.setText(1, 0, "Amount of time between each run of the UI monitor thread (minutes):");
		tbl.setWidget(1, 1, uiMonSleep);
		
		conflictMonSleep.setName(UserSettings.CONFLICT_MONITOR_SLEEP);
		tbl.setText(2, 0, "Amount of time between each run of the conflict monitor thread (minutes):");
		tbl.setWidget(2, 1, conflictMonSleep);
		
		sysMsgMonSleep.setName(UserSettings.SYSMSG_MONITOR_SLEEP);
		tbl.setText(3, 0, "Amount of time between each run of the system message monitor thread (minutes):");
		tbl.setWidget(3, 1, sysMsgMonSleep);
		
		lowSpaceMonSleep.setName(UserSettings.LOW_SPACE_MONITOR_SLEEP);
		tbl.setText(4, 0, "Amount of time between each run of the low space monitor thread (minutes):");
		tbl.setWidget(4, 1, lowSpaceMonSleep);
		
		lowSpaceThreshold.setName(UserSettings.LOW_SPACE_THRESHOLD);
		tbl.setText(5, 0, "Space is low when below this many GBs:");
		tbl.setWidget(5, 1, lowSpaceThreshold);

		reloadValues();
		
		for(int i = 1; i < tbl.getRowCount(); i += 2)
			tbl.getRowFormatter().addStyleName(i, "sageOddRow");
		
		Button saveBtn = new Button("Save");
		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				checkValidity();
			}
		});
		toolbar.add(saveBtn);
		
		Button resetBtn = new Button("Reset");
		resetBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				reloadValues();
			}
		});
		toolbar.add(resetBtn);
		
		add(tbl);
		add(toolbar);
	}
	
	private void setInputValue(final TextBox box, String var, String defaultVal) {
		rpc.getSetting(var, defaultVal, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(String result) {
				box.setValue(result);
			}
		});
	}
	
	private void checkValidity() {
		if(!recMonSleep.isValueValid())
			recMonSleep.setFocus(true);
		else if(!uiMonSleep.isValueValid())
			uiMonSleep.setFocus(true);
		else if(!conflictMonSleep.isValueValid())
			conflictMonSleep.setFocus(true);
		else if(!sysMsgMonSleep.isValueValid())
			sysMsgMonSleep.setFocus(true);
		else if(!lowSpaceMonSleep.isValueValid())
			lowSpaceMonSleep.setFocus(true);
		else {
			saveSetting(recMonSleep.getName(), recMonSleep.getValue());
			saveSetting(uiMonSleep.getName(), uiMonSleep.getValue());
			saveSetting(conflictMonSleep.getName(), conflictMonSleep.getValue());
			saveSetting(sysMsgMonSleep.getName(), sysMsgMonSleep.getValue());
			saveSetting(lowSpaceMonSleep.getName(), lowSpaceMonSleep.getValue());
			saveSetting(lowSpaceThreshold.getName(), lowSpaceThreshold.getValue());
		}
	}
	
	private void saveSetting(String var, String val) {
		GWT.log(var + " :: " + val, null);
		rpc.setSetting(var, val, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void reloadValues() {
		setInputValue(recMonSleep, recMonSleep.getName(), UserSettings.REC_MONITOR_SLEEP_DEFAULT);
		setInputValue(uiMonSleep, uiMonSleep.getName(), UserSettings.UI_MONITOR_SLEEP_DEFAULT);
		setInputValue(conflictMonSleep, conflictMonSleep.getName(), UserSettings.CONFLICT_MONITOR_SLEEP_DEFAULT);
		setInputValue(sysMsgMonSleep, sysMsgMonSleep.getName(), UserSettings.SYSMSG_MONITOR_SLEEP_DEFAULT);
		setInputValue(lowSpaceMonSleep, lowSpaceMonSleep.getName(), UserSettings.LOW_SPACE_MONITOR_SLEEP_DEFAULT);
		setInputValue(lowSpaceThreshold, lowSpaceThreshold.getName(), UserSettings.LOW_SPACE_THRESHOLD_DEFAULT);		
	}
}
