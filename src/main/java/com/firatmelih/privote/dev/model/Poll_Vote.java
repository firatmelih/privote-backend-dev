package com.firatmelih.privote.dev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Poll_Vote {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private User voter;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Poll poll;

    private boolean isVoted;
}
