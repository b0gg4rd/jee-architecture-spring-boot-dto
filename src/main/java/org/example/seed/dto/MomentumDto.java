package org.example.seed.dto;

import lombok.Data;
import org.example.seed.group.MomentumGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ricardo Pina Arellano on 25/11/2016.
 */
@Data
public class MomentumDto implements Serializable {

    private static final long serialVersionUID = -7741020340940362366L;

    @NotNull(groups = {MomentumGroup.class})
    private Date registerDate;

    @NotNull(groups = {MomentumGroup.class})
    private Date changeDate;
}
