package com.elevenhelevenm.practice.board.domain.board.controller;

import com.elevenhelevenm.practice.board.config.security.WithAuthUser;
import com.elevenhelevenm.practice.board.domain.board.dto.BoardDto;
import com.elevenhelevenm.practice.board.domain.board.model.Board;
import com.elevenhelevenm.practice.board.domain.board.repository.BoardRepository;
import com.elevenhelevenm.practice.board.domain.board.service.BoardService;
import com.elevenhelevenm.practice.board.domain.user.model.Role;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BoardApiControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MockMvc mvc;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @AfterEach
    void cleanup() {
        boardRepository.deleteAll();
    }

    /**
     * Query Test
     */
    @Test
    @WithMockUser(roles = {"MEMBER", "MANAGER", "ADMIN"})
    @DisplayName("????????? ?????? ?????? ?????? ??? ????????? ????????? ?????? ????????????.")
    void selectBoards() throws Exception {
        //given
        String page = "0";

        //when
        String url = "http://localhost" + port + "/api/v2/board";

        MvcResult result = mvc.perform(get(url)
                        .param("page", page))
                .andExpect(status().isOk())
                .andReturn();

        // TODO: 2022-04-18 ????????? ?????? ?????? ??????
        String json = result.getResponse().getContentAsString();
        json = json.substring(json.indexOf("["), json.indexOf("]") + 1);

        log.info("json : {}", json);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //java LocalDateTime??? ???????????? ?????? ????????? ?????? (jackson core??? bind-datatype-jsr ??????????????? ??????)
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); //json String ????????? Java Object??? ???????????? ?????? ??????

        //then
        List<BoardDto> boards = Arrays.asList(objectMapper.readValue(json, BoardDto[].class));
        assertThat(boards.size()).isLessThanOrEqualTo(10);
    }

    @Test
    @WithMockUser(roles = {"MEMBER", "MANAGER", "ADMIN"})
    @DisplayName("????????? ID??? ?????? ?????? ??? ?????? ????????? ????????? ????????????.")
    void selectBoard() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        BoardDto requestDto = BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        Long savedId = boardService.saveV2(requestDto);

        //when
        String url = "http://localhost:" + port + "/api/v2/board/" + savedId;

        MvcResult result = mvc.perform(get(url)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        BoardDto board = objectMapper.readValue(json, BoardDto.class);

        //then
        Board findBoard = boardRepository.findById(savedId).get();
        assertThat(board.getId()).isEqualTo(findBoard.getId());
        assertThat(board.getTitle()).isEqualTo(findBoard.getTitle());
        assertThat(board.getContent()).isEqualTo(findBoard.getContent());
        assertThat(board.getAuthor()).isEqualTo(findBoard.getAuthor());
    }

    /**
     * Command Test
     */
    @Test
    @WithMockUser(roles = {"MEMBER", "MANAGER", "ADMIN"})
    @DisplayName("???????????? ??? ?????? ??? ID ?????? ????????????.")
    void saveBoard() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        BoardDto requestDto = BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        //when
        String url = "http://localhost:" + port + "/api/v2/board";

        MvcResult result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        Long savedId = Long.parseLong(result.getResponse().getContentAsString());

        //then
        Board board = boardRepository.findById(savedId).get();
        assertThat(board.getId()).isEqualTo(savedId);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getAuthor()).isEqualTo(author);
    }


    @Test
    @WithAuthUser(username = "author", role = Role.MEMBER)
    @DisplayName("?????? ????????? ?????? ???????????? ????????? ?????? ??? ????????? ID??? ?????? ?????? ??? ????????? ????????? ????????????.")
    void updateBoard() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        Long savedId = boardService.saveV2(BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        BoardDto requestDto = BoardDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        //when
        String url = "http://localhost:" + port + "/api/v2/board/" + savedId;

        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(savedId.toString()));

        //then (id, author??? ???????????? ?????????.)
        Board board = boardRepository.findById(savedId).get();
        assertThat(board.getId()).isEqualTo(savedId);
        assertThat(board.getAuthor()).isEqualTo(author);
        assertThat(board.getTitle()).isEqualTo(updatedTitle);
        assertThat(board.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @WithAuthUser(username = "author", role = Role.MEMBER)
    @DisplayName("?????? ????????? ?????? ???????????? ????????? ?????? ??? ????????? ID??? ?????? ?????? ??? ???????????? ????????????.")
    void deleteBoard() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        Long savedId = boardService.saveV2(BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        Optional<Board> savedBoard = boardRepository.findById(savedId);
        assertThat(savedBoard).isNotEmpty();

        //when
        String url = "http://localhost:" + port + "/api/v1/board/" + savedId;

        mvc.perform(delete(url)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(savedId.toString()));

        //then
        Optional<Board> findBoard = boardRepository.findById(savedId);
        assertThat(findBoard).isEmpty();
    }

    @Test
    @WithAuthUser(username = "test", role = Role.MEMBER)
    @DisplayName("?????? ????????? ?????? ???????????? ????????? ?????? ??? ?????? ?????? ??? ?????? ????????? ????????????.")
    void updateBoardException() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        Long savedId = boardService.saveV2(BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        BoardDto requestDto = BoardDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        //when
        String url = "http://localhost:" + port + "/api/v2/board/" + savedId;

        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAuthUser(username = "test", role = Role.MEMBER)
    @DisplayName("?????? ????????? ?????? ???????????? ????????? ?????? ??? ?????? ?????? ??? ?????? ????????? ????????????.")
    void deleteBoardException() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "author";

        Long savedId = boardService.saveV2(BoardDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        Optional<Board> savedBoard = boardRepository.findById(savedId);
        assertThat(savedBoard).isNotEmpty();

        //when
        String url = "http://localhost:" + port + "/api/v1/board/" + savedId;

        mvc.perform(delete(url)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

}