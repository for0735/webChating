package com.eBook.mgr.mapper;

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
public interface EBookCreateMapper {
	public void createBookcube(Bookcube bookcube) throws Exception;
	public void createEpub(Epub epub) throws Exception;
	public void createJoara(Joara joara) throws Exception;
	public void createKakao(Kakao kakao) throws Exception;
	public void createKyobo(Kyobo kyobo) throws Exception;
	public void createMrblue(Mrblue mrblue) throws Exception;
	public void createMunpia(Munpia munpia) throws Exception;
	public void createNaver(Naver naver) throws Exception;
	public void createRidibooks(Ridibooks ridibooks) throws Exception;
	public void createRomance(Romance romance) throws Exception;
	public void createTocsoda(Tocsoda tocsoda) throws Exception;
	public void createWinstore(Winstore winstore) throws Exception;
	public void createAladin(Aladin aladin) throws Exception;
	public void createYes24(Yes24 yes24) throws Exception;
	
	
	public String readBrand(String productName) throws Exception;
	public String readAuthor(String productName) throws Exception;
	public String readWriterId(String author) throws Exception;
}
