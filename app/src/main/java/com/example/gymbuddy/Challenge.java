package com.example.gymbuddy;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Challenge {

    public String name;
    public String level;
    public LocalDateTime startDate;
    public Date endDate;
    public int limit;
    public ArrayList<String> participants;

    public Challenge() {

    }

    public Challenge(String name, String level, LocalDateTime startDate) {
        this.name = name;
        this.level = level;
        this.startDate = startDate;

        if (level.equalsIgnoreCase("easy")){
            limit = 10;
        } else if (level.equalsIgnoreCase("medium")){
            limit = 20;
        } else {
            limit = 30;
        }

        // set endDate by adding 10 days to startDAte
        Date initial = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initial);
        calendar.add(Calendar.DAY_OF_MONTH, 10);

        endDate = calendar.getTime();

        participants = new ArrayList<>();

    }

    public String getName() {
        String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        String cap = level.substring(0, 1).toUpperCase() + level.substring(1);
        return cap;
    }

    public void setLevel(String level) {

    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String id){
        participants.add(id);
    }


}
