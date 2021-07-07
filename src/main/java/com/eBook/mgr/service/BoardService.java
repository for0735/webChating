package com.eBook.mgr.service;

import java.util.List;

import com.eBook.mgr.domain.Board;

public interface BoardService {
	
	public void register(Board board) throws Exception;
	
	public Board read(Integer boardNo) throws Exception;
	public Board mainRead() throws Exception;
	
	public void modify(Board board) throws Exception;
	
	public void remove(Integer boardNo) throws Exception;
	
	public List<Board> list() throws Exception;
	
	public List<Board> search(String title) throws Exception;
	
	public void remove_use() throws Exception;
	
	public void select_use(int boardNo) throws Exception;
	public int lastNumber() throws Exception;
}
