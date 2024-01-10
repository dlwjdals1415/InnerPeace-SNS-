package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"fnqList","answerList","noticeList"})
public class Admin extends BaseEntity{
    @Id
    @Column(length = 30)
    private String admin_id;

    @Column(length = 40,nullable = false)
    private String admin_pw;

    @Builder.Default
    @OneToMany(mappedBy = "fnq_writer",cascade = CascadeType.ALL)
    private List<FNQ> fnqList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "answer_writer",cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "notice_writer",cascade = CascadeType.ALL)
    private List<Notice> noticeList = new ArrayList<>();

    public void addFnq(FNQ fnq){
        fnqList.add(fnq);
        fnq.setFnq_writer(this);
    }
    public void addAnswer(Answer answer){
        answerList.add(answer);
        answer.setAnswer_writer(this);
    }
    public void addNotice(Notice notice){
        noticeList.add(notice);
        notice.setNotice_writer(this);
    }
}
