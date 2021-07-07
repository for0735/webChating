package com.eBook.mgr.service;

import com.eBook.mgr.domain.DbSetting;

public interface DbSettingService {
	public void registerDbSetting(DbSetting dbSetting) throws Exception;
	
	public boolean findDbSetting(String bookName) throws Exception;

	public void removeDbSetting() throws Exception;
}
