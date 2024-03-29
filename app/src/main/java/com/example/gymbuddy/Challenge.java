package com.example.gymbuddy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Challenge implements Serializable {

    public String name;
    public String level;
    public Date startDate;
    public Date endDate;
    public int limit;
    public int currentProgress;
    public int doneDaily;
    public ArrayList<String> participants;
    public HashMap<String,Integer> participantsProgress;

    public HashMap<String, Integer> getParticipantsProgress() {
        return participantsProgress;
    }

    public void setParticipantsProgress(HashMap<String, Integer> participantsProgress) {
        this.participantsProgress = participantsProgress;
    }

    public Challenge(){

    }

    public Challenge(String name, String level, Date startDate) {
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

        currentProgress = 0;
        doneDaily = 0;


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, 10);

        endDate = calendar.getTime();

        participants = new ArrayList<>();
        participantsProgress = new HashMap<>();

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

    public void setLevel(String level) { this.level = level; }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
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

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public void addParticipant(String id){
        participants.add(id);
        participantsProgress.put(id,0);
    }

    public int getDoneDaily() {
        return doneDaily;
    }

    public void setDoneDaily(int doneDaily) {
        this.doneDaily = doneDaily;
    }

    public void updateDoneDaily(int doneDaily) {
        this.doneDaily = doneDaily;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }


}
