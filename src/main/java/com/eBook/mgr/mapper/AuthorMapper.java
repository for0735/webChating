package com.eBook.mgr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.dto.AuthorListDto;

@Mapper
public interface AuthorMapper {
	public AuthorListDto read(String writerId) throws Exception;
}
