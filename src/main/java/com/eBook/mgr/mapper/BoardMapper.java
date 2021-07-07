package com.eBook.mgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eBook.mgr.domain.Board;

@Mapper
public interface BoardMapper {
	public void create(Board board) throws Exception;
	
	public Board read(Integer boardNo) throws Exception;
	public Board mainRead() throws Exception;
	
	public void update(Board board) throws Exception;
	
	public void delete(Integer boardNo) throws Exception;
	
	public List<Board> list() throws Exception;
	
	public List<Board> search(String title) throws Exception;
	
	public void delete_use() throws Exception;
	
	public void select_use(int boardNo) throws Exception;
	public int lastNumber() throws Exception;
}
