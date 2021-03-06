package com.elevenhelevenm.practice.board.domain.board.repository;

import com.elevenhelevenm.practice.board.domain.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
