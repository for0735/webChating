package com.eBook.mgr.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EBookDeleteMapper {
	public void deleteBookcube(String idx) throws Exception;
	public void deleteEpub(String idx) throws Exception;
	public void deleteJoara(String idx) throws Exception;
	public void deleteKakao(String idx) throws Exception;
	public void deleteKyobo(String idx) throws Exception;
	public void deleteMrblue(String idx) throws Exception;
	public void deleteMunpia(String idx) throws Exception;
	public void deleteNaver(String idx) throws Exception;
	public void deleteRidibooks(String idx) throws Exception;
	public void deleteRomance(String idx) throws Exception;
	public void deleteTocsoda(String idx) throws Exception;
	public void deleteWinstore(String idx) throws Exception;
	public void deleteAladin(String idx) throws Exception;
	public void deleteYes24(String idx) throws Exception;
	
	public void allDeleteBookcube(String setDate) throws Exception;
	public void allDeleteEpub(String setDate) throws Exception;
	public void allDeleteJoara(String setDate) throws Exception;
	public void allDeleteKakao(String setDate) throws Exception;
	public void allDeleteKyobo(String setDate) throws Exception;
	public void allDeleteMrblue(String setDate) throws Exception;
	public void allDeleteMunpia(String setDate) throws Exception;
	public void allDeleteNaver(String setDate) throws Exception;
	public void allDeleteRidibooks(String setDate) throws Exception;
	public void allDeleteRomance(String setDate) throws Exception;
	public void allDeleteTocsoda(String setDate) throws Exception;
	public void allDeleteWinstore(String setDate) throws Exception;
	public void allDeleteAladin(String setDate) throws Exception;
	public void allDeleteYes24(String setDate) throws Exception;
}
