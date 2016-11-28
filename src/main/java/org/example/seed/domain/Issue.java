package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dozer.Mapping;
import org.example.seed.catalog.IssuePriority;
import org.example.seed.catalog.IssueStatus;
import org.example.seed.catalog.IssueType;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Issue extends Momentum implements IDomain {

    @Id
    @GeneratedValue
    @Column(name = "ID_ISSUE", nullable = false, updatable = false)
    @Mapping("key")
    private Long id;

    @Column(name = "TITLE", length = 80)
    private String title;

    @Column(name = "DESCRIPTION", length = 150)
    private String description;

    @Column(name = "TYPE", length = 11)
    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Column(name = "PRIORITY", length = 6)
    @Enumerated(EnumType.STRING)
    private IssuePriority priority;

    @Column(name = "STATUS", length = 11)
    @Enumerated(EnumType.STRING)
    private IssueStatus status;
}
