package com.eBook.mgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eBook.mgr.dto.PaymentDto;

@Mapper
public interface PaymentMapper {
	public List<PaymentDto> listPayment(String setDate) throws Exception;
	
	public void createPayment(PaymentDto dto) throws Exception;
	
	public String[] writerIdList() throws Exception;
	
	public void deleteList(String setDate) throws Exception;
	
	public void modifyCarryAmount(@Param("carryAmount") String carryAmount,@Param("writerId") String writerId) throws Exception;
	
	public void modifyVirtuousTax(String writerId) throws Exception;
	
	
	// 각 플랫폼 합계 로직
	public String[] bookcubePayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] epubPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] joaraPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] kakaoPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] kyoboPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] mrbluePayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] munpiaPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] naverPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] ridibooksPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] romancePayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] tocsodaPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] winstorePayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] yes24Payment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
	public String[] aladinPayment(@Param("writerId") String writerId, @Param("setDate") String setDate) throws Exception;
}
