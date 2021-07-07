package com.eBook.mgr.service;

import java.util.List;

import com.eBook.mgr.domain.platform.Aladin;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.domain.platform.Epub;
import com.eBook.mgr.domain.platform.Joara;
import com.eBook.mgr.domain.platform.Kakao;
import com.eBook.mgr.domain.platform.Kyobo;
import com.eBook.mgr.domain.platform.Mrblue;
import com.eBook.mgr.domain.platform.Munpia;
import com.eBook.mgr.domain.platform.Naver;
import com.eBook.mgr.domain.platform.Ridibooks;
import com.eBook.mgr.domain.platform.Romance;
import com.eBook.mgr.domain.platform.Tocsoda;
import com.eBook.mgr.domain.platform.Winstore;
import com.eBook.mgr.domain.platform.Yes24;
import com.eBook.mgr.dto.AuthorListDto;
import com.eBook.mgr.dto.PaymentDto;

public interface AuthService {
	public String authRoll(String id) throws Exception;
	
	public List<AuthorListDto> list(String id) throws Exception;
	
	public List<PaymentDto> listPayment(String setDate, String id) throws Exception;
	
	// 조회 로직 ----------------------------------------------------------------------
	public List<Bookcube> listBookcube(String setDate, String id) throws Exception;
	public List<Epub> listEpub(String setDate, String id) throws Exception;
	public List<Joara> listJoara(String setDate, String id) throws Exception;
	public List<Kakao> listKakao(String setDate, String id) throws Exception;
	public List<Kyobo> listkyobo(String setDate, String id) throws Exception;
	public List<Mrblue> listMrblue(String setDate, String id) throws Exception;
	public List<Munpia> listMunpia(String setDate, String id) throws Exception;
	public List<Naver> listNaver(String setDate, String id) throws Exception;
	public List<Ridibooks> listRidibooks(String setDate, String id) throws Exception;
	public List<Romance> listRomance(String setDate, String id) throws Exception;
	public List<Tocsoda> listTocsoda(String setDate, String id) throws Exception;
	public List<Winstore> listWinstore(String setDate, String id) throws Exception;
	public List<Yes24> listYes24(String setDate, String id) throws Exception;
	public List<Aladin> listAladin(String setDate, String id) throws Exception;
}
