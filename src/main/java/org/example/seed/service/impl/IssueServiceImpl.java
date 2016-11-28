package org.example.seed.service.impl;

import org.example.seed.catalog.IssuePriority;
import org.example.seed.catalog.IssueStatus;
import org.example.seed.domain.Issue;
import org.example.seed.repository.IssueRepository;
import org.example.seed.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

@Service
public class IssueServiceImpl implements IssueService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueRepository issueRepository;

    @Override
    @Async
    @Cacheable(value = "issues")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<Page<Issue>> findAll(int numberPage, int recordsPerPage) {

        this.logger.info("> findAll");

        final Pageable pageable = new PageRequest(numberPage, recordsPerPage, Sort.Direction.DESC, "title");

        Page<Issue> issues = this.issueRepository.findAll(pageable);

        this.logger.info("< findAll");

        return new AsyncResult<>(issues);
    }

    @Override
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Issue create(final Issue issue) {

        this.logger.info("> create");

        issue.setStatus(IssueStatus.OPEN);

        if (issue.getPriority() == null) {
            issue.setPriority(IssuePriority.MEDIUM);
        }

        Issue persistedIssue = this.issueRepository.save(issue);

        this.logger.info("< create");

        return persistedIssue;
    }

    @Override
    @Async
    @Cacheable(value = "issues")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<Issue> find(final Long id) {

        this.logger.info("> 	find id:{}", id);

        Issue issue = this.issueRepository.findOne(id);

        this.logger.info("< find id:[]", id);

        return new AsyncResult<>(issue);
    }

    @Override
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Issue update(final Issue issue) {

        this.logger.info("> update");

        final Issue currentIssue = this.issueRepository.findOne(issue.getId());

        currentIssue.setDescription(issue.getDescription());
        currentIssue.setTitle(issue.getTitle());
        currentIssue.setPriority(issue.getPriority());
        currentIssue.setStatus(issue.getStatus());
        currentIssue.setType(issue.getType());

        Issue updatedIssue = this.issueRepository.save(currentIssue);

        this.logger.info("< update");

        return updatedIssue;
    }

    @Override
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(final Long id) {

        this.logger.info("> delete");

        this.issueRepository.delete(id);

        this.logger.info("< delete");
    }
}
