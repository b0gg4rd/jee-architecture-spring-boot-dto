package org.example.seed.rest.api;

import org.example.seed.dto.IssueDto;
import org.example.seed.dto.PageDto;
import org.example.seed.group.MomentumGroup;
import org.example.seed.group.issue.IssueCreateGroup;
import org.example.seed.group.issue.IssueUpdateGroup;
import org.example.seed.mapper.impl.IssueMapperImpl;
import org.example.seed.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/issues")
public class IssueRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueMapperImpl issueMapperImpl;

    @Autowired
    private IssueService issueService;

    @Autowired
    private CounterService counterService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Callable<PageDto<IssueDto>> getAllIssues(@RequestParam("$numberPage") final Integer numberPage, @RequestParam("$recordsPerPage") final Integer recordsPerPage) throws ExecutionException, InterruptedException {
        this.logger.info("> getAllIssues");

        this.counterService.increment("services.issues.findAll.invoke");

        final PageDto<IssueDto> issuesDto = this.issueMapperImpl
                .mapPageReverse(this.issueService.findAll(numberPage - 1, recordsPerPage)).get();

        this.logger.info("< getAllIssues");

        return () -> issuesDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<IssueDto> createIssue(@RequestBody @Validated(value = {IssueCreateGroup.class}) IssueDto issueDto) {
        this.logger.info("> createIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl.map(this.issueService
                .create(this.issueMapperImpl.map(issueDto)));

        this.logger.info("< createIssue");

        return () -> currentIssueDto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Callable<IssueDto> getIssue(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        this.logger.info("> getIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl
                .map(this.issueService.find(Long.valueOf(id)).get());

        this.logger.info("< getIssue");

        return () -> currentIssueDto;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Callable<IssueDto> updateIssue(@RequestBody @Validated(value = {MomentumGroup.class, IssueUpdateGroup.class}) IssueDto issueDto) {
        this.logger.info("> updateIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl
                .map(this.issueService.update(this.issueMapperImpl.map(issueDto)));

        this.logger.info("< updateIssue");

        return () -> currentIssueDto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable("id") String issueId) {
        this.logger.info("> deleteIssue");

        this.issueService.delete(Long.valueOf(issueId));

        this.logger.info("< deleteIssue");
    }
}
