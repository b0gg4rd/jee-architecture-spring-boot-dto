package org.example.seed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seed.catalog.IssuePriority;
import org.example.seed.catalog.IssueStatus;
import org.example.seed.catalog.IssueType;
import org.example.seed.group.issue.IssueCreateGroup;
import org.example.seed.group.issue.IssueUpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Ricardo Pina Arellano on 25/11/2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto extends MomentumDto implements IDto {

    private static final long serialVersionUID = -5900981566946525598L;

    @NotEmpty(groups = {IssueUpdateGroup.class})
    private String key;

    @NotEmpty(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private String title;

    @NotEmpty(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private String description;

    @NotNull(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private IssueType type;

    @NotNull(groups = {IssueUpdateGroup.class})
    private IssueStatus status;

    @NotNull(groups = {IssueUpdateGroup.class})
    private IssuePriority priority;
}
