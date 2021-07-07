package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Ridibooks {
	String idx;
	
	String sales;										//판매처
	String seriesId; 									//시리즈 ID
	String bookId; 										//도서 ID
	String productName; 								//제목
	String episode; 									//권/화수
	String author; 										//저자
	String translators; 								//역자
	String picture; 									//그림
	String brand; 										//출판사
	String seriesName; 									//시리즈명
	String ebooksPrice; 								//전자책정가
	String singleSaleSales; 							//단권판매액
	String singleSaleTicket; 							//단권판매권
	String singleFreeTicket; 							//단권무료권
	String rentalAmount; 								//대여액
	String rentalTicket; 								//대여권
	String rentalFreeTicket;							//대여무료권
	String freeDownAmount; 								//자유다운금액
	String freeDownTicket; 								//자유다운권
	String setSaleAmount; 								//세트판매액
	String setSaleTicket; 								//세트판매권
	String setRentalAmount; 							//세트대여액
	String setRentalTicket; 							//세트대여권
	String cancelAmount; 								//취소액
	String cancelTicket; 								//취소권
	String cancelSingleSaleSales; 						//취소단권판매액
	String cancelSingleSaleTicket; 						//취소단권판매권
	String cancelRentalAmount; 							//취소대여액
	String cancelRentalTicket; 							//취소대여권
	String cancelFreeDownAmount; 						//취소자유다운액
	String cancelFreeDownTicket; 						//취소자유다운권
	String cancelSetSaleAmount; 						//취소세트판매액
	String cancelSetSaleTicket; 						//취소세트판매권
	String cancelSetRentalAmount; 						//세트대여액
	String cancelSetRentalTicket; 						//세트대여권
	String payment; 									//정산액
	String paperbookIsbn10; 							//종이책ISBN10
	String paperbookIsbn13; 							//종이책ISBN13
	String ebookIsbn10; 								//전자책ISBN10
	String ebookIsbn13; 								//전자책ISBN13
	String cpManageId; 									//CP 관리 ID
	String category1; 									//카테고리1
	String category2; 									//카테고리2
	String writerId; 									//작가id
	String setDate; 										//기준월
}
