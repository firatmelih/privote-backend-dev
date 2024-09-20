package com.firatmelih.privote.dev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String description;

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Poll_Option> pollOptions;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Poll_Vote> votes;
}
