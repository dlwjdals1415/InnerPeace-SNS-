package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"reporter","report_board","report_comment"})
public class Report extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_id")
    private Healer reporter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_no")
    private Board report_board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_no")
    private Comment report_comment;

    @Column(length = 100,nullable = false)
    private String report_reason;

    @Builder.Default
    private boolean report_status = false;
}
