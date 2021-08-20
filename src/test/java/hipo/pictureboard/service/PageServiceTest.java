package hipo.pictureboard.service;

import hipo.pictureboard.domain.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageServiceTest {

    @Autowired PageService pageService;

    @Test
    public void 페이지_테스트() {

        for (int i = 6; i < 7; i++) {
            Arrays.stream(pageService.makePageNumber(i, 101)).forEach(p-> System.out.println(p));
        }

    }

    @Test
    public void 마지막_페이지_테스트() {
        System.out.println("페이지 테스트 결과 = " + pageService.makePageNumber(6, 101));
        System.out.println("마지막 페이지 테스트 결과 = " + pageService.checkLastPage(6, 101));
    }

}