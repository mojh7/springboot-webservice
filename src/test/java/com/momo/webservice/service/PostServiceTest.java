package com.momo.webservice.service;

import com.momo.webservice.domain.posts.Posts;
import com.momo.webservice.domain.posts.PostsMainResponseDto;
import com.momo.webservice.domain.posts.PostsRepository;
import com.momo.webservice.domain.posts.PostsSaveRequestDto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void cleanup () {
        postsRepository.deleteAll();
    }

    /**
     * data-h2.sql entity 미리 insert한 것 적용되서 기존 dto test 코드로는 failed 뜸.
     * 그래서 save하면 맨 마지막 index에 저장되기 때문에 맨 마지막 index를 읽어서 내용 일치하는가에 대한 테스트 코드 작성.
     */

    @Test
    public void Dto데이터가_posts테이블에_저장된다(){
        //given
        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .author("test@gmail.com")
                .content("테스트의 본문")
                .title("테스트")
                .build();

        //when
        postsService.save(dto);

        //then
        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(postsList.size()-1);
        assertThat(posts.getAuthor(), equalTo(dto.getAuthor()));
        assertThat(posts.getContent(), equalTo(dto.getContent()));
        assertThat(posts.getTitle(), equalTo(dto.getTitle()));
    }

    /**
     *         PostsSaveRequestDto dto1 = PostsSaveRequestDto.builder()
     *                 .author("Test1@gmail.com")
     *                 .content("내용1")
     *                 .title("제목1")
     *                 .build();
     *
     *         PostsSaveRequestDto dto2 = PostsSaveRequestDto.builder()
     *                 .author("Test2@gmail.com")
     *                 .content("내용2")
     *                 .title("제목2")
     *                 .build();
     *
     *         postsService.save(dto1);
     *         postsService.save(dto2);
     *
     *         트랜잭션 save하고
     *
     *         readonly 트랜잭션 같이 하니 에러 뜨는 듯
     *         List<PostsMainResponseDto> posts = postsRepository.findAllDesc()
     *                 .map(PostsMainResponseDto::new)
     *                 .collect(Collectors.toList());
     */

    // test 코드도 postsRepository.findAllDesc() 트랜잭션 처리 있어서 같이 @Transactional readOnly 필요
    @Transactional(readOnly = true)
    @Test
    public void 게시글id_내림차순으로_출력한다(){
        //given


        //when
        List<PostsMainResponseDto> posts = postsRepository.findAllDesc()
                .map(PostsMainResponseDto::new)
                .collect(Collectors.toList());

        //then
        PostsMainResponseDto postsMainResponseDtoFirst = posts.get(0);
        PostsMainResponseDto postsMainResponseDtoLast = posts.get(posts.size()-1);
        assertThat(postsMainResponseDtoFirst.getId(), equalTo(Long.valueOf(posts.size())));
        assertThat(postsMainResponseDtoLast.getId(), equalTo(1L));
    }
}
