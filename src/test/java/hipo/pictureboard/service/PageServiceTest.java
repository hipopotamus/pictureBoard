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

        for (int i = 0; i < 20; i++) {
            Arrays.stream(pageService.makePageNumber(i, 401)).forEach(p-> System.out.println(p));
        }

    }

}