package org.example.seed.mapper.impl;

import org.example.seed.domain.Issue;
import org.example.seed.dto.IssueDto;
import org.springframework.stereotype.Component;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
@Component
public class IssueMapperImpl extends MapperImpl<IssueDto, Issue> {
    public IssueMapperImpl() {
        super(IssueDto.class, Issue.class);
    }
}
