package com.elevenhelevenm.practice.board.service;

import com.elevenhelevenm.practice.board.domain.board.Board;
import com.elevenhelevenm.practice.board.repository.BoardRepository;
import com.elevenhelevenm.practice.board.web.dto.request.board.BoardUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@Slf4j
@ExtendWith(MockitoExtension.class) //Mockito 사용 시 추가
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Test
    @DisplayName("ID 값과 Title, Content 값으로 수정 요청 시 해당 게시글이 변경된다.")
    void update() {
        //given
        Long id = 1L;
        String title = "title";
        String content = "content";
        String author = "author";

        Optional<Board> board = Optional.of(Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        given(boardRepository.findById(id)).willReturn(board);

        Optional<Board> saveBoard = boardRepository.findById(id);
        assertThat(saveBoard.get().getTitle()).isEqualTo(title);
        assertThat(saveBoard.get().getContent()).isEqualTo(content);
        assertThat(saveBoard.get().getAuthor()).isEqualTo(author);

        //when
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        boardService.update(id, requestDto);

        //then
        Optional<Board> findBoard = boardRepository.findById(id);
        assertThat(findBoard.get().getTitle()).isEqualTo(updatedTitle);
        assertThat(findBoard.get().getContent()).isEqualTo(updatedContent);
        assertThat(findBoard.get().getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("ID 값이 주어지면 해당 게시글을 삭제한다.")
    void delete() {
        //given
        Long id = 1L;
        String title = "title";
        String content = "content";
        String author = "author";

        Optional<Board> board = Optional.of(Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        //when
        given(boardRepository.findById(id)).willReturn(board);

        doAnswer(invocation -> given(boardRepository.findById(id)).willReturn(Optional.empty()))
                .when(boardRepository).delete(board.get());

        //then
        Optional<Board> saveBoard = boardRepository.findById(id);
        assertThat(saveBoard).isNotEmpty();

        boardService.delete(id);

        Optional<Board> findBoard = boardRepository.findById(id);
        assertThat(findBoard).isEmpty();
    }
}