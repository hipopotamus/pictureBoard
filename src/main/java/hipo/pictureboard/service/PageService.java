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
        int totalPageSize = (int) Math.ceil((double) size / (double) Page.maxPage);
        int endPageNumber = 1;

        for (int i = 1; page + 1 > endPageNumber; i++) {
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
}
