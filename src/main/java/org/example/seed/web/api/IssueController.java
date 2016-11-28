package org.example.seed.web.api;

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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/issues")
public class IssueController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueMapperImpl issueMapperImpl;

    @Autowired
    private IssueService issueService;

    @Autowired
    private CounterService counterService;

    @RequestMapping(method = RequestMethod.GET)
    public Callable<ResponseEntity<PageDto<IssueDto>>> getAllIssues(@RequestParam("$numberPage") final Integer numberPage, @RequestParam("$recordsPerPage") final Integer recordsPerPage) throws ExecutionException, InterruptedException {
        this.logger.info("> getAllIssues");

        this.counterService.increment("services.issues.findAll.invoke");

        final PageDto<IssueDto> issuesDto = this.issueMapperImpl
                .mapPageReverse(this.issueService.findAll(numberPage - 1, recordsPerPage)).get();

        this.logger.info("< getAllIssues");

        return () -> new ResponseEntity<>(issuesDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Callable<ResponseEntity<IssueDto>> createIssue(@RequestBody @Validated(value = {IssueCreateGroup.class}) IssueDto issueDto) {
        this.logger.info("> createIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl.map(this.issueService
                .create(this.issueMapperImpl.map(issueDto)));

        this.logger.info("< createIssue");

        return () -> new ResponseEntity<>(currentIssueDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Callable<ResponseEntity<IssueDto>> getIssue(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        this.logger.info("> getIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl
                .map(this.issueService.find(Long.valueOf(id)).get());

        this.logger.info("< getIssue");

        return () -> new ResponseEntity<>(currentIssueDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Callable<ResponseEntity<IssueDto>> updateIssue(@RequestBody @Validated(value = {MomentumGroup.class, IssueUpdateGroup.class}) IssueDto issueDto) {
        this.logger.info("> updateIssue");

        final IssueDto currentIssueDto = this.issueMapperImpl
                .map(this.issueService.update(this.issueMapperImpl.map(issueDto)));

        this.logger.info("< updateIssue");

        return () -> new ResponseEntity<>(currentIssueDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<IssueDto> deleteIssue(@PathVariable("id") String issueId) {
        this.logger.info("> deleteIssue");

        this.issueService.delete(Long.valueOf(issueId));

        this.logger.info("< deleteIssue");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
