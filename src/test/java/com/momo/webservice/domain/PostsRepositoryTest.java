package com.momo.webservice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.momo.webservice.domain.posts.Posts;
import com.momo.webservice.domain.posts.PostsRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class) // JUnit4 @RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach // JUnit4의 @After
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        postsRepository.deleteAll();
    }

    /**
     * data-h2.sql entity 미리 insert한 것 적용되서 기존 test 코드로는 failed 뜸.
     * 그래서 save하면 맨 마지막 index에 저장되기 때문에 맨 마지막 index를 읽어서 내용 일치하는가에 대한 테스트 코드 작성.
     */

    @Test
    public void 게시글저장_불러오기() {
        //given
        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .author("jojoldu@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(postsList.size()-1);
        /**
         * JUnit5에서 assertThat()과 is() 사용, hamcrest 라이브러리 포함해서 사용
         * https://hwanud.wordpress.com/2018/09/29/junit5%EC%97%90%EC%84%9C-assertthat%EA%B3%BC-is-%EC%82%AC%EC%9A%A9/
         */
        assertThat(posts.getTitle(), is("테스트 게시글"));
        assertThat(posts.getContent(), is("테스트 본문"));
    }


    /**
     * data-h2.sql entity 미리 insert한 것 적용되서 기존 test 코드로는 failed 뜸.
     * 그래서 save하면 맨 마지막 index에 저장되기 때문에 맨 마지막 index를 읽어서 내용 일치하는가에 대한 테스트 코드 작성.
     */

    @Test
    public void BaseTimeEntity_등록 (){
        //given
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .author("test11@gmail.com")
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(postsList.size()-1);
        assertThat(posts.getTitle(), is("테스트 게시글"));
        assertTrue(posts.getCreatedDate().isAfter(now));
        assertTrue(posts.getModifiedDate().isAfter(now));
    }
}
