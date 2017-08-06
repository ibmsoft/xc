package com.cubedrive.base.event;

import com.cubedrive.base.domain.account.User;

public class UserDeleteEvent {

    private final User user;

    public UserDeleteEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
