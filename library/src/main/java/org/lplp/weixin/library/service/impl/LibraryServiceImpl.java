package org.lplp.weixin.library.service.impl;

import java.util.LinkedList;

import org.lplp.weixin.library.domain.Book;
import org.lplp.weixin.library.domain.DebitList;
import org.lplp.weixin.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public Page<Book> search(String keyword, int pageNumber) {

		Pageable pageable = PageRequest.of(pageNumber, 3);

		Page<Book> page;
		if (StringUtils.isEmpty(keyword)) {
			// where disabled == false
			page = this.bookRepository.findByDisabledFalse(pageable);
		} else {
			// where name like '%' + keyword + '%' and disabled == false
			page = this.bookRepository.findByNameContainingAndDisabledFalse(keyword, pageable);
		}

		return page;
	}

	@Override
	public void add(String id, DebitList list) {
		if (list.getBooks() == null) {
			list.setBooks(new LinkedList<>());
		}

		boolean exists = false;
		for (Book book : list.getBooks()) {
			if (book.getId().equals(id)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			this.bookRepository.findById(id)
					.ifPresent(list.getBooks()::add);

//			Book book = this.bookRepository.findById(id)
//					// 如果Book不存在，返回null
//					.orElse(null);
//			if (book != null) {
//				list.getBooks().add(book);
//			}

//			this.bookRepository.findById(id)//
//					.ifPresent(new Consumer<Book>() {
//						@Override
//						public void accept(Book book) {
//							list.getBooks().add(book);
//						}
//					});

//			this.bookRepository.findById(id)//
//					.ifPresent((Book book) -> {
//						list.getBooks().add(book);
//					});

//			this.bookRepository.findById(id)//
//					.ifPresent(book -> list.getBooks().add(book));
		}
	}

	@Override
	public void remove(String id, DebitList list) {
		list.getBooks()
				.stream()
				.filter(book -> book.getId().equals(id))
				.findFirst()
				.ifPresent(list.getBooks()::remove);

//		Book book = null;
//		for (Book b : list.getBooks()) {
//			if (b.getId().equals(id)) {
//				book = b;
//				break;
//			}
//		}
//		list.getBooks().remove(book);
	}
}
