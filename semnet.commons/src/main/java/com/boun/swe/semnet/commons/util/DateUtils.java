package com.boun.swe.semnet.commons.util;

import java.util.Date;

public class DateUtils {

    private static long ONE_MINUTE = 1000 * 60;
    private static long ONE_HOUR = ONE_MINUTE * 60;
    private static long ONE_DAY = ONE_HOUR * 24;
    private static long ONE_WEEK = ONE_DAY * 7;

    public static String calculateDateDifference(Date date){
    	
    	long diffMillis = new Date().getTime() - date.getTime();
    	
    	long weekDiff = diffMillis % ONE_WEEK;
    	long dayDiff = weekDiff % ONE_DAY;
    	long hourDiff = dayDiff % ONE_HOUR;
    	long minuteDiff = dayDiff % ONE_MINUTE;
    	
    	long week = diffMillis / ONE_WEEK;
    	long day = weekDiff / ONE_DAY;
    	long hour = dayDiff / ONE_HOUR;
    	long minute = hourDiff / ONE_MINUTE;
    	long seconds = minuteDiff / 1000;
    	
    	StringBuilder builder = new StringBuilder();
    	
    	if(week > 0){
    		builder.append(week).append("w");
    		if(day > 0){
    			builder.append(" ").append(day).append("d");	
    		}
    		return builder.toString();
    	}
    	
    	if(day > 0){
    		builder.append(day).append("d");
    		if(hour > 0){
    			builder.append(" ").append(hour).append("h");	
    		}
    		return builder.toString();
    	}
    	
    	if(hour > 0){
    		builder.append(hour).append("h");
    		if(minute > 0){
    			builder.append(" ").append(minute).append("m");	
    		}
    		return builder.toString();
    	}
    	
    	if(minute > 0){
    		builder.append(minute).append("m");
    		if(seconds > 0){
    			builder.append(" ").append(seconds).append("s");	
    		}
    		return builder.toString();
    	}
    	
    	if(seconds > 0){
			builder.append(seconds).append("s");	
		}
    	return builder.toString();
    }
}
