package com.eBook.mgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.domain.Member;
import com.eBook.mgr.dto.AuthorListDto;
import com.eBook.mgr.mapper.AuthorMapper;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	AuthorMapper authorMapper;
	
	@Override
	public AuthorListDto read(String writerId) throws Exception {
		return authorMapper.read(writerId);
	}


}
