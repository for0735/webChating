package com.eBook.mgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eBook.mgr.domain.Board;
import com.eBook.mgr.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper mapper;
	
	@Override
	public void register(Board board) throws Exception {
		mapper.create(board);
		
	}

	@Override
	public Board read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public int lastNumber() throws Exception {
		// TODO Auto-generated method stub
		return mapper.lastNumber();
	}

	@Override
	public Board mainRead() throws Exception {

		return mapper.mainRead();
	}

	@Override
	public void modify(Board board) throws Exception {
		mapper.update(board);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
	}

	@Override
	public List<Board> list() throws Exception {
		return mapper.list();
	}

	@Override
	public List<Board> search(String title) throws Exception {
		return mapper.search(title);
	}

	@Override
	public void remove_use() throws Exception {
		// TODO Auto-generated method stub
		mapper.delete_use();
	}

	@Override
	public void select_use(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		mapper.select_use(boardNo);
	}
	
	

}
