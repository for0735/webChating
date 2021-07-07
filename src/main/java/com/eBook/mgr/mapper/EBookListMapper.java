package com.eBook.mgr.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eBook.mgr.domain.platform.Aladin;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.domain.platform.Epub;
import com.eBook.mgr.domain.platform.Joara;
import com.eBook.mgr.domain.platform.Kakao;
import com.eBook.mgr.domain.platform.Mrblue;
import com.eBook.mgr.domain.platform.Munpia;
import com.eBook.mgr.domain.platform.Naver;
import com.eBook.mgr.domain.platform.Ridibooks;
import com.eBook.mgr.domain.platform.Romance;
import com.eBook.mgr.domain.platform.Tocsoda;
import com.eBook.mgr.domain.platform.Winstore;
import com.eBook.mgr.domain.platform.Yes24;
import com.eBook.mgr.domain.platform.Kyobo;

@Mapper
public interface EBookListMapper {
	public List<Bookcube> listBookcube(String setDate) throws Exception;
	public List<Epub> listEpub(String setDate) throws Exception;
	public List<Joara> listJoara(String setDate) throws Exception;
	public List<Kakao> listKakao(String setDate) throws Exception;
	public List<Kyobo> listkyobo(String setDate) throws Exception;
	public List<Mrblue> listMrblue(String setDate) throws Exception;
	public List<Munpia> listMunpia(String setDate) throws Exception;
	public List<Naver> listNaver(String setDate) throws Exception;
	public List<Ridibooks> listRidibooks(String setDate) throws Exception;
	public List<Romance> listRomance(String setDate) throws Exception;
	public List<Tocsoda> listTocsoda(String setDate) throws Exception;
	public List<Winstore> listWinstore(String setDate) throws Exception;
	public List<Aladin> listAladin(String setDate) throws Exception;
	public List<Yes24> listYes24(String setDate) throws Exception;
}
