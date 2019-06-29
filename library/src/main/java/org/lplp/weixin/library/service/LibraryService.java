package org.lplp.weixin.library.service;

import org.lplp.weixin.library.domain.Book;
import org.lplp.weixin.library.domain.DebitList;
import org.springframework.data.domain.Page;

public interface LibraryService {

	Page<Book> search(String keyword, int pageNumber);

	void add(String id, DebitList list);

	void remove(String id, DebitList list);

}
