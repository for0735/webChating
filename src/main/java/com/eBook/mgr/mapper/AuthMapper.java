package com.eBook.mgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

@Mapper
public interface AuthMapper {
	//현재 접속 ID 권한 가져오기
	public String authRoll(String id) throws Exception;
	
	//회원 리스트조회
	public List<AuthorListDto> list(String id) throws Exception;
	
	//정산 조회
	public String[] listWriterId(String id) throws Exception;
	public PaymentDto listPayment(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;

	//플랫폼 조회
	public List<Bookcube> listBookcube(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Epub> listEpub(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Joara> listJoara(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Kakao> listKakao(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Kyobo> listkyobo(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Mrblue> listMrblue(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Munpia> listMunpia(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Naver> listNaver(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Ridibooks> listRidibooks(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Romance> listRomance(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Tocsoda> listTocsoda(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Winstore> listWinstore(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Aladin> listAladin(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
	public List<Yes24> listYes24(@Param("setDate") String setDate, @Param("writerId") String writerId) throws Exception;
}
