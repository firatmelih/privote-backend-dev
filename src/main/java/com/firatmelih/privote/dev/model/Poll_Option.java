package com.firatmelih.privote.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Poll_Option {
    @Id
    @GeneratedValue
    private Integer id;
    private String content;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Poll poll;

    private Integer count;
}
