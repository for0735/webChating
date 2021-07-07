package com.eBook.mgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eBook.mgr.domain.DbSetting;
import com.eBook.mgr.mapper.DbSettingMapper;

@Service
public class DbSettingServiceImpl implements DbSettingService {

	@Autowired
	DbSettingMapper dbSettingMapper;
	
	@Override
	public void registerDbSetting(DbSetting dbSetting) throws Exception {
		// TODO Auto-generated method stub
		dbSettingMapper.createDbSetting(dbSetting);
	}

	@Override
	public void removeDbSetting() throws Exception {

		dbSettingMapper.deleteDbSetting();
		
	}

	@Override
	public boolean findDbSetting(String bookName) throws Exception {
		// TODO Auto-generated method stub
		boolean result;
		result = dbSettingMapper.findDbSetting(bookName);
		
		return result;
	}

}
