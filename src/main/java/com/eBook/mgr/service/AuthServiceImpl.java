package com.eBook.mgr.service;

import java.util.ArrayList;
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
import com.eBook.mgr.mapper.AuthMapper;

@Service
public class AuthServiceImpl implements AuthService {
	
	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	AuthMapper authMapper;
	
	@Override
	public String authRoll(String id) throws Exception {
		
		return authMapper.authRoll(id);
	}

	@Override
	public List<AuthorListDto> list(String id) throws Exception {
		
		return authMapper.list(id);
	}

	@Override
	public List<PaymentDto> listPayment(String setDate, String id) throws Exception {
		
		String[] writerId = authMapper.listWriterId(id);
		List<PaymentDto> paymentDto = new ArrayList<PaymentDto>();
		
		for(int i = 0; i<writerId.length; i++) {
			paymentDto.add(authMapper.listPayment(setDate, writerId[i]));
		}
		
		log.info("authService : " + paymentDto.size());
		
		return paymentDto;
	}

	@Override
	public List<Bookcube> listBookcube(String setDate, String id) throws Exception {

		String[] writerId = authMapper.listWriterId(id);
		List<Bookcube> bookList = new ArrayList<Bookcube>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Bookcube> tempBook = authMapper.listBookcube(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				bookList.add(tempBook.get(j));
			}
		}
		
		System.out.println("현재 북리스트 " + bookList);
		
		return bookList;
	}
	
	@Override
	public List<Epub> listEpub(String setDate, String id) throws Exception {
		
		String[] writerId = authMapper.listWriterId(id);
		List<Epub> epubList = new ArrayList<Epub>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Epub> tempEpub = authMapper.listEpub(setDate, writerId[i]);
			for(int j = 0; j<tempEpub.size(); j++) {
				epubList.add(tempEpub.get(j));
			}
		}
		
		System.out.println("현재 북리스트 " + epubList);
		
		return epubList;
	}

	@Override
	public List<Joara> listJoara(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Joara> joaraList = new ArrayList<Joara>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Joara> tempBook = authMapper.listJoara(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				joaraList.add(tempBook.get(j));
			}
		}
		return joaraList;
	}

	@Override
	public List<Kakao> listKakao(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Kakao> kakaoList = new ArrayList<Kakao>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Kakao> tempBook = authMapper.listKakao(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				kakaoList.add(tempBook.get(j));
			}
		}
		return kakaoList;
	}

	@Override
	public List<Kyobo> listkyobo(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Kyobo> kyoboList = new ArrayList<Kyobo>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Kyobo> tempBook = authMapper.listkyobo(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				kyoboList.add(tempBook.get(j));
			}
		}
		return kyoboList;
	}

	@Override
	public List<Mrblue> listMrblue(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Mrblue> mrblueList = new ArrayList<Mrblue>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Mrblue> tempBook = authMapper.listMrblue(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				mrblueList.add(tempBook.get(j));
			}
		}
		return mrblueList;
	}

	@Override
	public List<Munpia> listMunpia(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Munpia> munpiaList = new ArrayList<Munpia>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Munpia> tempBook = authMapper.listMunpia(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				munpiaList.add(tempBook.get(j));
			}
		}
		return munpiaList;
	}

	@Override
	public List<Naver> listNaver(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Naver> naverList = new ArrayList<Naver>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Naver> tempBook = authMapper.listNaver(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				naverList.add(tempBook.get(j));
			}
		}
		return naverList;
	}

	@Override
	public List<Ridibooks> listRidibooks(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Ridibooks> ridibooksList = new ArrayList<Ridibooks>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Ridibooks> tempBook = authMapper.listRidibooks(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				ridibooksList.add(tempBook.get(j));
			}
		}
		return ridibooksList;
	}

	@Override
	public List<Romance> listRomance(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Romance> romanceList = new ArrayList<Romance>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Romance> tempBook = authMapper.listRomance(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				romanceList.add(tempBook.get(j));
			}
		}
		return romanceList;
	}

	@Override
	public List<Tocsoda> listTocsoda(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Tocsoda> tocsodaList = new ArrayList<Tocsoda>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Tocsoda> tempBook = authMapper.listTocsoda(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				tocsodaList.add(tempBook.get(j));
			}
		}
		return tocsodaList;
	}

	@Override
	public List<Winstore> listWinstore(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Winstore> winstoreList = new ArrayList<Winstore>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Winstore> tempBook = authMapper.listWinstore(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				winstoreList.add(tempBook.get(j));
			}
		}
		return winstoreList;
	}

	@Override
	public List<Yes24> listYes24(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Yes24> yes24List = new ArrayList<Yes24>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Yes24> tempBook = authMapper.listYes24(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				yes24List.add(tempBook.get(j));
			}
		}
		return yes24List;
	}

	@Override
	public List<Aladin> listAladin(String setDate, String id) throws Exception {
		String[] writerId = authMapper.listWriterId(id);
		List<Aladin> aladinList = new ArrayList<Aladin>();
		
		for(int i = 0; i<writerId.length; i++) {
			List<Aladin> tempBook = authMapper.listAladin(setDate, writerId[i]);
			for(int j = 0; j<tempBook.size(); j++) {
				aladinList.add(tempBook.get(j));
			}
		}
		return aladinList;
	}

	
	
}
