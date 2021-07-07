package com.eBook.mgr.service;

import java.util.Date;
import java.util.List;

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

public interface EBookService {
	
	// 등록 로직 ----------------------------------------------------------------------
	public void registerBookcube(Bookcube bookcube) throws Exception;
	public void registerEpub(Epub epub) throws Exception;
	public void registerJoara(Joara joara) throws Exception;
	public void registerKakao(Kakao kakao) throws Exception;
	public void registerKyobo(Kyobo kyobo) throws Exception;
	public void registerMrblue(Mrblue mrblue) throws Exception;
	public void registerMunpia(Munpia munpia) throws Exception;
	public void registerNaver(Naver naver) throws Exception;
	public void registerRidibooks(Ridibooks ridibooks) throws Exception;
	public void registerRomance(Romance romance) throws Exception;
	public void registerTocsoda(Tocsoda tocsoda) throws Exception;
	public void registerWinstore(Winstore winstore) throws Exception;
	public void registerYes24(Yes24 yes24) throws Exception;
	public void registerAladin(Aladin aladin) throws Exception;
	
	public String readBrand(String productName) throws Exception;
	public String readAuthor(String productName) throws Exception;
	public String readWriterId(String author) throws Exception;
	
	
	
	// 삭제 로직 ---------------------------------------------------------------------
	public void removeBookcube(String idx) throws Exception;
	public void removeEpub(String idx) throws Exception;
	public void removeJoara(String idx) throws Exception;
	public void removeKakao(String idx) throws Exception;
	public void removeKyobo(String idx) throws Exception;
	public void removeMrblue(String idx) throws Exception;
	public void removeMunpia(String idx) throws Exception;
	public void removeNaver(String idx) throws Exception;
	public void removeRidibooks(String idx) throws Exception;
	public void removeRomance(String idx) throws Exception;
	public void removeTocsoda(String idx) throws Exception;
	public void removeWinstore(String idx) throws Exception;
	public void removeYes24(String idx) throws Exception;
	public void removeAladin(String idx) throws Exception;
	
	
	public void allRemoveBookcube(String setDate) throws Exception;
	public void allRemoveEpub(String setDate) throws Exception;
	public void allRemoveJoara(String setDate) throws Exception;
	public void allRemoveKakao(String setDate) throws Exception;
	public void allRemoveKyobo(String setDate) throws Exception;
	public void allRemoveMrblue(String setDate) throws Exception;
	public void allRemoveMunpia(String setDate) throws Exception;
	public void allRemoveNaver(String setDate) throws Exception;
	public void allRemoveRidibooks(String setDate) throws Exception;
	public void allRemoveRomance(String setDate) throws Exception;
	public void allRemoveTocsoda(String setDate) throws Exception;
	public void allRemoveWinstore(String setDate) throws Exception;
	public void allRemoveAladin(String setDate) throws Exception;
	public void allRemoveYes24(String setDate) throws Exception;
	
	
	
	// 조회 로직 ----------------------------------------------------------------------
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
	public List<Yes24> listYes24(String setDate) throws Exception;
	public List<Aladin> listAladin(String setDate) throws Exception;
	
}

