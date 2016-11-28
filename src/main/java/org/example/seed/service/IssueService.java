package org.example.seed.service;

import java.util.concurrent.Future;

import org.example.seed.domain.Issue;
import org.springframework.data.domain.Page;

public interface IssueService {

	Future<Page<Issue>> findAll(final int numberPage, final int recordsPerPage);
	
	Issue create(final Issue issue);
	
	Future<Issue> find(final Long id);
	
	Issue update(final Issue issue);
	
	void delete(final Long id);
}
