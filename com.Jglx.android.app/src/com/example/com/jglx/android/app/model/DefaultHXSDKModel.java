/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.com.jglx.android.app.model;

/**
 * UI Demo HX Model implementation
 */

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.example.com.jglx.android.app.db.UserDao;
import com.example.com.jglx.android.app.util.HXPreferenceUtils;

/**
 * HuanXin default SDK Model implementation
 * 
 * @author easemob
 * 
 */
public class DefaultHXSDKModel extends HXSDKModel {
	UserDao dao = null;
	protected Context context = null;
	protected Map<Key, Object> valueCache = new HashMap<Key, Object>();

	public DefaultHXSDKModel(Context ctx) {
		context = ctx;
		HXPreferenceUtils.init(context);
	}

	@Override
	public void setSettingMsgNotification(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgNotification(paramBoolean);
		valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgNotification() {
		Object val = valueCache.get(Key.VibrateAndPlayToneOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgNotification();
			valueCache.put(Key.VibrateAndPlayToneOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgSound(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgSound(paramBoolean);
		valueCache.put(Key.PlayToneOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgSound() {
		Object val = valueCache.get(Key.PlayToneOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgSound();
			valueCache.put(Key.PlayToneOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgVibrate(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgVibrate(paramBoolean);
		valueCache.put(Key.VibrateOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgVibrate() {
		Object val = valueCache.get(Key.VibrateOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgVibrate();
			valueCache.put(Key.VibrateOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgSpeaker(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgSpeaker(paramBoolean);
		valueCache.put(Key.SpakerOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgSpeaker() {
		Object val = valueCache.get(Key.SpakerOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgSpeaker();
			valueCache.put(Key.SpakerOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public boolean getUseHXRoster() {
		return false;
	}

	@Override
	public String getAppProcessName() {
		return null;
	}

	public void allowChatroomOwnerLeave(boolean value) {
		HXPreferenceUtils.getInstance()
				.setSettingAllowChatroomOwnerLeave(value);
	}

	public boolean isChatroomOwnerLeaveAllowed() {
		return HXPreferenceUtils.getInstance()
				.getSettingAllowChatroomOwnerLeave();
	}

	public void setGroupsSynced(boolean synced) {
		HXPreferenceUtils.getInstance().setGroupsSynced(synced);
	}

	public boolean isGroupsSynced() {
		return HXPreferenceUtils.getInstance().isGroupsSynced();
	}

	public void setContactSynced(boolean synced) {
		HXPreferenceUtils.getInstance().setContactSynced(synced);
	}

	public boolean isContactSynced() {
		return HXPreferenceUtils.getInstance().isContactSynced();
	}

	public void setBlacklistSynced(boolean synced) {
		HXPreferenceUtils.getInstance().setBlacklistSynced(synced);
	}

	public boolean isBacklistSynced() {
		return HXPreferenceUtils.getInstance().isBacklistSynced();
	}

	enum Key {
		VibrateAndPlayToneOn, VibrateOn, PlayToneOn, SpakerOn, DisabledGroups, DisabledIds
	}
}
