package com.eBook.mgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eBook.mgr.dto.PaymentDto;
import com.eBook.mgr.mapper.PaymentMapper;

@Service
public class PaymentServiceImpl implements PaymentService{

	@Autowired
	PaymentMapper paymentMapper;
	
	@Override
	public List<PaymentDto> listPayment(String setDate) throws Exception {
		// TODO Auto-generated method stub
		return paymentMapper.listPayment(setDate);
	}

	@Override
	public void insertPayment(PaymentDto dto) throws Exception {
		// TODO Auto-generated method stub
		paymentMapper.createPayment(dto);
	}

	@Override
	public String[] writerIdList() throws Exception {
		// TODO Auto-generated method stub
		return paymentMapper.writerIdList();
	}

	@Override
	public void removeList(String setDate) throws Exception {
		// TODO Auto-generated method stub
		paymentMapper.deleteList(setDate);
	}

	@Override
	public void updateCarryAmount(String carryAmount, String writerId) throws Exception {
		// TODO Auto-generated method stub
		paymentMapper.modifyCarryAmount(carryAmount, writerId);
	}

	@Override
	public void updateVirtuousTax(String writerId) throws Exception {
		// TODO Auto-generated method stub
		paymentMapper.modifyVirtuousTax(writerId);
	}

	@Override
	public PaymentDto allPayment(String writerId, String setDate) throws Exception {

		PaymentDto paymentDto = new PaymentDto();
		
		// 각 플랫폼 아이디별로 합계 로직 구성
		String[] tempBookcube = paymentMapper.bookcubePayment(writerId, setDate);
		String[] tempEpub = paymentMapper.epubPayment(writerId, setDate);
		String[] tempJoara = paymentMapper.joaraPayment(writerId, setDate); 
		String[] tempKakao = paymentMapper.kakaoPayment(writerId, setDate);
		String[] tempKyobo = paymentMapper.kyoboPayment(writerId, setDate);
		String[] tempMrblue = paymentMapper.mrbluePayment(writerId, setDate);
		String[] tempMunpia = paymentMapper.munpiaPayment(writerId, setDate);
		String[] tempNaver = paymentMapper.naverPayment(writerId, setDate);
		String[] tempRidibooks = paymentMapper.ridibooksPayment(writerId, setDate);
		String[] tempRomance = paymentMapper.romancePayment(writerId, setDate);
		String[] tempTocsoda = paymentMapper.tocsodaPayment(writerId, setDate);
		String[] tempWinstore = paymentMapper.winstorePayment(writerId, setDate);
		String[] tempYes24 = paymentMapper.yes24Payment(writerId, setDate);
		String[] tempAladin = paymentMapper.aladinPayment(writerId, setDate);
		
		
		int i;
		int sum=0;
		
		
		for(i=0; i<tempBookcube.length; i++) {
			tempBookcube[i] = tempBookcube[i].replace(",", "");
			sum = sum + Integer.parseInt(tempBookcube[i]);
		}
		paymentDto.setPayBookcube(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempEpub.length; i++) {
			tempEpub[i] = tempEpub[i].replace(",", "");
			sum = sum + Integer.parseInt(tempEpub[i]);
		}
		paymentDto.setPayEpub(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempJoara.length; i++) {
			tempJoara[i] = tempJoara[i].replace(",", "");
			sum = sum + Integer.parseInt(tempJoara[i]);
		}
		paymentDto.setPayJoara(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempKakao.length; i++) {
			tempKakao[i] = tempKakao[i].replace(",", "");
			sum = sum + Integer.parseInt(tempKakao[i]);
		}
		paymentDto.setPayKakao(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempKyobo.length; i++) {
			tempKyobo[i] = tempKyobo[i].replace(",", "");
			sum = sum + Integer.parseInt(tempKyobo[i]);
		}
		paymentDto.setPayKyobo(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempMrblue.length; i++) {
			tempMrblue[i] = tempMrblue[i].replace(",", "");
			sum = sum + Integer.parseInt(tempMrblue[i]);
		}
		paymentDto.setPayMrblue(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempMunpia.length; i++) {
			tempMunpia[i] = tempMunpia[i].replace(",", "");
			sum = sum + Integer.parseInt(tempMunpia[i]);
		}
		paymentDto.setPayMunpia(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempNaver.length; i++) {
			tempNaver[i] = tempNaver[i].replace(",", "");
			sum = sum + Integer.parseInt(tempNaver[i]);
		}
		paymentDto.setPayNaver(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempRidibooks.length; i++) {
			tempRidibooks[i] = tempRidibooks[i].replace(",", "");
			sum = sum + Integer.parseInt(tempRidibooks[i]);
		}
		paymentDto.setPayRidibooks(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempRomance.length; i++) {
			tempRomance[i] = tempRomance[i].replace(",", "");
			sum = sum + Integer.parseInt(tempRomance[i]);
		}
		paymentDto.setPayRomance(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempTocsoda.length; i++) {
			tempTocsoda[i] = tempTocsoda[i].replace(",", "");
			sum = sum + Integer.parseInt(tempTocsoda[i]);
		}
		paymentDto.setPayTocsoda(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempWinstore.length; i++) {
			tempWinstore[i] = tempWinstore[i].replace(",", "");
			sum = sum + Integer.parseInt(tempWinstore[i]);
		}
		paymentDto.setPayWinstore(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempYes24.length; i++) {
			tempYes24[i] = tempYes24[i].replace(",", "");
			sum = sum + Integer.parseInt(tempYes24[i]);
		}
		paymentDto.setPayYes24(Integer.toString(sum));
		sum = 0;
		
		for(i=0; i<tempAladin.length; i++) {
			tempAladin[i] = tempAladin[i].replace(",", "");
			sum = sum + Integer.parseInt(tempAladin[i]);
		}
		paymentDto.setPayAladin(Integer.toString(sum));
		sum = 0;
		
		return paymentDto;
	}

}
