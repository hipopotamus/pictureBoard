package hipo.pictureboard.service;

import hipo.pictureboard.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageService {

    public int[] makePageNumber(int page, int size) {

        if (page < 1) {
            page = 1;
        }

        if (size == 0) {
            int[] PageNumbers = new int[1];
            PageNumbers[0] = 1;
            return PageNumbers;
        }
        int totalPageSize = (int) Math.ceil((double) size / (double) Page.maxPage);
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

    public boolean checkLastPage(int page, int size) {
        if (size == 0) {
            return false;
        }

        int[] pageNumber = makePageNumber(page, size);
        int totalPageSize = (int) Math.ceil((double) size / (double) Page.maxPage);
        if (pageNumber[pageNumber.length - 1] >= totalPageSize) {
            return false;
        }
        return true;
    }
}
