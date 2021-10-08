package hipo.pictureboard.service;

import hipo.pictureboard.domain.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PageService {

    public int[] makePageNumbers(int page, int size) {

        // 인자 체크
        if (page < 1) {
            page = 1;
        }

        int totalPageSize = (int) Math.ceil((double) size / (double) Page.maxPageResult);
        int endPageNumber = Page.maxPageNumber;

        for (int i = 2; page > endPageNumber; i++) {
            endPageNumber = Page.maxPageNumber * i;
        }

        int startPageNumber = endPageNumber - 4;

        if (endPageNumber > totalPageSize) {
            endPageNumber = totalPageSize;
        }

        int totalPageNumber = endPageNumber - startPageNumber + 1;
        int[] PageNumbers = new int[totalPageNumber];
        for (int i = 0; i < totalPageNumber ; i++) {
            PageNumbers[i] = startPageNumber + i;
        }

        return PageNumbers;
    }

    public Map<String, Boolean> checkPage(int page, int size) {
        Map<String, Boolean> map = new HashMap<>();
        if (checkPageSize(size)) {
            log.info("size ========== {}", size);
            return map;
        }


        map.put("checkFirstPage", checkFirstPage(size));
        map.put("checkLastPage", checkLastPage(page, size));
        return map;
    }

    public boolean checkLastPage(int page, int size) {
        int[] pageNumber = makePageNumbers(page, size);
        int totalPageSize = (int) Math.ceil((double) size / (double) Page.maxPageResult);
        if (pageNumber[pageNumber.length - 1] >= totalPageSize) {
            return true;
        }
        return false;
    }

    public boolean checkFirstPage(int size) {
        if (size <= Page.maxPageNumber * Page.maxPageResult) {
            return true;
        }

        return false;
    }

    public boolean checkPageSize(int size) {
        if (size == 0) {
            return true;
        }
        return false;
    }
}
