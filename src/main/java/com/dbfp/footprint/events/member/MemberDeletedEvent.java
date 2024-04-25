package com.dbfp.footprint.events.member;

import com.dbfp.footprint.domain.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberDeletedEvent extends ApplicationEvent {
    private final Member member;

    public MemberDeletedEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
