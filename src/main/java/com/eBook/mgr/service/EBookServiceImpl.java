package com.eBook.mgr.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.eBook.mgr.mapper.EBookCreateMapper;
import com.eBook.mgr.mapper.EBookDeleteMapper;
import com.eBook.mgr.mapper.EBookListMapper;

@Service
public class EBookServiceImpl implements EBookService{
	
	@Autowired
	EBookCreateMapper createMapper;
	
	@Autowired
	EBookDeleteMapper deleteMapper;
	
	@Autowired
	EBookListMapper listMapper;

	private static final Logger log = LoggerFactory.getLogger(EBookServiceImpl.class);

	
	// 등록단---------------------------------------------------------------------------
	@Override
	public void registerBookcube(Bookcube bookcube) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createBookcube(bookcube);
	}

	@Override
	public void registerEpub(Epub epub) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createEpub(epub);
	}

	@Override
	public void registerJoara(Joara joara) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createJoara(joara);
	}

	@Override
	public void registerKakao(Kakao kakao) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createKakao(kakao);
	}

	@Override
	public void registerKyobo(Kyobo kyobo) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createKyobo(kyobo);
	}

	@Override
	public void registerMrblue(Mrblue mrblue) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createMrblue(mrblue);
	}

	@Override
	public void registerMunpia(Munpia munpia) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createMunpia(munpia);
	}

	@Override
	public void registerNaver(Naver naver) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createNaver(naver);
	}

	@Override
	public void registerRidibooks(Ridibooks ridibooks) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createRidibooks(ridibooks);
	}

	@Override
	public void registerRomance(Romance romance) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createRomance(romance);
	}

	@Override
	public void registerTocsoda(Tocsoda tocsoda) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createTocsoda(tocsoda);
	}

	@Override
	public void registerWinstore(Winstore winstore) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createWinstore(winstore);
	}
	
	@Override
	public void registerYes24(Yes24 yes24) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createYes24(yes24);
	}
	
	@Override
	public void registerAladin(Aladin aladin) throws Exception {
		// TODO Auto-generated method stub
		createMapper.createAladin(aladin);
	}
	
	
	
	@Override
	public String readBrand(String productName) throws Exception {
		// TODO Auto-generated method stub
		return createMapper.readBrand(productName);
	}
	
	@Override
	public String readAuthor(String productName) throws Exception {
		// TODO Auto-generated method stub
		return createMapper.readAuthor(productName);
	}	
	
	@Override
	public String readWriterId(String author) throws Exception {
		// TODO Auto-generated method stub
		return createMapper.readWriterId(author);
	}
	
	
	
	
	

	// 삭제단---------------------------------------------------------------------------------
	@Override
	public void removeBookcube(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteBookcube(idx);
	}

	@Override
	public void removeEpub(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteEpub(idx);
	}

	@Override
	public void removeJoara(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteJoara(idx);
	}

	@Override
	public void removeKakao(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteKakao(idx);
	}

	@Override
	public void removeKyobo(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteKyobo(idx);
	}

	@Override
	public void removeMrblue(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteMrblue(idx);
	}

	@Override
	public void removeMunpia(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteMunpia(idx);
	}

	@Override
	public void removeNaver(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteNaver(idx);
	}

	@Override
	public void removeRidibooks(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteRidibooks(idx);
	}

	@Override
	public void removeRomance(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteRomance(idx);
	}

	@Override
	public void removeTocsoda(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteTocsoda(idx);
	}

	@Override
	public void removeWinstore(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteWinstore(idx);
	}
	
	@Override
	public void removeYes24(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteYes24(idx);
	}
	
	@Override
	public void removeAladin(String idx) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.deleteAladin(idx);
	}

	
	
	
	@Override
	public void allRemoveBookcube(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteBookcube(setDate);
	}
	
	@Override
	public void allRemoveEpub(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteEpub(setDate);
	}

	@Override
	public void allRemoveJoara(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteJoara(setDate);
	}

	@Override
	public void allRemoveKakao(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteKakao(setDate);
	}

	@Override
	public void allRemoveKyobo(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteKyobo(setDate);
	}

	@Override
	public void allRemoveMrblue(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteMrblue(setDate);
	}

	@Override
	public void allRemoveMunpia(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteMunpia(setDate);
	}

	@Override
	public void allRemoveNaver(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteNaver(setDate);
	}

	@Override
	public void allRemoveRidibooks(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteRidibooks(setDate);
	}

	@Override
	public void allRemoveRomance(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteRomance(setDate);
	}

	@Override
	public void allRemoveTocsoda(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteTocsoda(setDate);
	}

	@Override
	public void allRemoveWinstore(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteWinstore(setDate);
	}

	@Override
	public void allRemoveAladin(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteAladin(setDate);
	}

	@Override
	public void allRemoveYes24(String setDate) throws Exception {
		// TODO Auto-generated method stub
		deleteMapper.allDeleteYes24(setDate);
	}

	// 조회단---------------------------------------------------------------------------
	@Override
	public List<Bookcube> listBookcube(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listBookcube(setDate);
	}

	@Override
	public List<Epub> listEpub(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listEpub(setDate);
	}

	@Override
	public List<Joara> listJoara(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listJoara(setDate);
	}

	@Override
	public List<Kakao> listKakao(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listKakao(setDate);
	}

	@Override
	public List<Kyobo> listkyobo(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listkyobo(setDate);
	}

	@Override
	public List<Mrblue> listMrblue(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listMrblue(setDate);
	}

	@Override
	public List<Munpia> listMunpia(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listMunpia(setDate);
	}

	@Override
	public List<Naver> listNaver(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listNaver(setDate);
	}

	@Override
	public List<Ridibooks> listRidibooks(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listRidibooks(setDate);
	}

	@Override
	public List<Romance> listRomance(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listRomance(setDate);
	}

	@Override
	public List<Tocsoda> listTocsoda(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listTocsoda(setDate);
	}

	@Override
	public List<Winstore> listWinstore(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listWinstore(setDate);
	}
	
	@Override
	public List<Yes24> listYes24(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listYes24(setDate);
	}
	
	@Override
	public List<Aladin> listAladin(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return listMapper.listAladin(setDate);
	}

	


}
