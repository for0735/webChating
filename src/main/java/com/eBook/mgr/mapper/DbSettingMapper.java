package com.eBook.mgr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.eBook.mgr.domain.DbSetting;

@Mapper
public interface DbSettingMapper {
	public void createDbSetting(DbSetting dbSetting) throws Exception;
	
	public boolean findDbSetting(String bookName) throws Exception;
	
	public void deleteDbSetting() throws Exception;
}
