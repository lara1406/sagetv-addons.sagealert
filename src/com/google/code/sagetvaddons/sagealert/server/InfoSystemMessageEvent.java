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
package com.google.code.sagetvaddons.sagealert.server;

import gkusnick.sagetv.api.SystemMessageAPI.SystemMessage;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * Event triggered only for system messages with an alert level of 'INFO'
 * @author dbattams
 * @version $Id$
 */
final class InfoSystemMessageEvent extends SystemMessageEvent {

	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(InfoSystemMessageEvent.class.getCanonicalName(), "System Message Alert (Info Only)", "Alert when a SageTV system message with an alert level of 'INFO' is generated.");

	InfoSystemMessageEvent(SystemMessage msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}
}
